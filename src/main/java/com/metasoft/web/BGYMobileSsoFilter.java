package com.metasoft.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.keymobile.portal.entity.User;
import com.metasoft.model.Constant;
import com.metasoft.service.LogStatsService;
import com.metasoft.util.LoginInfoUtil;
import com.metasoft.util.PortalUserInfoUtil;
import com.metasoft.util.PortalUserInfoUtil.PortalUserExt;

import cn.com.landray.kk.token.TokenUtils;

public class BGYMobileSsoFilter extends HttpServlet implements Filter{
	
	private static Logger logger = LoggerFactory.getLogger(BGYMobileSsoFilter.class); 

	private static final long serialVersionUID = 1L; 
	private static Properties properties;
	
	private DataSharingMgrService dataSharingMgrService;
	
	private LogStatsService logStatsService;
	
	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		logger.info("doFilter...");
		HttpServletRequest request=(HttpServletRequest)req;     
        HttpServletResponse response  =(HttpServletResponse) resp;  
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute(Constant.Session_UserId);
        logger.info("userId = {" + userId + "}");
        if(userId == null){
        	Cookie[] cs = request.getCookies();
        	logger.info("cs = {" + cs + "}");
        	if(cs == null){
        		request.getRequestDispatcher("/jsp/mobile.jsp").forward(request, response);
            	return;
        	}
        	String tokenStr = null;
        	logger.info("cookie count :" + cs.length);
        	for(Cookie c : cs){
        		String name = c.getName();
        		logger.info("cookie name:"+name+" value:" + c.getValue());
        		if("LtpaToken".equalsIgnoreCase(name)){
        			tokenStr = c.getValue();
        			logger.info("LtpaToken:" + tokenStr);
        			break;
        		}
        	}
        	String portalUserId = "";
			try {
				if (tokenStr != null)
					portalUserId = TokenUtils.parseLtpaToken(tokenStr, getProValueByKey("key.yuanbao"));
			} catch (Exception e) {
				logger.error("parseLgpaToken error.", e);
			}
			if(!StringUtils.isEmpty(portalUserId)) {
				//获取用户信息.return Map<String, String> m ,Key值为areas 区域权限串, appAccount 应用账号, appPsw密码
				PortalUserExt portalUserInfo = PortalUserInfoUtil.getUserInfo(portalUserId);// 以后从session取
				String appAccount = portalUserInfo.getAppAccount();//userA.tenantA
				String appPsw = portalUserInfo.getAppPsw();//userA.tenantA
				String psw = portalUserInfo.getPsw();
				String areas = portalUserInfo.getAreas();
				
//				String appAccount = "adminA.A";//userA.tenantA
//				String appPsw = "pwd";//userA.tenantA
//				String psw = "";
//				String areas = "";
				
				logger.info("appAccount = {" + appAccount+ "}, appPsw = {" + appPsw + "}, psw = {"+psw+"}, areas={"+areas+"} ");
				ExtUser extUser = null;
				try {
					extUser = dataSharingMgrService.authenticateUser(appAccount.split("\\.")[0], appPsw, appAccount.split("\\.")[1]);
				 	Tenant tenant = dataSharingMgrService.getTenantById(extUser.getDomainId());
				 	setPortalUserToSession(portalUserId, portalUserInfo, session);
					LoginInfoUtil.setLoginInfoToSession(extUser, tenant, getProValueByKey("upload.baseDir"), session);
					logStatsService.indexMobileQueryState(extUser != null ? extUser.getTenantId() : "");
				} catch (DataSharingMgrError e) {
					logger.error("getExtUserById is error", e);
				}
				logger.info("portalUserId={"+portalUserId+"}, mshareUserId={"+extUser.getUserId()+"}");
			} else{
				logger.info("no userid");
				request.getRequestDispatcher("/jsp/mobile.jsp").forward(request, response);
            	return;
			}
        }
        chain.doFilter(req, resp);
        return;
    }

	private void setPortalUserToSession(String portalUserId, PortalUserExt portalUserExt, HttpSession session) {
		User user = new User();
		user.setAppAccount(portalUserExt.getAppAccount());
		user.setAppPsw(portalUserExt.getAppPsw());
		user.setUserId(portalUserId);
		user.setPassword(portalUserExt.getPsw());
		user.setDuties(portalUserExt.getAreas());
		session.setAttribute(Constant.Session_Keymobile_SSO, user);
	}
	
	static private String getProValueByKey(String key){
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(BGYMobileSsoFilter.class.getResourceAsStream("/application.properties"));
			} catch (IOException e) {
				logger.error("getYuanbaoKey error.", e);
			}
		}
		return properties.getProperty(key);
	}
	
    
    public void init(FilterConfig config) throws ServletException {  
    	ServletContext sc = config.getServletContext(); 
        XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
        logStatsService = cxt.getBean(LogStatsService.class);
        dataSharingMgrService = cxt.getBean(DataSharingMgrService.class);
    }
}
