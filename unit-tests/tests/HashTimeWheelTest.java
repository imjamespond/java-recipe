package test;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * 12-6-6 ����2:46
 * 
 * @author jiaguotian Copyright 2012 Sohu.com Inc. All Rights Reserved.
 */
public class HashTimeWheelTest {

	public static void main(String[] argv) {
		final Timer timer = new HashedWheelTimer();
		MyTimerTask myTask = new MyTimerTask();
		timer.newTimeout(myTask, 10, TimeUnit.SECONDS);
	}
}

class MyTimerTask implements TimerTask {
	
	@Override
	public void run(Timeout arg0) throws Exception {
		System.out.println("thread:" + Thread.currentThread().getName());	
	}
}

