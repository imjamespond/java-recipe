package com.test.qianxun.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

public class ApplicationFilter implements Filter {
	protected PathMatcher pathMatcher = new AntPathMatcher();
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	private ServletContext servletContext;
	private String encoding = "UTF-8";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String url = urlPathHelper.getLookupPathForRequest(request);
		if (!isStaticResources(url)) {
			String contextPath = request.getContextPath();
			String requestURL = request.getRequestURL().toString();
			request.setAttribute("contextPath", contextPath);
			request.setAttribute("requestURL", requestURL);
			request.setCharacterEncoding(encoding);
			servletContext.getRequestDispatcher("/app" + url).forward(request,
					servletResponse);
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	protected boolean isStaticResources(String url) {
		return pathMatcher.match("/favicon.ico", url)
				|| pathMatcher.match("/images/**", url)
				|| pathMatcher.match("/css/**", url)
				|| pathMatcher.match("/js/**", url)
				|| pathMatcher.match("/fonts/**", url)
				|| pathMatcher.match("/upload/**", url)
				|| pathMatcher.match("/plugins/**", url)
				|| pathMatcher.match("/htmls/**", url)
				|| pathMatcher.match("/temp/**", url);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

}