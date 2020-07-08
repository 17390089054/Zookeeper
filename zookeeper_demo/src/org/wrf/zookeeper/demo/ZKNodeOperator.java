package org.wrf.zookeeper.demo;

import java.io.IOException;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * Zookeeper节点基本操作--Java API
 * https://blog.csdn.net/ouzhuangzhuang/article/details/86680258
 * @ClassName: ZKNodeOperator
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午12:15:24
 */
public class ZKNodeOperator implements Watcher{
	private ZooKeeper zk=null;
	
	public static final String zkServerPath="192.168.0.103:2181";
	public static final Integer timeout=5000;
	
	public ZKNodeOperator() {}
	
	public ZKNodeOperator(String serverPath) {
		try {
			zk=new ZooKeeper(zkServerPath,timeout,new ZKNodeOperator());
		}catch (IOException e) {
			e.printStackTrace();
			if(zk!=null) {
				try {
					zk.close();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建zk同步节点
	 * @param path
	 * @param data
	 * @param acls
	 */
	public void create(String path,byte [] data,List<ACL> acls) {
		String result="";
		try {
			/**
			 * 同步创建节点，不支持子节点的递归创建
			 * 参数：
			 * path：创建的路径
			 * data：存储的数据byte[]
			 * acl： 控制权限策略
			 * 		Ids.OPEN_ACL_UNSAFE ---> world:anyone:cdrwa
			 * 		CREATOR_ALL_ACL  ----> auth:user:password:cdrwa
			 * createMode:节点类型，是一个枚举
			 * 		PERSISTENT：持久节点
			 * 		PERSISTENT_SEQUENTIAL：持久顺序节点
			 * 		EPHEMERAL：临时节点
			 * 		EPHEMERAL_SEQUENTIAL：临时有序节点	
			 */
			result=zk.create(path, data, acls, CreateMode.PERSISTENT);
			System.out.println("创建节点：\t"+result+"\t成功...");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建zk异步节点
	 * @param path
	 * @param data
	 * @param acls
	 * @param call
	 * @throws InterruptedException
	 */
	public void create(String path,byte [] data,List<ACL> acls,CreateCallBack call) throws InterruptedException {
		/**
		 * 异步创建节点，不支持子节点的递归创建，异步有一个callback函数
		 * 参数：
		 * path：创建的路径
		 * data：存储的数据byte[]
		 * acl： 控制权限策略
		 * 		Ids.OPEN_ACL_UNSAFE ---> world:anyone:cdrwa
		 * 		CREATOR_ALL_ACL  	----> auth:user:password:cdrwa
		 * createMode:节点类型，是一个枚举
		 * 		PERSISTENT：持久节点
		 * 		PERSISTENT_SEQUENTIAL：持久顺序节点
		 * 		EPHEMERAL：临时节点
		 * 		EPHEMERAL_SEQUENTIAL：临时有序节点	
		 */
		String ctx="{'create':'success'}";
		zk.create(path, data, acls, CreateMode.PERSISTENT,new CreateCallBack(),ctx);	
		Thread.sleep(2000);
	}
	
	/**
	 * 同步修改节点
	 * @param path
	 * @param data
	 * @param oldVersion
	 * @return
	 */
	public int update(String path,byte[] data,int oldVersion) {
		try {
			Stat status = zk.setData(path, data, oldVersion);
			return status.getVersion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	/**
	 * 异步修改zookeeper节点值
	 * @param path
	 * @param data
	 * @param oldVersion
	 * @param callback
	 * @throws InterruptedException 
	 */
	public void update(String path,byte[] data,int oldVersion,StatCallback callback) throws InterruptedException {
		try {
			String ctx="{'create':'success'}";
			zk.setData(path, data, oldVersion, callback, ctx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(2000);
	}
	
	/**
	 * 同步删除节点
	 * @param path
	 * @param version
	 */
	public void delete(String path,int version) {
		try {
			zk.delete(path, version);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 异步删除节点
	 * @param path
	 * @param version
	 * @param callback
	 * @throws InterruptedException
	 */
	public void delete(String path,int version,VoidCallback callback) throws InterruptedException {
		String ctx="{'create':'success'}";
		zk.delete(path, version, callback, ctx);
		Thread.sleep(2000);
	}
	
	public ZooKeeper getZookeeper() {
		return zk;
	}
	
	public void setZookeeper(ZooKeeper zk) {
		this.zk=zk;
	}
	
	
	@Override
	public void process(WatchedEvent event) {
		
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		ZKNodeOperator zkServer=new ZKNodeOperator(zkServerPath);
		//zkServer.create("/testdeletenode", "testdeletenode".getBytes(),Ids.OPEN_ACL_UNSAFE);
		//System.out.println(zkServer.update("/testnode", "111".getBytes(), 1));
		//zkServer.update("/testnode", "async".getBytes(), 2,new MyStatCallBack());
		//zkServer.delete("/testdeletenode", 0);
		zkServer.delete("/testdeletenode", 0,new DeleteCallBack());
	}

}
