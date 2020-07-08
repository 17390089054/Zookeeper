package org.wrf.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;

/**
 * Zookeeper实现Leader选举
 * @ClassName: CuratorLeader
 * @Description:
 * @author: knight
 * @date 2020年7月6日 下午4:14:47
 */
public class CuratorLeader {
	//Zookeeper连接信息
	private static final String ZK_ADDRESS="192.168.0.103:2181";
	private static final String ZK_PATH="/zk";
			
	public static void main(String[] args) throws InterruptedException {

		LeaderSelectorListener listener=new LeaderSelectorListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				
			}
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.err.println(Thread.currentThread().getName()+" take leadership!");
				Thread.sleep(5000L);
				System.err.println(Thread.currentThread().getName()+" relinquish leadership!");
			}
		};
		
		new Thread(()->{
			//注册监听
			registerListener(listener);
		},"t1") .start();
		
		new Thread(()->{
			//注册监听
			registerListener(listener);
		},"t2") .start();
		
		new Thread(()->{
			//注册监听
			registerListener(listener);
		},"t3") .start();
		
		Thread.sleep(Integer.MAX_VALUE);
	}

	private static void registerListener(LeaderSelectorListener listener) {
		//1.Connect to zk
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10, 5000));
		client.start();
		
		//2.Ensure path
		try {
			new EnsurePath(ZK_PATH).ensure(client.getZookeeperClient());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3.Register listener
		@SuppressWarnings("resource")
		LeaderSelector selector=new LeaderSelector(client,ZK_PATH, listener);
		selector.autoRequeue();
		selector.start();
	}
	
	
}
