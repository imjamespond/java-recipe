import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;
import com.metasoft.thrift.MsgService;
import com.metasoft.thrift.MsgStruct;

public class BlockingClient {
	static Logger log = LoggerFactory.getLogger(BlockingClient.class);

	public static void main(String[] args) throws InterruptedException {
		BasicConfigurator.configure();
		
		Thread[] threads = new Thread[8];
		for(int i=0;i<threads.length;i++){
			Thread t=new Thread(new Runnable(){
				@Override
				public void run() {
					test();
			}});
			threads[i] = t;
			t.start();
		}
		for(Thread t:threads)
			t.join();
	}
	
	static void test(){
		TSocket transport = new TSocket("localhost", 9090);
        try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
        
        TProtocol protocol = new  TBinaryProtocol(transport);
        MsgService.Client client = new MsgService.Client(protocol);
        
        MsgStruct msg = new MsgStruct("foo","bar");
        try {
        	for(int i=0;i<5;i++){
        		client.send(msg);
        		log.debug("sending msg done");
        	}
		} catch (TException e) {
			e.printStackTrace();
		}
        
        transport.close();
	}
}
