import com.metasoft.kafka.Consumer;
import com.metasoft.kafka.KafkaProperties;
import com.metasoft.kafka.Producer;

/* SimpleApp.java */

public class SimpleApp {
	public static Producer producer = new Producer( false);
	
	public static void main(String[] args) throws InterruptedException {
		String topic = args.length == 0 ? KafkaProperties.TOPIC : args[0].trim();
		Consumer consumerThread = new Consumer(topic).putGroupId("SimpleApp")
				.putServers(KafkaProperties.BOOTSTRAP_SERVERS).create();
		consumerThread.start();
		
		producer.putClientId("SimpleApp").putServers(KafkaProperties.BOOTSTRAP_SERVERS).create();
		producer.start();
		
		consumerThread.join();
		producer.join();
	}
}