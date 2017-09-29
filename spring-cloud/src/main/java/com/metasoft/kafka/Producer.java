package com.metasoft.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
 

public class Producer extends Thread {
	private KafkaProducer<Integer, String> producer;  
	private LinkedBlockingQueue<ProducerHelper> helpers = new LinkedBlockingQueue<ProducerHelper>();
	private Boolean running=true;
	private Properties props = new Properties();

	public Producer( Boolean isAsync) { 
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "3000");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 
	}
	
	public Producer putServers(String servers){
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		return this;
	}
	
	public Producer putClientId(String clientId){
		props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		return this;
	}
	
	public Producer create(){
		producer = new KafkaProducer<>(props); 
		return this;
	}

	public void run() { 
		while(true){
			ProducerHelper helper;
			try {
				helper = helpers.take();
				send(helper.getJSON(), helper.getTopic());
			} catch (InterruptedException e) { 
				e.printStackTrace();
			} 
			
			synchronized(running) {
				if(!running){ 
					producer.close();
					return;
				}
			} 
		}
		
	}
	
	public void produce(ProducerHelper helper){
		try {
			helpers.put(helper);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
	}
	
	public void sendAsync(String msg, String topic, Callback callback){ 
		producer.send(new ProducerRecord<>(topic, 1, msg), callback);//new DemoCallBack(startTime, 1, msg)
	}
	public void send(String msg, String topic){ 
		try {
			producer.send(new ProducerRecord<>(topic, 1, msg)).get(); 
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		producer.close();
	}
}

class DemoCallBack implements Callback {

	private final long startTime;
	private final int key;
	private final String message;

	public DemoCallBack(long startTime, int key, String message) {
		this.startTime = startTime;
		this.key = key;
		this.message = message;
	}

	/**
	 * A callback method the user can implement to provide asynchronous handling
	 * of request completion. This method will be called when the record sent to
	 * the server has been acknowledged. Exactly one of the arguments will be
	 * non-null.
	 *
	 * @param metadata
	 *            The metadata for the record that was sent (i.e. the partition
	 *            and offset). Null if an error occurred.
	 * @param exception
	 *            The exception thrown during processing of this record. Null if
	 *            no error occurred.
	 */
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (metadata != null) {
			System.out.println("message(" + key + ", " + message + ") sent to partition(" + metadata.partition() + "), "
					+ "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
		} else {
			exception.printStackTrace();
		}
	}
}