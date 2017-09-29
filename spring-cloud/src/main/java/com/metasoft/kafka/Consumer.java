package com.metasoft.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer extends Thread {
	Logger log = LoggerFactory.getLogger(Consumer.class);
	
	private KafkaConsumer<Integer, String> consumer;
	private final String topic;
	private Boolean running=true;
	private Properties props = new Properties();

	public Consumer(String topic) {  
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "10000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");//解序列

		
		this.topic = topic;
	}
	public Consumer putServers(String servers){
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		return this;
	}
	
	public Consumer putGroupId(String groupId){
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return this;
	}
	
	public Consumer create(){
		consumer = new KafkaConsumer<>(props);
		return this;
	}

	@Override
	public void run() { 
		String[] topics = {this.topic};
		consumer.subscribe(Arrays.asList(topics));
		long begin = System.currentTimeMillis();
		while (true) {
			this.poll();
			System.out.printf("elapsed: %d\r",System.currentTimeMillis()-begin).flush();
			synchronized(running) {
				if(!running){ 
					consumer.close();
					return;
				}
			} 
		}
	}
	
	public void pollFromBeginning(){ 
		List<PartitionInfo> partitions = consumer.partitionsFor(topic);
		List<TopicPartition> topicPas = new ArrayList<TopicPartition>();
		for(PartitionInfo pa : partitions){
			topicPas.add(new TopicPartition(topic, pa.partition()));
		}
		consumer.assign(topicPas);
		consumer.seekToBeginning(topicPas);
		poll();
	}
	
	public void pollFromEnd(){ 
		List<PartitionInfo> partitions = consumer.partitionsFor(topic);
		List<TopicPartition> topicPas = new ArrayList<TopicPartition>();
		for(PartitionInfo pa : partitions){
			topicPas.add(new TopicPartition(topic, pa.partition()));
		}
		consumer.assign(topicPas);
		consumer.seekToEnd(topicPas);
		poll();
	}
	
	public void pollFrom(int offset){ 
		List<PartitionInfo> partitions = consumer.partitionsFor(topic);
		List<TopicPartition> topicPas = new ArrayList<TopicPartition>();
		for(PartitionInfo pa : partitions){
			TopicPartition tp = new TopicPartition(topic, pa.partition());
			topicPas.add(tp);
		}
		consumer.assign(topicPas);
		for(TopicPartition tp : topicPas){
			consumer.seek(tp, offset);
		}
		
		poll();
	}
	
	private void poll(){ 
		ConsumerRecords<Integer, String> records = consumer.poll(9000);
		for (ConsumerRecord<Integer, String> record : records) {
			ConsumerHelper.Consume(record);
		}
	}

	public String name() {
		return null;
	}

	public boolean isInterruptible() {
		return false;
	}
}