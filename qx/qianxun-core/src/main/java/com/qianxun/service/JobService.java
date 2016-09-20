package com.qianxun.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.qianxun.model.Constant;
import com.qianxun.model.job.IJob;

@Service
public class JobService {
	//private static final Logger logger = LoggerFactory.getLogger(JobService.class);
	
	private BlockingQueue<IJob> jobQueue;
	
	JobService(){
		jobQueue = new LinkedBlockingQueue<IJob>(Constant.MAX_JOB);
	}
	
	@PostConstruct
	public void init(){
		Consumer<IJob> c1 = new Consumer<IJob>(jobQueue);
		Thread thread = new Thread(c1);
		thread.setName("JobService-Thread");
		thread.start();
	}
	
	public void produce(IJob job){
		try {
			jobQueue.put(job);
		} catch (InterruptedException e) {
			
		}
	}
	public int getJobNum(){
		return jobQueue.size();
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
