package com.lunatic.framework.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.java.SimpleFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Configuration
@Component
@EnableScheduling
public class SetTimeRedis {
	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;
	@Value("${spring.datasource.username}")
	private static String DEFAULTCHART = "UTF-8";
	private static Jedis jedis = null;
	private static Connection conn;
	@Value("${spring.linux.username}")
	private String userName;
	@Value("${spring.linux.password}")
	private String userPwd;

	@Scheduled(fixedDelay = 30000)
	private void creatRedisTasks() {
		System.out.println("执行定时任务开始生成====================="
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));

		String[] cNodes = clusterNodes.split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		// 分割出集群节点
		for (String node : cNodes) {
			String[] hp = node.split(":");
			nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
			// 检测redis集群节点是否挂掉（如果挂掉则自动重启）
			jedis = new Jedis(hp[0], Integer.parseInt(hp[1]));
			if (!SetTimeRedis.isReachable(jedis)) {
				// if (RedisConfig.login(hp[0],userName,userPwd)) {
				Session session = null;
				try {
				//	RedisConfig conf = new RedisConfig();
					conn = new Connection(hp[0]);
					conn.connect();// 连接
					boolean flg = conn.authenticateWithPassword(userName,
							userPwd);// 认证
					if (flg) {
						session = conn.openSession();
						session.execCommand("/usr/local/redis-3.2.11/bin/redis-server /usr/local/redis-cluster/"
								+ Integer.parseInt(hp[1]) + "/redis.conf");// 执行命令
						SetTimeRedis.processStdout(session.getStdout(),
								DEFAULTCHART);// 执行自动重启redis分支脚本
						System.out.println("主机" + hp[0] + "端口" + hp[1]
								+ "已启动！！！");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					session.close();
				}

				// }
			}
		}
	}

	/**
	 * 检测redis集群是否可连接上
	 * 
	 * @Title: isReachable
	 * @Description: TODO(用一句话描述该文件做什么)
	 * @author 刘峰
	 * @date 2018年10月8日
	 * @version V3.0
	 * @return boolean 返回类型
	 */
	public static boolean isReachable(Jedis jedis) {
		boolean isReached = true;
		try {
			jedis.connect();
			jedis.ping();
		} catch (JedisConnectionException e) {
			// e.printStackTrace();//异常不捕获直接返回true/false
			isReached = false;
		}
		System.out
				.println("The current Redis Server is Reachable:" + isReached);
		return isReached;
	}

	/**
	 * 远程登录linux的主机
	 * 
	 * @author 刘峰
	 * @since V0.1
	 * @date 2018年10月8日
	 * @return 登录成功返回true，否则返回false
	 */
	public static Boolean login(String ip, String userName, String UserPwd) {
		boolean flg = false;
		try {
			//RedisConfig conf = new RedisConfig();
			conn = new Connection(ip);
			conn.connect();// 连接
			flg = conn.authenticateWithPassword(userName, UserPwd);// 认证
			if (flg) {
				System.out.println("认证成功！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flg;
	}

	/**
	 * 解析脚本执行返回的结果集
	 * 
	 * @author 刘峰
	 * @param in
	 *            输入流对象
	 * @param charset
	 *            编码
	 * @since V0.1
	 * @date 2018年10月8日
	 * @return 以纯文本的格式返回
	 */
	public static String processStdout(InputStream in, String charset) {
		InputStream stdout = new StreamGobbler(in);
		StringBuffer buffer = new StringBuffer();
		;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					stdout, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}