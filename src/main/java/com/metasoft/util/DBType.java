package com.metasoft.util;

public class DBType {
	
	private String url;
	
	public DBType (String url) {
		this.url = url;
	}
	
	public boolean isMySql() {
		return url.startsWith("jdbc:mysql");
	}
	
	public boolean isDB2() {
		return url.startsWith("jdbc:db2");
	}

}
