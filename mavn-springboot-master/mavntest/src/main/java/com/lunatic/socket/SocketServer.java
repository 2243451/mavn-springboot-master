package com.tocel.partrol.manage.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tocel.framework.utils.StringUtils;
import com.tocel.partrol.manage.common.ClientData;
import com.tocel.partrol.manage.common.EmpStatus;
import com.tocel.partrol.manage.common.TerminalStatus;
import com.tocel.partrol.manage.config.MySpringConfigurator;
import com.tocel.partrol.manage.domain.BaseTerminalOnline;
import com.tocel.partrol.manage.dto.ClientUserDTO;
import com.tocel.partrol.manage.dto.TerminalOnlineDTO;
import com.tocel.partrol.manage.dto.TerminalRequestDTO;
import com.tocel.partrol.manage.service.BaseEmpService;
import com.tocel.partrol.manage.service.TerminalOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 丁伟成
 * @version V3.1.00
 * @Package com.tocel.partrol.manage.server
 * @Description: TODO(websocket服务类)
 * @date 2019年1月28日15:17:00
 */
@ServerEndpoint(value = "/socketServer/{uuid}", configurator = MySpringConfigurator.class)
@Component
public class SocketServer {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    @Autowired
    private BaseEmpService baseEmpService;

    @Autowired
    private TerminalOnlineService terminalOnlineService;

    /**
     * 用户连接时触发
     *
     * @param session
     */
    @OnOpen
    public void open(Session session, @PathParam(value = "uuid") String uuid) {
        if (StringUtils.isNotEmpty(uuid)) {
            if (uuid.contains("web")) {
                ClientData.onLineWebs.put(uuid, session);
            } else if (uuid.contains("sendCreatInfo")) {
                ClientData.onLines.put(uuid, session);
                ClientData.onLinesTerminal.put(session.getId(), session);
            } else {
                ClientData.onLines.put(uuid, session);
                ClientUserDTO clientUserDTO = new ClientUserDTO();
                clientUserDTO.setUuid(uuid);
                clientUserDTO.setLastConnTime(new Date());
                ClientData.clientUserDTOs.put(session.getId(), clientUserDTO);
            }
            logger.info(session.getId() + "#" + uuid + "," + "连接上了");
        }
    }


    /**
     * 收到信息时触发
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            if (StringUtils.isNotEmpty(message)) {
                System.out.println(message);
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
                        String[] terminalCodes = terminalCode.split(",");
                        for (String code : terminalCodes) {
                            sendMessageToTerminal(msg, code);
                        }
                    }

                    /**向终端发送更新信息*/
                    if (TerminalStatus.SENDUPDATELICENCESTATUS.getIndex().toString().equals(requestDTO.getType())) {
                        System.out.println("发送给终端的json：======" + message);
                        String[] terminalCodes = terminalCode.split(",");
                        for (String code : terminalCodes) {
                            sendMessageToTerminal(msg, code);
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
            ClientData.getMessageQueue().offer(message);
        }
    }

    /**
     * 推送消息到app（在线监测报警推送）
     *
     * @param msg
     */
    private void sendMessageToApp(String msg) throws IOException {
        if (null != ClientData.onLinesTerminal && ClientData.onLinesTerminal.size() > 0) {
            for (String key : ClientData.onLinesTerminal.keySet()) {
                sendMsg(ClientData.onLinesTerminal.get(key), msg);
            }
        }
    }

    /**
     * 连接关闭触发
     */
    @OnClose
    public void onClose(Session session, @PathParam(value = "uuid") String uuid) {
        logger.info(uuid + "####客户端下线了####");
        if (uuid.contains("sendCreatInfo")) {
            ClientData.onLines.remove(uuid);
            ClientData.onLinesTerminal.remove(session.getId());
        } else {
            ClientData.onLineWebs.remove(uuid);
            if (StringUtils.isNotEmpty(session.getId())) {
                ClientUserDTO clientUserDTO = ClientData.clientUserDTOs.get(session.getId());
                if (clientUserDTO != null) {
                    ClientData.onLines.remove(clientUserDTO.getUuid());
                    ClientData.clientUserDTOs.remove(session.getId());
                }
            }
        }
    }


    /**
     * 发生错误时触发
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam(value = "uuid") String uuid) {
        logger.info(uuid + "####客户端掉线了####");
        if (uuid.contains("sendCreatInfo")) {
            ClientData.onLines.remove(uuid);
            ClientData.onLinesTerminal.remove(session.getId());
        } else {
            ClientData.onLineWebs.remove(uuid);
            if (StringUtils.isNotEmpty(session.getId())) {
                ClientUserDTO clientUserDTO = ClientData.clientUserDTOs.get(session.getId());
                if (clientUserDTO != null) {
                    ClientData.onLines.remove(clientUserDTO.getUuid());
                    ClientData.clientUserDTOs.remove(session.getId());
                }
            }
        }
        // error.printStackTrace();
    }

    /**
     * 信息发送的方法
     *
     * @param message
     * @param userId
     */
    public static void sendMessage(String message, String userId) throws IOException {
        if (userId != null) {
            Session s = ClientData.onLines.get(userId);
            if (s != null) {
                sendMsg(s, message);
            }
        }

    }

    private synchronized static void sendMsg(Session s, String message) throws IOException {
        s.getAsyncRemote().sendText(message);
    }

    /**
     * 信息发送到web的方法
     *
     * @param message
     * @param code
     */
    public static void sendMessageToWeb(String message, String code) throws IOException {

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

//            boolean isSucc = false;
//            for(Map.Entry<String, ClientUserDTO> entry: ClientData.clientUserDTOs.entrySet()) {
//                ClientUserDTO dto = entry.getValue();
//                String terminalCode = dto.getTerminalCode();
//                if(null != dto &&StringUtils.isNotEmpty(terminalCode)&& terminalCode.equals(code)){
//                    Session s = ClientData.onLines.get(dto.getUuid());
//                    if (s != null) {
//                        sendMsg(s,message);
//                        isSucc = true;
//                    }else{
//                        throw new Exception();
//                    }
//                }
//            }
//            if(!isSucc){
//                throw new Exception();
//            }
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
    public synchronized static void sendAll(String msg) throws IOException {
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

    public synchronized static void SendMany(String msg, String[] persons) throws IOException {
        for (String userid : persons) {
            sendMessage(msg, userid);
        }

    }

}
