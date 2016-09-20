package com.tongyi.action;

import java.util.List;
import java.util.Map;

public class PageRequest<T> {
	private List<T> items;
	private Map<String,String> params;
	
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
