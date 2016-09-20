package com.metasoft.flying.service.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

@Service
public class JobService {
	//private static final Logger logger = LoggerFactory.getLogger(JobService.class);
	
	private BlockingQueue<IJob> jobQueue;
	
	JobService(){
		jobQueue = new LinkedBlockingQueue<IJob>();
	}
	
	//@PostConstruct
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

/*class JobTest extends Job{
	JobTest(int i){
		
	}
	@Override
	public void doJob() {
		System.out.println(Thread.currentThread().getName());
	}
	
}*/
