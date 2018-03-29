package com.metasoft.model;

public class AccessRequest {

	private String objectId;
	private String objectName;
	private boolean hasRight;
	private String tenantId;
	
	public AccessRequest() {}
		
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public String getObjectId() {
		return objectId;
	}
	
	public void setObjectName(String name) {
		this.objectName = name;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	public void setHasRight(boolean hasRight) {
		this.hasRight = hasRight;
	}
	
	public boolean hasRight() {
		return hasRight;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public String toString() {
		return objectName;
	}
	
}
