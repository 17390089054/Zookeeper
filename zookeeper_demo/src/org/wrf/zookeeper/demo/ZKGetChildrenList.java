package org.wrf.zookeeper.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * zk获取节点数据演示
 * @ClassName: ZKGetNodeData
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午4:56:00
 */
public class ZKGetChildrenList implements Watcher {
	
	private ZooKeeper zookeeper=null;
	
	public static final String zkServerPath="192.168.0.103:2181";
	public static final Integer timeout=5000;
//	private static Stat stat=new Stat();
	
	public ZKGetChildrenList() {}
	
	public ZKGetChildrenList(String connectString) {
		try {
			zookeeper=new ZooKeeper(connectString,timeout,new ZKGetChildrenList());
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

	private static CountDownLatch countDown=new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZKGetChildrenList zkServer=new ZKGetChildrenList(zkServerPath);
		/**
		 * 参数：
		 * 	path：节点路径
		 * 	watch：true或者false，注册一个watch事件
		 *  stat：状态
		 */
		/*	
	 	List<String> childrenList = zkServer.getZookeeper().getChildren("/imooc", true);
		for(String child:childrenList) {
			System.out.println(child);
		}
	 	*/
		//异步调用
		/*String ctx="{'callback':'ChildrenCallback'}";
		zkServer.getZookeeper().getChildren("/imooc", true,new ChildrenCallBack(),ctx);*/
		String ctx="{'callback':'ChildrenCallback'}";
		zkServer.getZookeeper().getChildren("/imooc", true,new Children2CallBack(),ctx);
		
		countDown.await();
		
	}
	
	@Override
	public void process(WatchedEvent event) {
		try {
			if(event.getType()==EventType.NodeChildrenChanged) {
				System.out.println("NodeChildrenChanged");
				ZKGetChildrenList zkServer=new ZKGetChildrenList(zkServerPath);
				List<String> childList = zkServer.getZookeeper().getChildren(event.getPath(), false);
				for(String s:childList) {
					System.out.println(s);
				}
				countDown.countDown();
			}else if(event.getType()==EventType.NodeCreated) {
				System.out.println("NodeCreated");
			}else if(event.getType()==EventType.NodeDataChanged) {
				System.out.println("NodeDataChanged");
			}else if(event.getType()==EventType.NodeDeleted) {
				System.out.println("NodeDeleted");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
	
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}

}
