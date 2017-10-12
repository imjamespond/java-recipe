import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1,
	controlledShutdown=true,
	topics = {
	         Simple1.topic1, Simple1.topic2})
public class Simple2 {

	//@Autowired
	//private Config config;
	
	@Autowired
	private Listener listener;

	@Autowired
	private KafkaTemplate<Integer, String> template;

	@Test
	public void testPort() throws InterruptedException {
	    template.send(Simple1.topic1, 0, "foo");
	    template.flush();
	    assertTrue(this.listener.latch1.await(10, TimeUnit.SECONDS));
		//assertThat(config.broker.getBrokersAsString()).isEqualTo("127.0.0.1:" + this.config.port);
	}

	@Configuration
	@EnableKafka
	public static class Config {

		//private int port;

		@Autowired
		public KafkaEmbedded broker;
		
		@Value("${" + KafkaEmbedded.SPRING_EMBEDDED_KAFKA_BROKERS + "}")
        private String brokerAddresses;

//		@Bean
//		public KafkaEmbedded broker() throws IOException {
//			KafkaEmbedded broker = new KafkaEmbedded(1);
//			ServerSocket ss = ServerSocketFactory.getDefault().createServerSocket(0);
//			this.port = ss.getLocalPort();
//			ss.close();
//			broker.setKafkaPorts(this.port);
//			return broker;
//		}
		
		@Bean
	    ConcurrentKafkaListenerContainerFactory<Integer, String>
	                        kafkaListenerContainerFactory() {
	        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
	                                new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory());
	        return factory;
	    }

	    @Bean
	    public ConsumerFactory<Integer, String> consumerFactory() {
	        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	    }

	    @Bean
	    public Map<String, Object> consumerConfigs() {
	        //Map<String, Object> props = Simple1.consumerProps();
	    	Map<String, Object> consumerProps = 
	    			KafkaTestUtils.consumerProps(Simple1.group, "false", broker);
			consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
			return consumerProps;
	    }

	    @Bean
	    public Listener listener() {
	        return new Listener();
	    }

	    @Bean
	    public ProducerFactory<Integer, String> producerFactory() {
	        return new DefaultKafkaProducerFactory<>(producerConfigs());
	    }

	    @Bean
	    public Map<String, Object> producerConfigs() {
	        //Map<String, Object> props = Simple1.senderProps();
	        return KafkaTestUtils.producerProps(broker);
	    }

	    @Bean
	    public KafkaTemplate<Integer, String> kafkaTemplate() {
	        return new KafkaTemplate<Integer, String>(producerFactory());
	    }

	}

	public static class Listener {

	    private final CountDownLatch latch1 = new CountDownLatch(1);

	    @KafkaListener(id = "foo", topics = Simple1.topic1 
	    		/*,containerFactory="kafkaListenerContainerFactory"*/)
	    public void listen1(String foo) {
	    	System.out.println("===========listen1:"+foo);
	        this.latch1.countDown();
	    }

	}
}
