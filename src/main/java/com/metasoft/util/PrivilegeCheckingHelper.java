package com.metasoft.util;

import javax.servlet.http.HttpSession;

import com.metasoft.model.Constant;

public class PrivilegeCheckingHelper {

	public static Boolean isSysAdmin(HttpSession session) {
		return (Boolean) session.getAttribute(Constant.Session_IsSysAdmin);
	}

	public static Boolean isTenantAdmin(HttpSession session) {
		return (Boolean) session.getAttribute(Constant.Session_IsTenantAdmin);
	}
	
	public static Boolean isProvider(HttpSession session) {
		return (Boolean) session.getAttribute(Constant.Session_IsProvider);
	}
	
	public static Boolean isConsumer(HttpSession session) {
		return (Boolean) session.getAttribute(Constant.Session_IsConsumer);
	}
	          
	public static String getUserId(HttpSession session) {
		return (String) session.getAttribute(Constant.Session_UserId);
	}
	
	public static String getUserName(HttpSession session) {
		return (String) session.getAttribute(Constant.Session_UserName);
	}

	public static String getTenantId(HttpSession session) {
		return (String) session.getAttribute(Constant.Session_TenantId);
	}

	public static String getTenantName(HttpSession session) {
		return (String) session.getAttribute(Constant.Session_TenantName);
	}
	
	public static String getTenantBaseDir(HttpSession session) {
		return (String) session.getAttribute(Constant.Session_TenantBaseDir);
	}

	public static int getRoleInt(HttpSession session) {
		int result = 0;
		
		if (isSysAdmin(session) != null && isSysAdmin(session)) {
			result = 1;
		}
		else {
			if (isTenantAdmin(session) != null && isTenantAdmin(session)) {
				result |= 2;
			}
			if (isProvider(session) != null && isProvider(session)) {
				result |= 4;
			}		
			if (isConsumer(session) != null && isConsumer(session)) {
				result |= 8;
			}
		}
		return result;
	}
}
