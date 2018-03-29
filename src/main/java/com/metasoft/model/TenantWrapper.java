package com.metasoft.model;

import java.util.List;

import com.keymobile.dataSharingMgr.interfaces.Tenant;

public class TenantWrapper extends Tenant {

	public static final int ACL_PROVIDER = 2;
	public static final int ACL_CONSUMER = 1;
	public static final String TAG_PROVIDER = "provider";
	public static final String TAG_CONSUMER = "consumer";
	
	protected List<String> roles;
	
	public TenantWrapper() {}
	
	public TenantWrapper(Tenant tenant) {
		this.domainId = tenant.getDomainId();
	    this.dirId = tenant.getDirId();
	    this.tenantId = tenant.getTenantId();
	    
	    this.name = tenant.getName();
	    this.code = tenant.getCode();
	    this.state = tenant.getState();
	    this.desc = tenant.getDesc();
	    this.organization = tenant.getOrganization();
	    this.createDate = tenant.getCreateDate();
	    this.creator = tenant.getCreator();
	    this.modifyDate = tenant.getModifyDate();
	    this.modifier = tenant.getModifier();
	    this.acl = tenant.getACL();
	}
	
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
    	this.roles = roles;
    }
   
}
