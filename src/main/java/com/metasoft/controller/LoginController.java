package com.metasoft.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.model.Constant;
import com.metasoft.service.FileService;
import com.metasoft.util.LoginInfoUtil;

@Controller
@RequestMapping(value = "/")
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private FileService fileService;

	@RequestMapping("login")
	public String login(HttpServletRequest request, Model model, String referer) {
		if(null==referer)
			referer=Constant.DOMAIN_NAME+"/manage/home";
		if(referer.indexOf("/")!=0)
			referer="/"+referer;//ie url bug?
		model.addAttribute("referer", referer);
		return "login";
	}

	@RequestMapping("login.logout")
	public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = (HttpSession) request.getSession();
		LoginInfoUtil.removeLoginInfoFromSession(session);
		return "ok";
	}
	
	@RequestMapping(value = "login.post", method = RequestMethod.POST)
	public @ResponseBody String loginPost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("username") String userName,
			@RequestParam("password") String password,
			@RequestParam("tenant") String tenantName) {
		try {
			ExtUser user = dataSharingMgrService.authenticateUser(userName, password, tenantName);
			Tenant tenant = dataSharingMgrService.getTenantByName(tenantName);

			HttpSession session = (HttpSession) request.getSession();
			LoginInfoUtil.setLoginInfoToSession(user, tenant, fileService.getBaseDir(), session);
		} catch (DataSharingMgrError e) {
			logger.error(e.getMessage());
			return "false";
		}
		return "ok";
	}
	
	@RequestMapping(value = "login-test", method = RequestMethod.GET)
	public @ResponseBody String loginTest(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="tenant", required=true, defaultValue="foobar") String tenant,
			@RequestParam(value="user", required=true, defaultValue="foobar") String user,
			@RequestParam(value="passwd", required=true, defaultValue="pwd") String passwd){
		this.loginPost(request, response, user, DigestUtils.sha1Hex(passwd), tenant);
		return "ok";
	}

	public static Cookie GetCookie(String sid, int expiry) {
		Cookie cookie = new Cookie(Constant.COOKIE_NAME, sid);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setMaxAge(expiry);
		return cookie;
	}

}