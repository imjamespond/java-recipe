package com.metasoft.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metasoft.model.Constant;
import com.metasoft.service.LocalizationService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
public class IndexController {
	@Autowired
	LocalizationService ls;
	
	@RequestMapping(value = {"/", "/home/**", "/manage/**", "/signin", "/doc-upload*"})
	public String login(HttpServletRequest request, Model model) {
		model.addAttribute("frontent_version", ls.frontentVersion);
		return "index";
	}
	
	@RequestMapping(value = {"/mobile/**"})
	public String loginMobile(HttpServletRequest request, Model model) {
		model.addAttribute("frontent_version", ls.frontentVersion);
		return "mobile";
	}
	
	@RequestMapping(value = "/management/session", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> loginVerify(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = (HttpSession) request.getSession();
		
		Map<String,Object> rs = new HashMap<String,Object>();
		int roles = PrivilegeCheckingHelper.getRoleInt(session);		
		rs.put(Constant.Session_Roles, roles);
		rs.put(Constant.Session_UserId, PrivilegeCheckingHelper.getUserId(session));
		rs.put(Constant.Session_UserName, PrivilegeCheckingHelper.getUserName(session));
		rs.put(Constant.Session_TenantId, PrivilegeCheckingHelper.getTenantId(session));
		rs.put("is_dev", ls.isDev);
		return rs;
	}
}