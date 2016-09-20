package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		LinkedBlockingQueue<Job> q = new LinkedBlockingQueue<Job>();

		Consumer<Job> c1 = new Consumer<Job>(q);
		Consumer<Job> c2 = new Consumer<Job>(q);

		new Thread(c1).start();
		new Thread(c2).start();
		
		for(int i=0;i<10;i++){
			try {
				q.put(new JobTest(i));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Thread.sleep(10000l);
		System.out.println(q.size());
	}

}

class Consumer<T extends Job> implements Runnable {
	private final BlockingQueue<T> queue;

	Consumer(BlockingQueue<T> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	void consume(T t) {
		t.doJob();
	}
}

abstract class Job{
	public abstract void doJob();
}

class JobTest extends Job{
	JobTest(int i){
		
	}
	@Override
	public void doJob() {
		System.out.println(Thread.currentThread().getName());
	}
	
}
