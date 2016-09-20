package com.tongyi.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	
	public static String getUserName(HttpServletRequest request){
		String name = null;
		Cookie[] cookies = request.getCookies();
		return name;
	}
	
	public static void setCookie(HttpServletRequest request,String key,String value){
		
	}
}
