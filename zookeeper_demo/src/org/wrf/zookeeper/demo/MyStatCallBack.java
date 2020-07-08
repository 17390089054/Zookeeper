package org.wrf.zookeeper.demo;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.data.Stat;

public class MyStatCallBack implements StatCallback{

	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		System.out.println("修改节点"+path);
		System.out.println((String)ctx);
	}

}
