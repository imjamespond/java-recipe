package com.qianxun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.qianxun.model.Manager;
import com.qianxun.service.ManagerService;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	private List<Pattern> freeList = new ArrayList<Pattern>();
	private Map<String, List<Pattern>> resourceMap = new HashMap<String, List<Pattern>>();
	private ManagerService managerService;
	@Value("#{properties['image.url']}")
	private String imageURL;
	@Value("#{properties['temp.url']}")
	private String tempURL;

	public void setManagerService(ManagerService managerService) {
		this.managerService = managerService;
	}

	public void setFreeList(List<String> freeList) {
		for (String regex : freeList) {
			this.freeList.add(Pattern.compile(regex));
		}
	}

	public void setResourceMap(Map<String, List<String>> resourceMap) {
		Set<String> keySet = resourceMap.keySet();
		for (String key : keySet) {
			List<Pattern> patternList = new ArrayList<Pattern>();
			this.resourceMap.put(key, patternList);
			List<String> regexList = resourceMap.get(key);
			for (String regex : regexList) {
				patternList.add(Pattern.compile(regex));
			}
		}
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("imageURL", imageURL);
		request.setAttribute("tempURL", tempURL);
		String url = urlPathHelper.getLookupPathForRequest(request);
		int i = url.indexOf("/", 1);
		String index = null;
		if (url.equals("/")) {
			index = "index";
		} else if (i > 1) {
			index = url.substring(1, i);
		} else {
			index = url.substring(1);
		}
		request.setAttribute("index", index);

		for (Pattern pattern : freeList) {
			if (pattern.matcher(url).find()) {
				return true;
			}
		}

		String id = (String) request.getSession().getAttribute("id");
		if (id == null) {
			response.sendRedirect(request.getContextPath() + "/manager/login");
			return false;
		}

		long mid = Long.parseLong(id);
		Manager manager = this.managerService.get(mid);
		String roles = manager.getRoles();
		if (roles == null || roles.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/manager/login");
			return false;
		}

//		String[] array = roles.split(",");
//		for (String key : array) {
//			if (resourceMap.containsKey(key)) {
//				List<Pattern> patternList = resourceMap.get(key);
//				for (Pattern pattern : patternList) {
//					if (pattern.matcher(url).find()) {
//						return true;
//					}
//				}
//			}
//		}
		return true;

		//response.sendRedirect(request.getContextPath() + "/manager/login");
		//return false;
	}

}
