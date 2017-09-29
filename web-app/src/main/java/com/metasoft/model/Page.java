package com.metasoft.model;

/**
 * 分页辅助类
 * 
 */
public class Page {
	public int total;
	public int size;
	public int page;
	public int getOffset(){
		return size*(page-1);
	}
	public int getLimit() {
		// in order to support old style db2 grammar, limit = size * page
		return size * page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
