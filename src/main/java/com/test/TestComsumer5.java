package com.test;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

//Storing Offsets Outside Kafka
// 若消费记录和offset存于 关系db, 则允许提交到同一个事务中.
public class TestComsumer5 {
    public static void main(String[] args) {
        String topicName = "shuaige";
        boolean running = true;

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.0.254:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");//Configure enable.auto.commit=false
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //consumer.subscribe(Arrays.asList(topicName));
        TopicPartition partition0 = new TopicPartition(topicName, 0);
        TopicPartition partition1 = new TopicPartition(topicName, 1);
        consumer.assign(Arrays.asList(partition0, partition1));

        //When partitions are taken from a consumer the consumer will want to commit its offset for those partitions by
        // - implementing ConsumerRebalanceListener.onPartitionsRevoked(Collection). 当消费者拿到分区并提交offset时
        //When partitions are assigned to a consumer, the consumer will want to look up the offset for those new partitions 当分配分区给消费者,其查询那些分区的offset时
        // - and correctly initialize the consumer to that position by implementing ConsumerRebalanceListener.onPartitionsAssigned(Collection).和正确初始
        //consumer.subscribe(Arrays.asList(topicName), new ConsumerRebalanceListener() { ...});

        //consumer.seek(partition0, 0);//On restart restore the position of the consumer using seek(TopicPartition, long).
        //consumer.seek(partition1, 0);
        //Controlling The Consumer's Position
        //consumer.seekToBeginning(Collection<TopicPartition>);
        consumer.seekToEnd(Arrays.asList(partition0, partition1));

        try {
            while(running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.printf("partition: %d, offset: %d, record: %s\n",record.partition(), record.offset(), record.value());//Use the offset provided with each ConsumerRecord to save your position.
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }
}
