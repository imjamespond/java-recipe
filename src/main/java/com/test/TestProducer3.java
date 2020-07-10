package com.test;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.util.Properties;

//The example below illustrates how the new APIs are meant to be used.
// It is similar to the example above, except that all 100 messages are part of a single transaction.
public class TestProducer3 {
    public static void main(String[] args) {
        String topicName = "shuaige";

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.254:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("transactional.id", "my-transactional-id");

        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.initTransactions();

        try {
            producer.beginTransaction();
            for (int i = 0; i < 100; i++) {
                ProducerRecord<String,String> record =
                        new ProducerRecord<String, String>(topicName, 1, "key"+Integer.toString(i), "value"+Integer.toString(i));
                producer.send(record,
                        new Callback() {
                            public void onCompletion(RecordMetadata metadata, Exception e) {
                                if (e != null) {
                                    e.printStackTrace();
                                } else {
                                    System.out.println("The offset of the record we just sent is: " + metadata.offset());
                                }
                            }
                        });
                //If you want to simulate a simple blocking call you can call the get() method immediately:
                // producer.send(record).get();
            }
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        }

        producer.close();

    }
}
