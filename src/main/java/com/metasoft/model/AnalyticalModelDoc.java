package com.metasoft.model;

public class AnalyticalModelDoc {

	protected String name;
	protected String angleName; //视角
	protected String remarks; //备注
	protected String resPath; 
	
	public AnalyticalModelDoc(String name, String angleName, String remarks, String resPath) {
		this.name = name;
		this.angleName = angleName;
		this.remarks = remarks;
		this.resPath = resPath;
	}
	
}
