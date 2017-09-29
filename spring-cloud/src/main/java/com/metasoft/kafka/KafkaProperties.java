package com.metasoft.kafka;
public class KafkaProperties {
    public static final String TOPIC = "test";
    public static final String KAFKA_SERVER_URL = "yy";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String BOOTSTRAP_SERVERS = 
    		KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000; 
    public static final String CLIENT_ID = "SimpleConsumerDemoClient"; 

    private KafkaProperties() {}
}