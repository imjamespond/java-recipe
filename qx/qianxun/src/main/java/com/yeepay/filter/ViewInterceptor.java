package com.yeepay.filter;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ViewInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		ResourceBundle resb1 = ResourceBundle.getBundle("payapi");
		String password = resb1.getString("password");

		String s = (String) request.getSession().getAttribute("yj_wappay");
		if (s != null) {
			if (s.equals(password)) {
				System.out.println("可以查看");
				return true;
			}
		}
		System.out.println("不能查看");
		response.sendRedirect(request.getContextPath() + "/");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("===========HandlerInterceptor1 postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("===========HandlerInterceptor1 afterCompletion");
	}
}
