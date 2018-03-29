package com.metasoft.model;

import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;

public class DataSource extends DBAddress {
	
	protected String tenantName;
	protected String dbSubType;
	
	public DataSource() {}
	
	public DataSource(DBAddress dbAddress, String tenantName) {
		this.name = dbAddress.getName();
		this.jdbcURL = dbAddress.getJdbcURL();
		this.dbUser = dbAddress.getDbUser();
		this.dbPwd = dbAddress.getDbPwd();
		this.createDate = dbAddress.getCreateDate();
		this.creator = dbAddress.getCreator();
		this.modifyDate = dbAddress.getModifyDate();
		this.modifier = dbAddress.getModifier();
		this.desc = dbAddress.getDesc();
		this.dbAddressId = dbAddress.getDbAddressId();
		this.dirId = dbAddress.getDirId();
		this.tenantId = dbAddress.getTenantId();

		this.tenantName = tenantName;
		this.type = dbAddress.getType();
		setDbSubType(this.jdbcURL);
	}
	
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
	private void setDbSubType(String jdbcURL) {
		if (this.jdbcURL.startsWith("jdbc:db2"))
			dbSubType = "DB2";
        else if (this.jdbcURL.startsWith("jdbc:gbase"))
        	dbSubType = "GBase 8a";
        else if (this.jdbcURL.startsWith("jdbc:vertica"))
        	dbSubType = "Vertica";
        else if (jdbcURL.startsWith("jdbc:oracle"))
        	dbSubType = "Oracle";
        else if (jdbcURL.startsWith("jdbc:postgresql"))
        	dbSubType = "PostgreSQL";
        else if (jdbcURL.startsWith("jdbc:hive2"))
        	dbSubType = "Hive";
        else if (jdbcURL.startsWith("jdbc:sap"))
        	dbSubType = "Hana";
        else if (jdbcURL.startsWith("jdbc:teradata"))
        	dbSubType = "Teradata";
        else if (jdbcURL.startsWith("kpi:"))
        	dbSubType = "kpi";
        else
        	dbSubType = "Unknown";
	}
	
}
