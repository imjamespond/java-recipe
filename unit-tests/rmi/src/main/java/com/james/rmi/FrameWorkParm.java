package com.james.rmi;

public final class FrameWorkParm implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5358478825360802860L;
	// passed data from Client
	private Object client_data;
	// Function name
	private String func_name;
	// maximum time to wait for a reply
	private int wait_time;
	// priority of the request
	private int priority;
	
	public Object getClient_data() {
		return client_data;
	}
	public void setClient_data(Object client_data) {
		this.client_data = client_data;
	}
	public String getFunc_name() {
		return func_name;
	}
	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}
	public int getWait_time() {
		return wait_time;
	}
	public void setWait_time(int wait_time) {
		this.wait_time = wait_time;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}