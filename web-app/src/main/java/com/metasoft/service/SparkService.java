package com.metasoft.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.metasoft.kafka.Consumer;
import com.metasoft.kafka.KafkaProperties;
import com.metasoft.kafka.Producer;
import com.metasoft.kafka.ProducerHelper;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
 

@Service
public class SparkService {
	private static final Logger log = LoggerFactory.getLogger(SparkService.class);

	public static class Result {
		String status; 
		Object result;
		
		public Result(String status) {
			this.status = status;
		}

		public Result(String status, Object result) {
			this.status = status;
			this.result = result;
		}

		public static Result AsyncTimeout() { 
			return new Result("async-timeout");
		}
		
		public static Result TaskTimeout() { 
			return new Result("task-timeout");
		}

		public static Result Invalid() {
			return new Result("invalid");
		}
		
		public static Result Complete(Object result) {
			return new Result("completed", result);
		}
	}

	public static class Task {
		DeferredResult<Result> result;
		
		public Task(){ }
		
		public Task(long defer, Result rs){
			result = new DeferredResult<Result>(defer, rs);
		}
		
		public Task(Result rs){
			result = new DeferredResult<Result>(null, rs);
		}

		public DeferredResult<Result> getResult() {
			return result;
		}

		public void setResult(long defer, Result rs) {
			result = new DeferredResult<Result>(defer, rs);
		}

	}
	
	HashedWheelTimer timer = new HashedWheelTimer();
	ConcurrentMap<String, Task> taskMap = new ConcurrentHashMap<String, Task>();
	Producer producer;
	Consumer consumerThread;

	@Value("${kfk.topic}")
	public String kfkTopic;
	@Value("${kfk.servers}")
	public String kfkServers;
	@Value("${kfk.client.id}")
	public String kfkClientId;
	@Value("${kfk.group.id}")
	public String kfkGroupId;
	
	@PostConstruct
	void init(){
		producer = new Producer( false).
				putClientId(kfkClientId).putServers(kfkServers).create();
		producer.start();
		
		consumerThread = new Consumer(kfkTopic).putGroupId(kfkGroupId)
				.putServers(KafkaProperties.BOOTSTRAP_SERVERS).create();
		consumerThread.start();
	}
	
	@PreDestroy
	void destroy() throws InterruptedException{
		producer.join();
		consumerThread.join();
	}

	public String addTask() {
		String taskId = UUID.randomUUID().toString();
		taskMap.put(taskId, new Task());
		
		timer.newTimeout(new TimerTask(){

			@Override
			public void run(Timeout timeout) throws Exception { 
				Task task = rmTask(taskId);
				if(task!=null && task.getResult()!=null 
						&& !task.getResult().isSetOrExpired()){ 
					task.getResult().setResult(Result.TaskTimeout());
				}
			}
		//TODO get timeout configurable	
		}, 30l, TimeUnit.SECONDS);
		
		return taskId;
	}

	public Task getTask(String taskId) {
		Task task = taskMap.get(taskId); 
		return task;
	}
	public Task rmTask(String taskId) { 
		return taskMap.remove(taskId);
	}

	public Producer getProducer() {
		return producer;
	}

	public static void Test(ProducerHelper test){
		SparkService ss = ApplicationService.getBean(SparkService.class);
		Task task = ss.rmTask(test.getToken());
		if(task!=null && task.getResult()!=null 
				&& !task.getResult().isSetOrExpired()){ 
			task.getResult().setResult( Result.Complete(test.getArg()));
		}
		
		log.debug((String) test.getArg());
	}
}
