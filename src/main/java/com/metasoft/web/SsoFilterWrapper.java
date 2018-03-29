package com.metasoft.web;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.metasoft.service.FileService;

public class SsoFilterWrapper implements Filter {
	
	private Filter filter;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	
	@Autowired
	private FileService fileService;
	
	private String className;
	
	@PostConstruct
	public void init()throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException  {
		Constructor<?> constr = Class.forName(className).getConstructor(DataSharingMgrService.class, FileService.class);	
		filter = (Filter)constr.newInstance(dataSharingMgrService, fileService);
	}
	
	public SsoFilterWrapper(String className) {
		this.className = className;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		filter.init(filterConfig);		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		filter.doFilter(request, response, chain);
	}

	@Override
	public void destroy() {
		filter.destroy();		
	}

}
