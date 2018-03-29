package com.metasoft.util;
/**
 * 存放缓存对象
 */
public class CacheData {
	private Object data;
	private Long time;
	private int count;
	
	public CacheData(){
		
	}
	
	public CacheData(Object data,long time,int count){
		this.data = data;
		this.time = time;
		this.count = count;
	}
	
	public	CacheData(Object data){
		this.data = data;
		this.time = System.currentTimeMillis();
		this.count = 1;
	}
	
	public void addCount(){
		count++;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}


