package com.metasoft.kafka;
import java.util.Collections;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.metasoft.kafka.ProducerHelper;
import com.metasoft.kafka.util.Commons;

public class ConsumerHelper  {
	static Logger log = LoggerFactory.getLogger(ConsumerHelper.class);
	
	public static void Consume(ConsumerRecord<Integer, String> record){
		log.info("Received message: {}-{}, ({}) at offset {}" ,record.topic() , record.key(), record.value(), record.offset());
		ConsumerHelper.DoJson(record.value());
	}
	
	public static void DoJson(String json){ 
    	try {
    		ProducerHelper helper = new Gson().fromJson(json, ProducerHelper.class); 
			String clazzname = helper.getClazz();
			if(clazzname == null)
				return;
			String methodname = helper.getMethod(); 
			helper.setTopic(helper.getCallbackTopic());
			helper.setClazz(helper.getCallbackClazz());
			helper.setMethod(helper.getCallbackMethod());
			Commons.InvokeMethod(clazzname, methodname, null, 
					Collections.singletonList(ProducerHelper.class.getName() ), Collections.singletonList(helper));
		} catch (Exception e) { 
			e.printStackTrace();
		} catch (NoSuchMethodError err){
			err.printStackTrace();
		}
	}

}