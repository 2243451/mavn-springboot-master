package com.tocel.partrol.manage.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tocel.framework.utils.StringUtils;
import com.tocel.partrol.manage.common.ClientData;
import com.tocel.partrol.manage.common.EmpStatus;
import com.tocel.partrol.manage.common.TerminalStatus;
import com.tocel.partrol.manage.domain.BaseTerminalOnline;
import com.tocel.partrol.manage.dto.ClientUserDTO;
import com.tocel.partrol.manage.dto.TerminalOnlineDTO;
import com.tocel.partrol.manage.dto.TerminalRequestDTO;
import com.tocel.partrol.manage.service.BaseEmpService;
import com.tocel.partrol.manage.service.TerminalOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author 丁伟成
 * @version V3.1.00
 * @Package com.tocel.partrol.manage.server
 * @Description: TODO(定时任务 ， 检测终端用户在线情况)
 * @date 2019年1月28日15:17:00
 */
@EnableScheduling
@Component
public class CheckHeartBeat {

    private static Logger logger = LoggerFactory.getLogger(CheckHeartBeat.class);


    @Autowired
    private BaseEmpService baseEmpService;

    @Autowired
    private TerminalOnlineService terminalOnlineService;

    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled() {
        synchronized (this) {
            Map<String, ClientUserDTO> clientUserDTOMap = ClientData.clientUserDTOs;
            for (String key : clientUserDTOMap.keySet()) {
                ClientUserDTO clientUserDTO = clientUserDTOMap.get(key);
                Date lastLoginTime = clientUserDTO.getLastLoginTime();
                Date lastConnectionTime = clientUserDTO.getLastConnTime();
                //员工账号
                String empAccount = clientUserDTO.getEmpAccount();
                String terminalCode = clientUserDTO.getTerminalCode();
                if ((lastLoginTime != null && calLastedTime(lastLoginTime) > 10) && calLastedTime(lastConnectionTime) < 10) {//有心跳但没有登录状态
                    //离线之后 将用户的登录信息修改一下
                    if (StringUtils.isNotEmpty(empAccount)) {
                        // System.out.println(clientUserDTO.getUuid()+"----用户没有登录状态,将终端在线状态置为-1-----");
                        BaseTerminalOnline baseTerminalOnline = new BaseTerminalOnline();
                        baseTerminalOnline.setEmpAccount(empAccount);
                        baseTerminalOnline.setTerminalCode(terminalCode);
                        baseEmpService.updateStateByParam(baseTerminalOnline);
                        //将用户的在线状态切换至离线
                        clientUserDTO.setEmpAccount(null);//将账户信息设置为空
                    }
                }
                if (calLastedTime(lastConnectionTime) > 10 && calLastedTime(lastConnectionTime) > 10) {//没有心跳也没有登录状态
                    //离线之后 将用户的登录信息修改一下
                    if (StringUtils.isNotEmpty(empAccount)) {
                        //System.out.println(clientUserDTO.getUuid()+"----已经没有心跳了-----");
                        //System.out.println(clientUserDTO.getUuid()+"----用户没有登录状态,将终端在线状态置为-1-----");
                        BaseTerminalOnline baseTerminalOnline = new BaseTerminalOnline();
                        baseTerminalOnline.setEmpAccount(empAccount);
                        baseTerminalOnline.setTerminalCode(terminalCode);
                        baseEmpService.updateStateByParam(baseTerminalOnline);
                        //查询当前账户有没有其他终端上登录
                        List<BaseTerminalOnline> terminalOnlineList = terminalOnlineService.findOnlineListByAccount(empAccount);
                        if (terminalOnlineList == null || terminalOnlineList.size() <= 0) {
                            baseEmpService.updateBaseEmp(clientUserDTO.getEmpAccount(), EmpStatus.NOT_ONLINE.getByteIndex());
                        }
                        //将用户的在线状态切换至离线
                        clientUserDTO.setEmpAccount(null);//将账户信息设置为空
                    }
                }
            }
            //删除库里已经离线的终端
            terminalOnlineService.deleteOfflineData();
        }

    }

    /**
     * 循环发送未成功的socket信息
     */
    @Scheduled(cron = "0/1 * * * * *")
    public void sendMessage() {
        String message = ClientData.getMessageQueue().poll();

        try {
            if (StringUtils.isNotEmpty(message)) {
                System.out.println("补发消息：===============" + message);
                if (message.indexOf("type") > -1) {
                    //终端返回的数据包

                    TerminalRequestDTO requestDTO = JSONObject.parseObject(message, TerminalRequestDTO.class);
                    String terminalCode = requestDTO.getCode();
                    String uuid = requestDTO.getUuid();
                    String msg = requestDTO.getMsg();

                    /** 向终端推送（在线监测）*/
                    if (TerminalStatus.ONLINESTATUS.getIndex().toString().equals(requestDTO.getType())) {
                        TerminalRequestDTO terminalRequestDTO = new TerminalRequestDTO();
                        terminalRequestDTO.setType("6");
                        List<Map<String, String>> msgList = (List<Map<String, String>>) JSONArray.parse(msg);
                        terminalRequestDTO.setData(msgList);
                        terminalRequestDTO.setCode(terminalCode);
                        sendMessageToApp(JSON.toJSONString(terminalRequestDTO));

                    }
                    /**向web端发送警报*/
                    if (TerminalStatus.SENDCAVEATOWEB.getIndex().toString().equals(requestDTO.getType())) {
                        for (String code : ClientData.onLineWebs.keySet()) {
                            if (code.contains(terminalCode)) {
                                sendMessageToWeb(msg, code);
                            }
                        }
                    }
                    /**向终端发送警报*/
                    if (TerminalStatus.SENDCAVEATOTERMINAL.getIndex().toString().equals(requestDTO.getType())) {
                        System.out.println("发送给终端的json：======" + message);
                        String[] uuids = terminalCode.split(",");
                        for (String u : uuids) {
                            sendMessageToTerminal(msg, u);
                        }
                    }

                    /**心跳*/
                    if (StringUtils.isEmpty(uuid)) {
                        return;
                    }
                    Session session = ClientData.onLines.get(uuid);
                    if (session == null) {
                        return;
                    }
                    if (requestDTO != null) {
                        logger.info("发送人:" + terminalCode + "内容:" + message);
                    } else {
                        logger.info("发送人:" + "null" + "内容:" + message);
                    }

                    if (TerminalStatus.HEART.getIndex().toString().equals(requestDTO.getType())) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("{\"code\":\"");
                        sb.append(terminalCode);
                        sb.append("\",\"msg\":\"");
                        sb.append("hb");
                        sb.append("\",\"type\":");
                        sb.append("1");
                        sb.append("}");
                        System.out.println("心跳：=======" + sb.toString());
                        if (terminalCode.equals("sendCreatInfo")) {
                            if (msg.equals("hb")) {
                                if (terminalCode.equals("sendCreatInfo")) {
                                    sendMessageToApp(sb.toString());
                                }
                            }
                        } else {
                            ClientData.clientUserDTOs.get(session.getId()).setLastConnTime(new Date());
                            ClientData.clientUserDTOs.get(session.getId()).setTerminalCode(terminalCode);
                            if (msg.equals("hb")) {
                                BaseTerminalOnline baseTerminalOnline = new BaseTerminalOnline();
                                baseTerminalOnline.setTerminalCode(terminalCode);
                                baseEmpService.updateStateByTerminalCode(baseTerminalOnline);
                                sendMessage(sb.toString(), ClientData.clientUserDTOs.get(session.getId()).getUuid());
                            } else {
                                if (StringUtils.isNotEmpty(msg)) {
                                    ClientData.clientUserDTOs.get(session.getId()).setLastLoginTime(new Date());
                                    ClientData.clientUserDTOs.get(session.getId()).setEmpAccount(msg);
                                    baseEmpService.updateBaseEmp(terminalCode, EmpStatus.ONLINE.getByteIndex());
                                }
                                sendMessage(sb.toString(), ClientData.clientUserDTOs.get(session.getId()).getUuid());
                                if (requestDTO.getMonitorState() != null) {
                                    TerminalOnlineDTO terminalOnline = requestDTO.getMonitorState();
                                    //System.out.print(terminalOnline.getEmpAccount() + "," + terminalOnline.getOnlineMeetingcode() + "," + terminalOnline.getTerminalCode() + "," + terminalOnline.getJobId() + "," + terminalOnline.getOnlineType() + "====");
                                    terminalOnlineService.addTerminalOnline(terminalOnline, uuid);
                                }
                            }
                        }
                    }
                    if (TerminalStatus.LOGOUT.getIndex().toString().equals(requestDTO.getType())) {

                        //通过员工查询终端在线情况
                        BaseTerminalOnline onlineParams = new BaseTerminalOnline();
                        onlineParams.setEmpAccount(msg);
                        onlineParams.setTerminalCode(terminalCode);
                        List<BaseTerminalOnline> onlineList = terminalOnlineService.selectByEmpAccount(onlineParams);
                        if (onlineList != null && onlineList.size() > 0) {
                            for (int i = 0; i < onlineList.size(); i++) {
                                BaseTerminalOnline baseTerminalOnline = onlineList.get(i);
                                //通过终端编号查询出
                                String uuidInfo = baseTerminalOnline.getUuid();
                                String termCode = baseTerminalOnline.getTerminalCode();
                                StringBuffer sb = new StringBuffer();
                                sb.append("{\"code\":\"");
                                sb.append(termCode);
                                sb.append("\",\"msg\":\"");
                                sb.append("123");
                                sb.append("\",\"type\":");
                                sb.append("2");
                                sb.append("}");
                                if (StringUtils.isNotEmpty(uuidInfo)) {
                                    Session session2 = ClientData.onLines.get(uuidInfo);
                                    if (session2 != null) {
                                        sendMessage(sb.toString(), ClientData.clientUserDTOs.get(session2.getId()).getUuid());
                                    }
                                }

                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("onMessage方法报错：" + e.getMessage());
            //ClientData.messageQueue.offer(message);
        }
    }

    /**
     * 推送消息到app（在线监测报警推送）
     *
     * @param msg
     */
    private void sendMessageToApp(String msg) throws Exception {
        if (null != ClientData.onLinesTerminal && ClientData.onLinesTerminal.size() > 0) {
            for (String key : ClientData.onLinesTerminal.keySet()) {
                sendMsg(ClientData.onLinesTerminal.get(key), msg);
            }
        }
    }

    private int calLastedTime(Date startDate) {
        long a = System.currentTimeMillis();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        //System.out.println(startDate+","+new Date());
        //System.out.println(a+","+b+","+c);
        return c;
    }


    /**
     * 信息发送的方法
     *
     * @param message
     * @param userId
     */
    public static void sendMessage(String message, String userId) throws Exception {
        if (userId != null) {
            Session s = ClientData.onLines.get(userId);
            if (s != null) {
                sendMsg(s, message);
            }
        }

    }

    private synchronized static void sendMsg(Session s, String message) throws Exception {
        //s.getBasicRemote().sendText(message);
        s.getAsyncRemote().sendText(message);
    }

    /**
     * 信息发送到web的方法
     *
     * @param message
     * @param code
     */
    public static void sendMessageToWeb(String message, String code) throws Exception {

        if (code != null) {
            Session s = ClientData.onLineWebs.get(code);
            if (s != null) {
                sendMsg(s, message);
            }
        }

    }

    /**
     * 信息发送到终端的方法
     *
     * @param message
     * @param uuid
     */
    public synchronized static void sendMessageToTerminal(String message, String uuid) throws Exception {
        if (StringUtils.isNotEmpty(uuid)) {
            Session s = ClientData.onLines.get(uuid);
            if (s != null) {
                sendMsg(s, message);
            }
        }
    }

    /**
     * 获取当前连接数
     *
     * @return
     */
    public synchronized static int getOnlineNum() {

        return ClientData.onLines.size();
    }


    /**
     * 信息群发
     *
     * @param msg
     */
    public synchronized static void sendAll(String msg) throws Exception {
        if (null != ClientData.clientUserDTOs && ClientData.clientUserDTOs.size() > 0) {
            for (String key : ClientData.clientUserDTOs.keySet()) {
                sendMessage(msg, ClientData.clientUserDTOs.get(key).getUuid());
            }
        }
    }

    /**
     * 多个人发送给指定的几个用户
     *
     * @param msg
     * @param persons 用户s
     */

    public synchronized static void SendMany(String msg, String[] persons) throws Exception {
        for (String userid : persons) {
            sendMessage(msg, userid);
        }

    }
}
