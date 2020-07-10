package com.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

//Manual Partition Assignment
// 之前kafka动态分配一个公平的分区,在组内. 然而某些时候你可能要更好的控制指定的分区. 比如:1,若进程维护本地状态(本地kv store),它只能得到它维护的分区的记录
// 2,若进程本身高可用,它会自动重启,kafka不需要检测失败!!并重新分配分区!!,因为消费进程可能在其它机器上重启
// 要使用此模式,不用 subscribe,而是call assign(Collection) 并传入完整的 你想消费的分区列表
// 提示: 不能混和手动assignment和动态subscription分区.
public class TestComsumer4 {
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
        //consumer.subscribe(Arrays.asList(topicName));
        TopicPartition partition0 = new TopicPartition(topicName, 0);
        TopicPartition partition1 = new TopicPartition(topicName, 1);
        consumer.assign(Arrays.asList(partition0, partition1));

        // 提示: 使用自动提交也会给你 "至少一次" 的传送, 但需要 你必须消费所有从poll返回的数据 在任何后续调用前或是close前.
        // - 若你没这么操作这两个之一,则有可能是提交了的offset到了已消费位置之前,which会导致丢失记录. 手动控制的好处是你有直接的控制当一个记录能消费时
        // 之前用commitSync 来标记所有记录提交, 某些情况你可能要更好控制哪些记录被提交, 通过指定一个offset, 下面我们在完成处理每个分区的记录后 再提交offset
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
                    //Note: The committed offset should always be the offset of the next message that your application will read.
                    // - 提交的offset应为下一条消息的offset, 因此,当调用commitSync时,应该加1给上一条消息的offset
                    // Thus, when calling commitSync(offsets) you should add one to the offset of the last message processed.
                }
            }
        } finally {
            consumer.close();
        }
    }
}
