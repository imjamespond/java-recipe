package com.metasoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.service.DataSharingDBService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = "/services")
public class ServicesController {
	
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private DataSharingDBService dataSharingDBService;
	
	@RequestMapping("/queryDataSharingDB.load")
	public @ResponseBody List<List<String>> queryDataSharingDB(HttpServletRequest request, @RequestParam("userName") String userName,
			@RequestParam("password") String password, @RequestParam("sql") String sql) 
			throws Exception {
		HttpSession session = request.getSession();
		ExtUser user = validateUser(userName, password);
	
		return dataSharingDBService.executeQuery(sql, user.getName() + "." + PrivilegeCheckingHelper.getTenantName(session) , user.getPassword(), 20);		
	}
	
	@RequestMapping("/hasAccessPrivilege.load")
	public @ResponseBody boolean hasAccessPrivilege(HttpServletRequest request, @RequestParam("userName") String userName,
			@RequestParam("password") String password, @RequestParam("objectId") String objectId) throws Exception {
		ExtUser user = validateUser(userName, password);
		TableModel tableModel = locateObject(objectId);
		String domainId = dataSharingMgrService.getTenantById(tableModel.getTenantId()).getName();
		
		if (user.getACL() == 1) {
			if (user.getDomainId().equals(domainId)) {
				return true;
			} else {
				return dataSharingMgrService.getAllDomainPrivilege(user.getDomainId()).keySet().contains(tableModel.getTableModelId());
			}
		}
		else {
			return dataSharingMgrService.getPrivilegedTableModelIds(user.getUserId(), domainId).containsKey(tableModel.getTableModelId());
		}
	}
	
	@RequestMapping("/goto.load")
	public void submitAccessAppl(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("userName") String userName, @RequestParam("password") String password, 
				@RequestParam("dest") String dest) throws Exception {
		ExtUser user = validateUser(userName, password);
		//ExtDomain domain = dataSharingMgrService.getExtDomainByName(user.getDomainId());

		HttpSession session = (HttpSession) request.getSession();
		session.setAttribute("isSysadmin", user.isSysadmin());
		session.setAttribute("userId", user.getUserId());
		session.setAttribute("domainId", user.getDomainId());
		session.setAttribute("dirId", user.getDirId());
		//session.setAttribute("domainACL", domain.getACL());
		session.setAttribute("userACL", user.getACL());
		
		response.sendRedirect(dest);
	}
	
	private ExtUser validateUser(String userName, String password) throws Exception {
		List<ExtUser> users = dataSharingMgrService.getExtUserByName(userName);
		if (users.size() > 1) 
			throw new Exception("User with name " + userName + " is ambiguors.");
		if (users.size() == 0)
			throw new Exception("User with name " + userName + " does not exists.");
		
		ExtUser user = users.get(0); 
		if (!user.getPassword().equals(password)) {
			throw new Exception("Wrong password.");
		}
		
		return user;
	}
	
	private TableModel locateObject(String objectId) throws Exception {
		String [] objectIdParts = objectId.split("\\.");
		if (objectIdParts.length != 4)
			throw new Exception("ObjectId " + objectId + " is malformed.");
		
		String dbAddressName = objectIdParts[1];
		String tableModelName = objectIdParts[2] + "." + objectIdParts[3];
		List<Tenant> tenants = dataSharingMgrService.getAllTenants();
		for (Tenant tenant : tenants) {
			List<DBAddress> dbAddresses = dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId());
			for (DBAddress dbAddress : dbAddresses) {
				if (dbAddress.getName().equals(dbAddressName)) {
					List<TableModel> tableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddress.getDbAddressId());
					for (TableModel tableModel : tableModels) {
						if (tableModel.getName().equals(tableModelName)) {
							return tableModel;
						}
					}
				}
			}
		}
		
		throw new Exception("Object " + objectId + " does not exists.");
	}
	
}
