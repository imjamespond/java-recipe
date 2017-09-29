package com.metasoft.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ProducerHelper  {
	static Logger log = LoggerFactory.getLogger(ProducerHelper.class);
	
	private String topic; 
	private String token; 
	private String clazz;
	private String method;
	private String callbackClazz;
	private String callbackMethod;
	private String callbackTopic;
	Object arg;
	
    public static void test(ProducerHelper helper){
    	System.out.println(helper.getJSON());
    }
    
	public ProducerHelper(String topic) {  
		this.topic = topic;
	}
	
	public ProducerHelper( ) { 
	}
	  
	public void setClazz(String clazz){ 
		this.clazz = clazz;
	}
	public void setMethod(String method){ 
		this.method = method;
	}  
	public String getClazz( ){ 
		return clazz;
	}
	public String getMethod( ){  
		return method;
	} 
	public String getJSON(){ 
		return new Gson().toJson(this);
	} 
	public String getTopic() { 
		return topic;
	} 
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getCallbackClazz() {
		return callbackClazz;
	}

	public void setCallbackClazz(String callbackClazz) {
		this.callbackClazz = callbackClazz;
	}

	public String getCallbackMethod() {
		return callbackMethod;
	}

	public void setCallbackMethod(String callbackMethod) {
		this.callbackMethod = callbackMethod;
	}

	public String getCallbackTopic() {
		return callbackTopic;
	}

	public void setCallbackTopic(String callbackTopic) {
		this.callbackTopic = callbackTopic;
	}

}