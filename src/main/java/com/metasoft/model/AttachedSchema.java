package com.metasoft.model;

public class AttachedSchema {
	
	private String attachedSchemaName;
	private String ownerdbAddressId;

	public AttachedSchema(String schemaName, String dbAddressId) {
		this.attachedSchemaName = schemaName;
		this.ownerdbAddressId = dbAddressId;
	}
	
	public String getAttachedSchemaName() {
		return attachedSchemaName;
	}
	
	public String getOwerDBAddressId() {
		return ownerdbAddressId;
	}
	
}
