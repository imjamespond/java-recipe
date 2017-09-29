package com.metasoft.kafka;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminClient {
	Logger log = LoggerFactory.getLogger(AdminClient.class);
	
	org.apache.kafka.clients.admin.AdminClient admin;
	AdminClientConfig config;
	public AdminClient() {
		Properties props = new Properties(); 
		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
		props.put(AdminClientConfig.CLIENT_ID_CONFIG, KafkaProperties.CLIENT_ID);
		admin = KafkaAdminClient.create(props);
		
		
	}
	
	public void listTopics(){
		ListTopicsResult rs = admin.listTopics();
		KafkaFuture<Set<String>> fu = rs.names();
//		fu.thenApply( new Function<Set<String>, Integer>(){
//			@Override
//			public Integer apply(Set<String> a) { 
//				log.info("listTopics {}", a.toString());
//				return null;
//			}
//		});
		
		try {
			log.info("listTopics {}", fu.get().toString());
		} catch (InterruptedException | ExecutionException e) { 
			e.printStackTrace();
		}
	}
	
	public void describeTopics(){
		DescribeTopicsResult rs = 
				admin.describeTopics(Collections.singletonList(KafkaProperties.TOPIC) );
		Map<String, KafkaFuture<TopicDescription>> tds = rs.values();
		for(Entry<String, KafkaFuture<TopicDescription>> entry: tds.entrySet()){
			
			KafkaFuture<TopicDescription> fu = entry.getValue();
			try {
				TopicDescription td = fu.get();;
	
				log.info("Entry:{}, td:{}", entry.getKey(), td.toString());
			} catch (InterruptedException | ExecutionException e) { 
				e.printStackTrace();
			}
		}
	}
	 
	
	public void describeCluster(){
		DescribeClusterResult rs = 
				admin.describeCluster( );
		log.info("describeCluster:{}", rs.toString());
	}

}
