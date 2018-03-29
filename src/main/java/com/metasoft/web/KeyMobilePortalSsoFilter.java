package com.metasoft.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.keymobile.portal.entity.User;
import com.metasoft.model.Constant;
import com.metasoft.service.FileService;
import com.metasoft.sso.client.web.SsoFilter;
import com.metasoft.util.LoginInfoUtil;

public class KeyMobilePortalSsoFilter extends SsoFilter {
	
	final private static Logger logger = LoggerFactory.getLogger(KeyMobilePortalSsoFilter.class);
	
	private DataSharingMgrService dataSharingMgrService;
	
	private FileService fileService;
	
	public KeyMobilePortalSsoFilter(DataSharingMgrService dataSharingMgrService, FileService fileService) {
		super();
		this.dataSharingMgrService = dataSharingMgrService;
		this.fileService = fileService;
	}

	@Override
	protected void doLogin(HttpServletRequest req, HttpServletResponse resp) {
	        HttpSession session = ((HttpServletRequest)req).getSession();
	       
	        Object logined = session.getAttribute(Constant.Session_UserId);
	        logger.info("userId = {" + logined + "}");
	        if(logined == null) {	            
	        	User portalUser = (User) session.getAttribute(Constant.Session_Keymobile_SSO);
	        	System.out.println(portalUser.getAppAccount());
	        	System.out.println(portalUser.getAppPsw());
	        	logger.info("Portal user is ["+portalUser.getUserId()+"]");
	        	logger.info("appAccount is ["+portalUser.getAppAccount()+"]");

	            ExtUser user = null;
	            try {
	            	user = authenticateUser(portalUser);
	            	logger.info("domainId["+user.getDomainId()+"]");
	            	Tenant tenant = dataSharingMgrService.getTenantById(user.getDomainId());
	            	LoginInfoUtil.setLoginInfoToSession(user, tenant, fileService.getBaseDir(), session);
	            } catch(DataSharingMgrError e) {
	            	logger.error("sso login failed.", e);
	            } catch (Exception e) {
	            	logger.error("sso login failed.", e);
	            }
	        }
	        
	        super.doLogin(req, resp);
		}
	
    private ExtUser authenticateUser(User portalUser) throws Exception {
    	if (portalUser.getAppAccount() == null) {
    		throw new Exception("appAccount is null.");
    	}
    	
    	if (portalUser.getAppAccount().split("\\.").length != 2) {
    		throw new Exception("appAccount format is error.");
    	}
    	String accountParts[] = portalUser.getAppAccount().split("\\."); 
    	String domainName = accountParts[1];
    	String userName = accountParts[0];
    	String userPwd = portalUser.getAppPsw();//未加密的
    	return dataSharingMgrService.authenticateUser(userName, userPwd , domainName);
    }
    
    @Override
    protected void doLogout(HttpServletRequest req, HttpServletResponse resp) {
    	
    	logger.debug("logout from sso....");
       
    	HttpSession session = ((HttpServletRequest) req).getSession();
    	LoginInfoUtil.removeLoginInfoFromSession(session);
    }
    
}
