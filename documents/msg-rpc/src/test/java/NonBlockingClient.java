import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

import com.google.gson.Gson;
import com.metasoft.thrift.MsgService;
import com.metasoft.thrift.MsgService.AsyncClient.send_call;
import com.metasoft.thrift.MsgStruct;

public class NonBlockingClient {
	static Logger log = LoggerFactory.getLogger(NonBlockingClient.class);

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
        MsgStruct msg = new MsgStruct("foo","bar");
        try {
            MsgService.AsyncClient client = new MsgService.AsyncClient(new TBinaryProtocol.Factory(), 
            		new TAsyncClientManager(), 
            		new TNonblockingSocket("localhost", 9090));
            
        	for(int i=0;i<5;i++){
        		client.send(msg, new AsyncMethodCallback<MsgService.AsyncClient.send_call>(){

					@Override
					public void onError(Exception exception) {
						exception.printStackTrace();
					}

					@Override
					public void onComplete(send_call response) {
						log.debug("sending msg done:{}", response.toString());
					}});
        		
        	}
		} catch (TException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
}
