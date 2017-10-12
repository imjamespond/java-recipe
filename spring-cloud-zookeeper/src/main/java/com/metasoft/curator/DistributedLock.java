package com.metasoft.curator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class DistributedLock {

	public void Lock(CuratorFramework client, String lockPath, Runnable job, long maxWait) {
		try{	
			InterProcessMutex lock = new InterProcessMutex(client, lockPath);
			if ( lock.acquire(maxWait, TimeUnit.MILLISECONDS) ) 
			{
			    try {
			    	job.run();
			    }
			    catch (Exception e) {
					e.printStackTrace();
				}
			    finally {
			        lock.release();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
