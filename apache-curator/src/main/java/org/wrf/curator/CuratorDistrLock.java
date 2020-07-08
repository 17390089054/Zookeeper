package org.wrf.curator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

/**
 * Apache Curator实现分布式锁
 * @ClassName: CuratorDistrLock
 * @Description:
 * @author: knight
 * @date 2020年7月6日 下午3:59:59
 */
public class CuratorDistrLock {
	//zookeeper连接信息
	private static final String ZK_ADDRESS="192.168.0.103:2181";
	private static final String ZK_LOCK_PATH="/zk";
	
	public static void main(String[] args) {
		//1.Connect to zk
		CuratorFramework client=CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10, 5000));
		client.start();
		System.out.println("zk client start successfully!");
		
		new Thread(()->{
			//抢锁
			doWithLock(client);
		},"t1").start();
		
		new Thread(()->{
			//抢锁
			doWithLock(client);
		},"t2").start();;
		
	}
	
	private static void doWithLock(CuratorFramework client) {
		InterProcessLock lock=new InterProcessMutex(client, ZK_LOCK_PATH);
		try {
			if(lock.acquire(10*1000, TimeUnit.SECONDS)) {
				System.out.println(Thread.currentThread().getName()+" hold lock!");
				Thread.sleep(20000L);
				System.out.println(Thread.currentThread().getName()+" release lock!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				lock.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
