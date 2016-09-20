package com.james.jetty.vo;

public class MyResult<T> {
	private final static String ok = "ok";
	private final static String error = "error";
	public String msg;
	private T result;

	public MyResult(String msg) {
		super();
		this.msg = msg;
	}
	
	public MyResult() {
		super();
		this.msg = ok;
	}
	
	public void setResult(T obj) {
		result = obj;
	}	

	public T getResult() {
		return result;
	}

	public static MyResult<Object> error(){
		return new MyResult<Object>(error);
	}
	
	public static MyResult<Object> ok(){
		return new MyResult<Object>(ok);
	}
}
