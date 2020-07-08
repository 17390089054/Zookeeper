package org.wrf.zookeeper.demo;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java客户端连接Zookeeper服务器创建会话
 * @ClassName: ZkConnect
 * @Description:
 * @author: knight
 * @date 2020年6月21日 上午10:31:49
 */

public class ZkConnect implements Watcher{
	private static final Logger logger=LoggerFactory.getLogger(ZkConnect.class);
	//zookeeper服务器地址，单个地址代表单节点，多个地址代表集群
	public static final String zkServerPath="192.168.0.103:2181";
//	public static final String zkServerPath="192.168.0.103:2181,192.168.0.104:2181,192.168.0.106:2181";
	//连接超时时间
	public static final Integer timeout=5000;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		ZooKeeper zooKeeper=new ZooKeeper(zkServerPath, timeout, new ZkConnect());
		
		logger.info("客户端开始连接zookeeper服务器...");
		logger.info("连接状态：{}",zooKeeper.getState());
		
		Thread.sleep(2000);

		logger.info("连接状态：{}",zooKeeper.getState());
	}
	
	@Override
	public void process(WatchedEvent event) {
		logger.info("接收到watch通知：{}",event);
	}

}
