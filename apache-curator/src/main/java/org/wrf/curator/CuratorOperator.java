package org.wrf.curator;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Apache Curator实现Zookeeper基本操作
 * 创建、销毁会话，节点增删改查
 * @ClassName: CuratorOperator
 * @Description:
 * @author: knight
 * @date 2020年6月24日 下午2:16:19
 */
public class CuratorOperator {
	
	Logger logger=LoggerFactory.getLogger(CuratorOperator.class);
	
	private CuratorFramework client=null;
	public CuratorOperator() {};
	
	public CuratorOperator(String serverPath) {
		//连接字符串，会话超时时间，连接超时时间，重试策略（重试sleep时间，重试次数）
		this.client = CuratorFrameworkFactory.newClient(serverPath,
				5000,5000,new ExponentialBackoffRetry(1000, 3));
		logger.info("连接ZK服务端成功");
	};
	
	public CuratorFramework getClient() {
		//连接字符串，会话超时时间，连接超时时间，重试策略（重试sleep时间，重试次数）
		/*CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.103:2181",
				5000,5000,new ExponentialBackoffRetry(1000, 3));
		return client;*/
		return this.client;
	}
	
	/**
	 * Fluent风格创建客户端
	 * @return
	 */
	public CuratorFramework getClientByFluent() {
		return CuratorFrameworkFactory.builder()
				.connectString("192.168.0.103:2181")
				.sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.namespace("base")
				.build();
	}	
	
	/**
	 * 创建节点
	 * @throws Exception
	 */
	public void CreateNode(String path,byte[] bytes) throws Exception {
		this.client.create().creatingParentsIfNeeded()//递归创建所需父节点
		.withMode(CreateMode.PERSISTENT)//创建类型为持久节点
		.forPath(path,bytes);//目录及内容
	}
	
	/**
	 * 删除节点
	 * @param path
	 * @param version
	 * @throws Exception
	 */
	public void deleteNode(String path,int version) throws Exception {
		client.delete()
		.guaranteed() 				//保证强制删除
		.deletingChildrenIfNeeded() //递归删除子节点
		.withVersion(version)		//指定删除的版本号
		.forPath(path);
	}
	
	/**
	 * 修改节点值
	 * @param path
	 * @param version
	 * @param bytes
	 * @throws Exception
	 */
	public void setNode(String path,int version,byte[] bytes) throws Exception {
		client.setData()
		.withVersion(version)
		.forPath(path,bytes);
	}
	
	/**
	 * 获取节点值
	 * @param path
	 * @throws Exception
	 */
	public void getNode(String path) throws Exception {
		byte[] bytes=client.getData().forPath(path);
		System.err.println(new String(bytes));
	}
	
	/**
	 * 获取节点状态
	 * @param path
	 * @throws Exception
	 */
	public void getNodeState(String path) throws Exception {
		byte[] bytes = client.getData().storingStatIn(new Stat()).forPath(path);
		System.err.println(new String(bytes));
	}
	
	/**
	 * 开启一个事务
	 * @param path
	 * @throws Exception
	 */
	public void startTransaction(String path) throws Exception {
		client.inTransaction().check().forPath(path)
		.and()
		.create().withMode(CreateMode.EPHEMERAL).forPath("/nodeA","nodeA".getBytes())
		.and()
		.create().withMode(CreateMode.EPHEMERAL).forPath("/nodeB","nodeB".getBytes())
		.and()
		.commit();
	}
	
	/**
	 * 异步创建节点
	 * @param path
	 * @throws Exception 
	 */
	public void AsyncCallBack(String path) throws Exception {
		Executor executor = Executors.newFixedThreadPool(2);
		client.create()
		.creatingParentsIfNeeded()
		.withMode(CreateMode.EPHEMERAL)
		.inBackground((curatorFramework,curatorEvent)->{
			System.err.println(String.format("eventType:%s,resultCode:%s", curatorEvent.getType(),curatorEvent.getResultCode()));
		},executor)
		.forPath(path);
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		CuratorOperator operator=new CuratorOperator("192.168.0.103:2181");
		operator.getClient().start();
		//operator.CreateNode("/zk/wrf/newNode","newNode".getBytes());
		//operator.deleteNode("/zk/wrf",0);
		//operator.setNode("/zk",1,"zk-data".getBytes());
		//operator.getNode("/zk");
		//operator.getNodeState("/zk");
		//operator.startTransaction("/zk");
		operator.AsyncCallBack("/zk1");
	}

}
