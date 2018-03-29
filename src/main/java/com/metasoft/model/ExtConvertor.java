package com.metasoft.model;

import java.util.ArrayList;
import java.util.List;

import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.util.Commons;

public class ExtConvertor {
	
	public static Tenant cast(TenantWrapper tenantWrapper) {
		Tenant tenant = new Tenant();
		
		tenant.setDomainId(tenantWrapper.getDomainId());
		tenant.setDirId(tenantWrapper.getDirId());
		
		tenant.setName(tenantWrapper.getName());
		tenant.setCode(tenantWrapper.getCode());
		tenant.setState(tenantWrapper.getState());
		tenant.setDesc(tenantWrapper.getDesc());
		tenant.setOrganization(tenantWrapper.getOrganization());
		tenant.setCreateDate(tenantWrapper.getCreateDate());
		tenant.setCreator(tenantWrapper.getCreator());
		tenant.setModifyDate(tenantWrapper.getModifyDate());
		tenant.setModifier(tenantWrapper.getModifier());
	
		int acl = 0;
		if (tenantWrapper.getRoles() != null) {
			for (String role : tenantWrapper.getRoles()) {
				if (role.equals(TenantWrapper.TAG_PROVIDER)) 
					acl += TenantWrapper.ACL_PROVIDER;
				if (role.equals(TenantWrapper.TAG_CONSUMER))
					acl += TenantWrapper.ACL_CONSUMER;
			}
		}
		tenant.setACL(acl);
		
		return tenant;
	}
	
	public static TenantWrapper cast(Tenant tenant) {
		TenantWrapper tenantWrapper = new TenantWrapper(tenant);
		
		List<String> roles = new ArrayList<String>();
		if ((tenant.getACL() & TenantWrapper.ACL_PROVIDER) == TenantWrapper.ACL_PROVIDER) {
			roles.add(TenantWrapper.TAG_PROVIDER);
		}
		if ((tenant.getACL() & TenantWrapper.ACL_CONSUMER) == TenantWrapper.ACL_CONSUMER) {
			roles.add(TenantWrapper.TAG_CONSUMER);
		}
		tenantWrapper.setRoles(roles);
		
		return tenantWrapper;
	}
	
	public static ExtUser cast(ExtUserWrapper userDef) {
		ExtUser user = new ExtUser();
		
		user.setUserId(userDef.getUserId());
		user.setName(userDef.getName());
		user.setPassword(userDef.getPassword());
		user.setUserAccount(userDef.getUserAccount());
		user.setState(userDef.getState());
		user.setTel(userDef.getTel());
		user.setEmail(userDef.getEmail());
		user.setCreateDate(userDef.getCreateDate());
		user.setCreator(userDef.getCreator());
		user.setModifyDate(userDef.getModifyDate());
		user.setModifier(userDef.getModifier());
		user.setDesc(userDef.getDesc());
		user.setDomainId(userDef.getDomainId());
		user.setDirId(userDef.getDirId());
		user.setSysadmin(userDef.isSysadmin());
		
		int acl = 0;
		if (userDef.getRoles() != null) {
			for (String role : userDef.getRoles()) {
				if (role.equals(ExtUserWrapper.TAG_ADMIN)) 
					acl = ExtUserWrapper.ACL_ADMIN;
				if (role.equals(ExtUserWrapper.TAG_USER))
					acl = ExtUserWrapper.ACL_USER;
			}
		}
		user.setACL(acl);
		
		return user;
	}
	
	public static ExtUserWrapper cast(ExtUser user) {
		ExtUserWrapper userDef = new ExtUserWrapper();
		
		userDef.setUserId(user.getUserId());
		userDef.setName(user.getName());
		userDef.setPassword(user.getPassword());
		userDef.setUserAccount(user.getUserAccount());
		userDef.setState(user.getState());
		userDef.setTel(user.getTel());
		userDef.setEmail(user.getEmail());
		userDef.setCreateDate(user.getCreateDate());
		userDef.setCreator(user.getCreator());
		userDef.setModifyDate(user.getModifyDate());
		userDef.setModifier(user.getModifier());
		userDef.setDesc(user.getDesc());
		userDef.setDomainId(user.getDomainId());
		userDef.setDirId(user.getDirId());
		userDef.setSysadmin(user.isSysadmin());
		
		List<String> roles = new ArrayList<String>();
		if (user.getACL() == ExtUserWrapper.ACL_ADMIN)
			roles.add(ExtUserWrapper.TAG_ADMIN);
		if (user.getACL() == ExtUserWrapper.ACL_USER)
			roles.add(ExtUserWrapper.TAG_USER);
		
		userDef.setRoles(roles);
		
		return userDef;
	}
	
}
