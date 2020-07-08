package org.wrf.zookeeper.countdown;

import java.util.concurrent.CountDownLatch;


/**
 * 抽象类，用于演示危险品化工车监控中心 统一检查
 * @ClassName: DangerCenter
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午4:08:37
 */
public abstract class DangerCenter implements Runnable{
	private CountDownLatch latch;		//计数器
	private String station;				//调度站
	private boolean ok;					//调度站针对自己的站点进行检查，是否检查ok的标志
	
	public DangerCenter(CountDownLatch latch,String station) {
		this.latch=latch;
		this.station=station;
		this.ok=false;
	}

	@Override
	public void run() {
		try {
			check();
			this.ok=true;
		} catch (Exception e) {
			e.printStackTrace();
			this.ok=false;
		}finally {
			if(latch!=null) {
				latch.countDown();
			}
		}
		
	}
	
	/**
	 * 检查危险化品车
	 * 蒸罐
	 * 汽油
	 * 轮胎
	 * gps
	 * ....
	 */
	public abstract void check();
	
	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	
	
	
}
