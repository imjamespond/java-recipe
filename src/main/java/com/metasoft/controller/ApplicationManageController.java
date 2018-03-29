package com.metasoft.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.WorkFlow.interfaces.Appl;
import com.keymobile.WorkFlow.interfaces.ApplFlowError;
import com.keymobile.WorkFlow.interfaces.ApplFlowService;
import com.keymobile.WorkFlow.interfaces.Appr;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.model.dao.ApplicationDao;
import com.metasoft.model.dao.ApplicationObjectDao;
import com.metasoft.service.dao.ApplicationDaoService;
import com.metasoft.service.dao.ApplicationObjectDaoService;
import com.metasoft.util.Attr;
import com.metasoft.util.Commons;
import com.metasoft.util.DbUtil;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = { "/application-manage" })
public class ApplicationManageController {
	
	@Autowired
	private ApplicationDaoService aaDaoService;
	@Autowired
	private ApplicationObjectDaoService aoDaoService;
	@Autowired
	private ApplFlowService applFlowService;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	      
	@RequestMapping(value = { "/my-appliction.load" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getMyApplicationByState(HttpServletRequest request,
			@RequestParam(value="state",required=true) String state, Page page) throws DataSharingMgrError, ApplFlowError {
		List<Appl> appls = null;
		if (state.equals("to-apply")) {
			appls = applFlowService.listSavedAppls(PrivilegeCheckingHelper.getUserId(request.getSession()));
		} else if (state.equals("apply")) {
			appls = applFlowService.listOnGoingAppls(PrivilegeCheckingHelper.getUserId(request.getSession()));
			appls.addAll(applFlowService.listEndedAppls(PrivilegeCheckingHelper.getUserId(request.getSession())));
		} else {
			appls = new ArrayList<>();
		}
		List<ApplicationDao> applDaos = new ArrayList<>();
		for (Appl appl : appls) {
			ApplicationDao applDao = new ApplicationDao();
			applDao.setAppl_id(Integer.toString(appl.getApplId()));
			if (appl.getStartTime() != null)
				applDao.setCreate_date(appl.getStartTime());
			if (appl.getEndTime() != null)
				applDao.setFinish_date(appl.getEndTime());
			applDao.setTitle(appl.getTitle());
			applDao.setReason(appl.getReason());
			if (appl.getApplierId().startsWith("domain=")) {
				Tenant tenant = dataSharingMgrService.getTenantById(appl.getApplierId());
				applDao.setDomain_id(tenant.getTenantId());
				applDao.domain = tenant.getName();
			} else {
				ExtUser user = dataSharingMgrService.getExtUserById(appl.getApplierId());
				Tenant tenant = dataSharingMgrService.getTenantById(user.getTenantId());
				applDao.setUser_id(user.getUserId());
				applDao.username = user.getName();
				applDao.setDomain_id(user.getDomainId());
				applDao.domain = tenant.getName();
			}	
			applDao.applType = ApplicationDao.Type.APPLYDATA.name;
			for (String objectId : appl.getApplObjects().keySet()) {
				ApplicationObjectDao applObject = new ApplicationObjectDao();
				TableModel tableModel = dataSharingMgrService.getTableModelById(objectId);
				if (tableModel == null)
					continue;
				applObject.setObj_id(objectId);
				applObject.setObj_name(tableModel.getName());
				applObject.setObj_type(ApplicationObjectDao.Type.Table.name);
				applObject.setDomain_id(tableModel.getTenantId());
				applObject.setColumnsIndices(appl.getApplObjects().get(objectId));
				
				applDao.addApplicationObject(applObject);
			}
			
			applDaos.add(applDao);
		}
		Map<String, Object> map = new HashMap<String, Object>(); 
		List<Attr> attrs = DbUtil.GeAttrList(ApplicationDao.class);
		map.put("data", applDaos);
		map.put("attrs", attrs);
		map.put("page", page);
		
		return map;
	}
	
	@RequestMapping(value = { "/appliction.load" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getApplicationByState(HttpServletRequest request,
			@RequestParam(value="token",required=true) int token, Page page) throws DataSharingMgrError, ApplFlowError {
		List<Appr> apprs;
		if (token == 3) 
			apprs = applFlowService.listEndedAppr(PrivilegeCheckingHelper.getUserId(request.getSession()));
		else 
			apprs = applFlowService.listOnGoingAppr(PrivilegeCheckingHelper.getUserId(request.getSession()));
		Map<String, Object> map = new HashMap<String, Object>(); 
		List<Attr> attrs = DbUtil.GeAttrList(ApplicationDao.class);
		List<ApplicationDao> applDaos = new ArrayList<>();
		for (Appr appr : apprs) {
			Appl appl = applFlowService.getApplById(appr.getApplId());
			ApplicationDao applDao = new ApplicationDao();
			applDao.setAppl_id(Integer.toString(appl.getApplId()));
			applDao.setCreate_date(appl.getStartTime());
			if (appl.getEndTime() != null)
				applDao.setFinish_date(appl.getEndTime());
			applDao.setTitle(appl.getTitle());
			applDao.setReason(appl.getReason());
			if (appl.getApplierId().startsWith("domain=")) {
				Tenant tenant = dataSharingMgrService.getTenantById(appl.getApplierId());
				applDao.setDomain_id(tenant.getTenantId());
				applDao.domain = tenant.getName();
			} else {
				ExtUser user = dataSharingMgrService.getExtUserById(appl.getApplierId());
				Tenant tenant = dataSharingMgrService.getTenantById(user.getTenantId());
				applDao.setUser_id(user.getUserId());
				applDao.username = user.getName();
				applDao.setDomain_id(user.getDomainId());
				applDao.domain = tenant.getName();
			}	
			applDao.applType = ApplicationDao.Type.APPLYDATA.name;
			for (String objectId : appr.getApprObjects().keySet()) {
				ApplicationObjectDao applObject = new ApplicationObjectDao();
				TableModel tableModel = dataSharingMgrService.getTableModelById(objectId);
				if (tableModel == null)
					continue;
				applObject.setObj_id(objectId);
				applObject.setObj_name(tableModel.getName());
				applObject.setObj_type(ApplicationObjectDao.Type.Table.name);
				applObject.setDomain_id(tableModel.getTenantId());
				applObject.setColumnsIndices(appl.getApplObjects().get(objectId));
				
				applDao.addApplicationObject(applObject);
			}
		
			applDaos.add(applDao);
		}
		map.put("data", applDaos);
		map.put("attrs", attrs);
		map.put("page", page);
		
		return map;
	}
	
	@RequestMapping(value = { "/new" }, method = RequestMethod.POST)
	public @ResponseBody String apply(HttpServletRequest request, 
			ApplicationDao dao, @RequestParam(value="appl_reqs",required=true) String applObjsJson) throws Exception {
		HttpSession session = request.getSession();
		Appl appl = new Appl(PrivilegeCheckingHelper.getUserId(session), dao.getTitle(), dao.getReason());
		Map<String, List<Integer>> applObjects = Commons.FromJson(applObjsJson);
		for (String applObject : applObjects.keySet()) {
			appl.addApplObject(applObject, applObjects.get(applObject));
		}
		int savedApplId = applFlowService.saveAppl(appl, PrivilegeCheckingHelper.getUserId(session));
		for (String applObject : applObjects.keySet()) {
			aoDaoService.delete(applObject, PrivilegeCheckingHelper.getUserId(session));
		}
		if (dao.getAppl_state().equals(ApplicationDao.State.APPLY.name))
			applFlowService.submitAppl(savedApplId);
		
		return "ok";
	}
	
	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public @ResponseBody String applyUpdate(HttpServletRequest request, 
			ApplicationDao dao, @RequestParam(value="appl_reqs",required=true) String applReqs) throws Exception {
		if (dao.getAppl_state().equals("apply")) {
			applFlowService.submitAppl(Integer.parseInt(dao.getAppl_id()));
		} else { 
			aaDaoService.updateAppl(request, dao, applReqs);
		}
		return "ok";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	public @ResponseBody String delApplication(HttpServletRequest request, String appl_id) throws ApplFlowError {
		applFlowService.deleteSavedAppl(Integer.parseInt(appl_id), PrivilegeCheckingHelper.getUserId(request.getSession()));
		return "ok";
	}
	
	@RequestMapping(value = { "/del-appl-reqs" }, method = RequestMethod.POST)
	public @ResponseBody String delApplication(String appl_id,
			@RequestParam(value="appl_reqs",required=true) String applReqs) throws Exception{
		aoDaoService.batchUpdateMode(applReqs, "");
		return "ok";
	}
	
}