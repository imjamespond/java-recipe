package com.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestTopics {
    public static void main(String[] args) {

        Map<String, List<PartitionInfo>> topics;

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.254:9092");
        props.put("group.id", "test-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        topics = consumer.listTopics();
        consumer.close();

        for(Map.Entry<String, List<PartitionInfo>> entry: topics.entrySet()){
            System.out.println("key:"+entry.getKey());
            List<PartitionInfo> list = entry.getValue();
            for(PartitionInfo info: list){

                System.out.printf("partition: %s, topic: %s\n",info.partition(), info.topic());
            }
        }
    }
}
