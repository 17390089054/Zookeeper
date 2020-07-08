package org.wrf.zookeeper.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 调度所有检查站
 * @ClassName: CheckStartUp
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午4:25:42
 */
public class CheckStartUp {
	private static List<DangerCenter> stationList;
	private static CountDownLatch latch;
	
	public CheckStartUp() {
	}
	
	public static boolean checkAllStations() throws Exception {
		//初始化3个调度站
		latch=new CountDownLatch(3);
		
		//把所有站点添加进list
		stationList=new ArrayList<>();
		stationList.add(new StationBeijinIMooc(latch));
		stationList.add(new StationJiangsuSanling(latch));
		stationList.add(new StationShandongChangchuan(latch));
		
		//使用线程池
		ExecutorService executor = Executors.newFixedThreadPool(stationList.size());
	
		for(DangerCenter dangerCenter:stationList) {
			executor.execute(dangerCenter);
		}
		
		//等待线程执行完毕
		latch.await();
		
		for(DangerCenter center:stationList) {
			if(!center.isOk())
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		boolean result = CheckStartUp.checkAllStations();
		System.out.println("监控中心针对所有危化品调度站点的检查结果为："+result);
	}
	
	
}
