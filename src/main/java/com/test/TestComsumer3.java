package com.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

//Manual Offset Control
//If we allowed offsets to auto commit as in the previous example, records would be considered consumed after they were returned to the user in poll.
// 如果像之前那样允许自动提交, 记录会在poll返回后当作已消费. 为避免这样,我们可以在插入db后再提交offset
public class TestComsumer3 {
    public static void main(String[] args) {
        String topicName = "shuaige";
        boolean running = true;

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.0.254:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topicName));


        try {
            while(running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                    //Note: The committed offset should always be the offset of the next message that your application will read.
                    // 提交的offset应为下一条消息的offset, 因此,当调用commitSync时,应该加1给上一条消息的offset
                    // Thus, when calling commitSync(offsets) you should add one to the offset of the last message processed.
                }
            }
        } finally {
            consumer.close();
        }
    }
}
