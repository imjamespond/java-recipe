package com.metasoft.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

@Service
public class JobQueue {
	//private static final Logger logger = LoggerFactory.getLogger(JobService.class);
	
	private BlockingQueue<IJob> jobQueue;
	
	public JobQueue(){
		jobQueue = new LinkedBlockingQueue<IJob>();
	}
	
	Thread singleThread;
	public void initSingleThread(){
		Consumer<IJob> c1 = new Consumer<IJob>(jobQueue);
		singleThread = new Thread(c1);
		singleThread.setName("JobService-Thread");
		singleThread.start();
	}
	public void joinSingleThread(){
		try {
			singleThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void produce(IJob job){
		try {
			jobQueue.put(job);
		} catch (InterruptedException e) {
			
		}
	}
}

class Consumer<T extends IJob> implements Runnable {
	private final BlockingQueue<T> queue;

	Consumer(BlockingQueue<T> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				T t=queue.take();
				t.doJob();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}