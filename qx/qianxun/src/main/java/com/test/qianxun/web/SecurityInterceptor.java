package com.test.qianxun.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.test.qianxun.service.SessionService;
import com.test.qianxun.model.Session;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private SessionService sessionService;
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private List<Pattern> freeList = new ArrayList<Pattern>();
	private String cookieName = "qxgame";
	@Value("#{properties['image.url']}")
	private String imageURL;
	@Value("#{properties['temp.url']}")
	private String tempURL;

	public void setFreeList(List<String> freeList) {
		for (String url : freeList) {
			this.freeList.add(Pattern.compile(url));
		}
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("imageURL", imageURL);
		request.setAttribute("tempURL", tempURL);
		String url = urlPathHelper.getLookupPathForRequest(request);
		String sessionId = getSessionId(request);
		Session session = null;
		if (sessionId != null) {
			session = sessionService.get(sessionId);
			if (session != null) {
				sessionService.bind(sessionId);
				request.setAttribute("session", session);
			}
		}

		for (Pattern pattern : freeList) {
			if (pattern.matcher(url).find()) {
				return true;
			}
		}

		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/info/login?referer=" + url);
			return false;
		}

		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		sessionService.unbind();
	}

	private String getSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
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
