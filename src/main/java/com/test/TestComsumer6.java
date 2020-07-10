package com.test;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ISOLATION_LEVEL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import java.time.Duration;
import java.util.*;

//Exactly Once Processing
public class TestComsumer6 {
    public static void main(String[] args) {
        String topicName = "shuaige";

        Properties props = new Properties();
        props.setProperty(BOOTSTRAP_SERVERS_CONFIG, "192.168.0.254:9092");
        props.setProperty(GROUP_ID_CONFIG, "test");
        props.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "false");
        //Using a value of read_committed ensures that we don't read any transactional messages before the transaction completes.
        //通过读取事务可以保证分区, 可以复制两个debug实例测试, 原理是通过自动分区实现
        props.setProperty(ISOLATION_LEVEL_CONFIG, "read_committed");
        props.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsRevoked:");
                collection.forEach(c -> {
                    System.out.println(c.partition());
                });
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsAssigned:");
                collection.forEach(c -> {
                    System.out.println(c.partition());
                });
            }
        });

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
            int total = 0;
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    System.out.printf("partition: %d, offset: %d, record: %s, total: %d\n", record.partition(), record.offset(), record.value(), ++total);//Use the offset provided with each ConsumerRecord to save your position.
                }
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
            }
        }
    }
}
