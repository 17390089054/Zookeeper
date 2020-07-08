package org.wrf.zookeeper.demo;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java客户端会话重连
 * @ClassName: ZkConnectSessionWatcher
 * @Description:
 * @author: knight
 * @date 2020年6月21日 上午10:32:22
 */

public class ZkConnectSessionWatcher implements Watcher{
	private static final Logger logger=LoggerFactory.getLogger(ZkConnectSessionWatcher.class);
	//zookeeper服务器地址，单个地址代表单节点，多个地址代表集群
	public static final String zkServerPath="192.168.0.103:2181";
//	public static final String zkServerPath="192.168.0.103:2181,192.168.0.104:2181,192.168.0.106:2181";
	//连接超时时间
	public static final Integer timeout=5000;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		ZooKeeper zooKeeper=new ZooKeeper(zkServerPath, timeout, new ZkConnectSessionWatcher());
		//获取SessionID
		long sessionId = zooKeeper.getSessionId();
		String hexString = "0x"+Long.toHexString(sessionId);
		System.out.println(hexString);
		//获取SessionPassword
		byte[] sessionPasswd = zooKeeper.getSessionPasswd();
		
		logger.info("客户端开始连接zookeeper服务器...");
		logger.info("连接状态：{}",zooKeeper.getState());
		Thread.sleep(1000);
		logger.info("连接状态：{}",zooKeeper.getState());
			
		Thread.sleep(2000);
		
		//开始重连会话
		logger.warn("开始重连会话");
		ZooKeeper zkSession = new ZooKeeper(zkServerPath, timeout, new ZkConnectSessionWatcher(), sessionId, sessionPasswd, false);
		logger.warn("重新连接状态zkSession：{}",zkSession.getState());
		Thread.sleep(1000);
		logger.warn("重新连接状态zkSession：{}",zkSession.getState());
		
		
		
	}
	
	@Override
	public void process(WatchedEvent event) {
		logger.info("接收到watch通知：{}",event);
	}

}
