package com.metasoft.util;

import java.io.File;

import javax.servlet.http.HttpSession;

import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.model.Constant;
import com.metasoft.model.ExtUserWrapper;
import com.metasoft.model.TenantWrapper;

/**
 * record login info
 *
 */
public class LoginInfoUtil {

	public static void setLoginInfoToSession(ExtUser user, Tenant tenant, String baseDir,
			HttpSession session) {
		session.setAttribute(Constant.Session_UserId, user.getUserId());
		session.setAttribute(Constant.Session_UserName, user.getName());
		session.setAttribute(Constant.Session_TenantId, tenant.getTenantId());
		session.setAttribute(Constant.Session_TenantName, tenant.getName());
		session.setAttribute(Constant.Session_DomainACL, tenant.getACL());
		session.setAttribute(Constant.Session_UserACL, user.getACL());

		session.setAttribute(Constant.Session_IsSysAdmin, user.isSysadmin());
		session.setAttribute(Constant.Session_IsTenantAdmin, isTenantAdmin(user.getACL()));
		session.setAttribute(Constant.Session_IsProvider, isProvider(tenant.getACL()));
		session.setAttribute(Constant.Session_IsConsumer, isConsumer(tenant.getACL()));
		
		session.setAttribute(Constant.Session_TenantBaseDir, baseDir + File.separator + tenant.getName());
	}

	private static boolean isTenantAdmin(Integer userACL) {
		if (userACL == null)
			return false;
		if (((Integer) userACL) == ExtUserWrapper.ACL_ADMIN)
			return true;
		else
			return false;
	}

	private static boolean isProvider(Integer domainACL) {
		if (domainACL == null)
			return false;
		if (((Integer) domainACL & TenantWrapper.ACL_PROVIDER) == TenantWrapper.ACL_PROVIDER)
			return true;
		else
			return false;
	}

	private static boolean isConsumer(Integer domainACL) {
		if (domainACL == null)
			return false;
		if (((Integer) domainACL & TenantWrapper.ACL_CONSUMER) == TenantWrapper.ACL_CONSUMER)
			return true;
		else
			return false;
	}

	public static void removeLoginInfoFromSession(HttpSession session) {
		session.setAttribute(Constant.Session_UserId, null);
		session.setAttribute(Constant.Session_UserName, null);
		session.setAttribute(Constant.Session_TenantId, null);
		session.setAttribute(Constant.Session_TenantName, null);
		session.setAttribute(Constant.Session_DomainACL, null);
		session.setAttribute(Constant.Session_UserACL, null);

		session.setAttribute(Constant.Session_IsSysAdmin, null);
		session.setAttribute(Constant.Session_IsTenantAdmin, null);
		session.setAttribute(Constant.Session_IsProvider, null);
		session.setAttribute(Constant.Session_IsConsumer, null);
	}
}
