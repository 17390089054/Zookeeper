package org.wrf.zookeeper.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;


/**
 * zookeeper节点操作acl演示
 * @ClassName: ZKNodeAcl
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午6:13:02
 */
public class ZKNodeAcl implements Watcher{
	private ZooKeeper zookeeper=null;
	public static final String zkServerPath="192.168.0.103:2181";
	public static final Integer timeout=5000;
	
	public ZKNodeAcl() {}
	
	public ZKNodeAcl(String connectString) {
		try {
			zookeeper=new ZooKeeper(connectString,timeout,new ZKNodeAcl());
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
			result=zookeeper.create(path, data, acls, CreateMode.PERSISTENT);
			System.out.println("创建节点：\t"+result+"\t成功...");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		ZKNodeAcl zkServer=new ZKNodeAcl(zkServerPath);
		//任何人都可以访问
		//zkServer.create("/aclimooc", "imooc".getBytes(), Ids.OPEN_ACL_UNSAFE);
		
		//注册过的用户通过addAuthInfo才能操作节点，参考命令addauth
		zkServer.getZookeeper().addAuthInfo("digest", "imooc1:123456".getBytes());
		zkServer.create("/aclimooc/testdigest", "testdigest".getBytes(), Ids.CREATOR_ALL_ACL);
		Stat stat=new Stat();
		byte[] data = zkServer.getZookeeper().getData("/aclimooc/testdigest", false, stat);
		System.out.println(new String(data));
		zkServer.getZookeeper().setData("/aclimooc/testdigest", "now".getBytes(), 0);

		//Ip权限访问
		List<ACL> alcsIP=new ArrayList<ACL>();
		Id ipId1=new Id("ip","192.168.0.102");
		alcsIP.add(new ACL(Perms.ALL,ipId1));
		zkServer.create("/aclimooc/ip7", "ip7".getBytes(), alcsIP);
		
		//验证
		/*
		zkServer.getZookeeper().setData("/aclimooc/ip7", "啦啦啦啦".getBytes(), 0);
		byte[] data = zkServer.getZookeeper().getData("/aclimooc/ip7", true, new Stat());
		System.out.println(new String(data));
		*/
	}

	@Override
	public void process(WatchedEvent event) {
		
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}
	
}
