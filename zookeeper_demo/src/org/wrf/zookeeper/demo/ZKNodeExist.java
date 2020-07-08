package org.wrf.zookeeper.demo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class ZKNodeExist implements Watcher{
	private ZooKeeper zookeeper=null;
	public static final String zkServerPath="192.168.0.103:2181";
	public static final Integer timeout=5000;
	
	public ZKNodeExist() {}
	
	public ZKNodeExist(String connectString) {
		try {
			zookeeper=new ZooKeeper(connectString,timeout,new ZKNodeExist());
		} catch (IOException e) {
			e.printStackTrace();
			if(zookeeper!=null) {
				try {
					zookeeper.close();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private static CountDownLatch latch=new CountDownLatch(1);
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		ZKNodeExist zkServer=new ZKNodeExist(zkServerPath);
		Stat stat = zkServer.getZookeeper().exists("/imooc", true);
		if(stat!=null) {
			System.out.println("查询的节点版本为dataVersion："+stat.getVersion());
		}else{
			System.out.println("该节点不存在....");
		}
		latch.await();
	}
	
	
	@Override
	public void process(WatchedEvent event) {
		if(event.getType()== EventType.NodeCreated) {
			System.out.println("节点创建");
			latch.countDown();
		}else if(event.getType()== EventType.NodeDataChanged) {
			System.out.println("节点数据改变");
			latch.countDown();
		}else if(event.getType()==EventType.NodeDeleted) {
			System.out.println("节点删除");
			latch.countDown();
		}
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}



}
