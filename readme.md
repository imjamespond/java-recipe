### 创建话题  
./kafka-topics.bat --create --bootstrap-server 192.168.0.254:9092 --replication-factor 1 --partitions 1 --topic shuaige  
### 生产话题  
./kafka-console-producer.bat --bootstrap-server 192.168.0.254:9092 --topic shuaige  
### 消费话题  
./kafka-console-consumer.bat --bootstrap-server 192.168.0.254:9092 --topic shuaige --from-beginning  
### 话题列表  
./kafka-topics.bat --list --bootstrap-server 192.168.0.254:9092  

## 文档  
https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html  
https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html


## Topic  
./kafka-topics.sh --alter --topic shuaige --partitions 4  
./kafka-run-class.sh kafka.admin.ConsumerGroupCommand \
    --bootstrap-server 192.168.0.254:9092 \
    --describe --group test

## 参数  
--bootstrap-server <String: server to connect to> ```string in the form HOST1:PORT1,HOST2:PORT2. ```    
                                           