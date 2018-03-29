package com.metasoft.model;

import java.io.File;

public class Constant {
	
	public static final int PAGE_SIZE = 20;
	public static final String COOKIE_NAME = "METASOFT";
	public static final String DOMAIN_NAME = "/data-center";
	public static final String SIGNIN_URL = DOMAIN_NAME+"/login";
	public static final String Version = "1.0";
	public static String JS_SRC = DOMAIN_NAME+"/js/dist";
	
	public static final String kCharset = "UTF-8";
	
	public static final String INI_DB2_TABLE_FILE = "sql"+File.separator+"db2.sql";
	public static final String INI_MYSQL_TABLE_FILE = "sql"+File.separator+"mysql.sql";
	
	//PrivilegeError  info
	public static final String SysAdmin_Or_TenAdmin_Required = "SysAdmin/TenantAdmin required";
	public static final String User_Or_TenAdmin_Required = "user/TenantAdmin required";
	public static final String TenAdmin_Required = "TenantAdmin required";
	public static final String SysAdmin_Required = "SysAdmin required";
	public static final String PortalUser_Required = "PortalUser required";
	
	//DataSharingError info
	public static final String DbAddress_Name_Duplicate = "DbAddress name duplicate";
	public static final String CatalogName_Null_Tip = "CatalogName null tip";
	public static final String CatalogName_Duplicate_Tip = "CatalogName duplicate tip";
	
	//MDServiceError info
	public static final String Jdbc_Connection_Error_Tip = "Jdbc Connection Error Tip";
	public static final String Jdbc_Authoriza_Error_Tip = "Jdbc Authoriza Error Tip";

	//cache name
	public static final String DB_MODELCATALOGS_CACHE = "dbaddress_modelcatalogs_cache";
	
	public static final String Session_Roles = "roles";
	
	public static final String Session_UserId = "userId";
	public static final String Session_UserName = "userName";
	public static final String Session_TenantId = "tenantId";
	public static final String Session_TenantName = "tenantName";
	public static final String Session_DomainACL = "domainACL";
	public static final String Session_UserACL = "userACL";
	
	public static final String Session_IsSysAdmin = "isSysadmin";
	public static final String Session_IsTenantAdmin = "isTenantAdmin";
	public static final String Session_IsProvider = "isProvider";
	public static final String Session_IsConsumer = "isConsumer";
	
	public static final String Session_TenantBaseDir = "tenantBaseDir";
	
	public static final String Session_Keymobile_SSO = "METASOFT_SSO_USER_INFO";
	
}
