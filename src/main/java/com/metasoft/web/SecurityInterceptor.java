package com.metasoft.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.metasoft.model.Constant;
import com.metasoft.util.PrivilegeCheckingHelper;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private List<Pattern> freeList = new ArrayList<Pattern>();

	/**
	 * provided for mvc configuration
	 * @param freeList
	 */
	public void setFreeList(List<String> freeList) {
		for (String url : freeList) {
			this.freeList.add(Pattern.compile(url));
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = urlPathHelper.getLookupPathForRequest(request);
		
		for (Pattern pattern : freeList) {
			if (pattern.matcher(url).find()) {
				return true;
			}
		}

		int roles = PrivilegeCheckingHelper.getRoleInt(request.getSession());		
		if (roles == 0) {
			if(request.getMethod().equals(RequestMethod.POST.name())||
					url.indexOf(".load")>0){
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain;charset=UTF-8");//"text/html"将会被sitemesh过滤
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.getWriter().write("session.expired");
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
			}    
            return false;
		}
		request.setAttribute(Constant.Session_Roles, roles);
		request.setAttribute(Constant.Session_UserId, PrivilegeCheckingHelper.getUserId(request.getSession()));
		request.setAttribute(Constant.Session_UserName, PrivilegeCheckingHelper.getUserName(request.getSession()));
		request.setAttribute(Constant.Session_TenantId, PrivilegeCheckingHelper.getTenantId(request.getSession()));
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//sessionService.unbind();
	}

	public String getSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constant.COOKIE_NAME)) {
					String value = cookie.getValue();
					if (!value.isEmpty()) {
						return value;
					}
					return null;
				}
			}
		}
		return null;
	}
}
