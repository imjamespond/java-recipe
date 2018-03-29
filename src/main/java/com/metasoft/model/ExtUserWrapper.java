package com.metasoft.model;

import java.util.List;

import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;

public class ExtUserWrapper extends ExtUser {
	
	public static final int ACL_ADMIN = 1;
	public static final int ACL_USER = 0;
	public static final String TAG_ADMIN = "tenantAdmin";
	public static final String TAG_USER = "tenantUser";
	
	protected List<String> roles;
	
	public ExtUserWrapper() {}
	
	public ExtUserWrapper(ExtUser user) {
		this.userId = user.getUserId();
	    this.name = user.getName();
	    this.password = user.getPassword();
	    this.userAccount = user.getUserAccount();
	    this.state = user.getState();
	    this.tel = user.getTel();
	    this.email = user.getEmail();
	    this.createDate = user.getCreateDate();
	    this.creator = user.getCreator();
	    this.modifyDate = user.getModifyDate();
	    this.modifier = user.getModifier();
	    this.desc = user.getDesc();
	    this.domainId = user.getDomainId();
	    this.tenantId = user.getTenantId();
	    this.dirId = user.getDirId();
	    this.isSysadmin = user.isSysadmin();
	    this.acl = user.getACL();
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
