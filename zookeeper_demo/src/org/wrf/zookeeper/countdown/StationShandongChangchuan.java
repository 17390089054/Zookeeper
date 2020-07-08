package org.wrf.zookeeper.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * 山东长川捡查中心
 * @ClassName: StationBeijinIMooc
 * @Description:
 * @author: knight
 * @date 2020年6月21日 下午4:20:19
 */
public class StationShandongChangchuan extends DangerCenter{

	public StationShandongChangchuan(CountDownLatch latch) {
		super(latch, "山东长川调度站");

}

	@Override
	public void check() {
		System.out.println("正在检查["+this.getStation()+"]...");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("检查[ "+this.getStation()+"] 完毕，可以发车~");
	}

}