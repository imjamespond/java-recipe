package com.qianxun.web;

import org.copycat.framework.FrameworkFilter;

public class ApplicationFilter extends FrameworkFilter {

	@Override
	protected boolean isStaticResources(String url) {
		return pathMatcher.match("/favicon.ico", url)
				|| pathMatcher.match("/images/**", url)
				|| pathMatcher.match("/uploads/**", url)
				|| pathMatcher.match("/plugins/**", url)
				|| pathMatcher.match("/styles/**", url)
				|| pathMatcher.match("/scripts/**", url)
				|| pathMatcher.match("/htmls/**", url);
	}
}
