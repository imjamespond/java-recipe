package com.metasoft.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.metasoft.model.GenericDao;
import com.metasoft.model.dao.ApplicationObjectDao;
import com.metasoft.service.dao.ApplicationObjectDaoService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = { "/logic-request" })
public class LogicRequestController {
	
	@Autowired
	private ApplicationObjectDaoService aoDaoService;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	
	@RequestMapping(value = { "/add-tables.post" }, method = RequestMethod.POST)
	public @ResponseBody int addTables(HttpServletRequest request,
			@RequestParam(value="type", required=false)  String type,
			@RequestParam("objectIds") List<String> objectIds) 
					throws DataSharingMgrError, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		
		List<ApplicationObjectDao> applObjects = new ArrayList<ApplicationObjectDao>();
		for (String objectId : objectIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(objectId);
			ApplicationObjectDao applObject = new ApplicationObjectDao();
			applObject.setAppl_id("");
			applObject.setObj_id(tableModel.getTableModelId());
			applObject.setObj_name(tableModel.getName());
			switch (tableModel.getType()) {
				case "kpi" : applObject.setObj_type("指标");
					break;
				case "analyticalModel" : applObject.setObj_type("分析模型");
					break;
				case "db" : applObject.setObj_type("库表");
					break;
				default :
					applObject.setObj_type(tableModel.getType());
			}
			applObject.setUser_id(userId);
			applObject.setDomain_id(tableModel.getTenantId());
			applObject.setRemark("");
			
			applObjects.add(applObject);
		}
		
		aoDaoService.batchReplaceInto(applObjects);
		return applObjects.size();
	}
	
	@RequestMapping("/list.load")
	public @ResponseBody List<GenericDao> listLogicRequests(HttpServletRequest request) throws DataSharingMgrError {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		Object[] objs = {userId};
		List<GenericDao> daos = aoDaoService.select(" where user_id=? and appl_id=''", objs);
		for (GenericDao dao : daos) {
			ApplicationObjectDao application = (ApplicationObjectDao) dao;
			
			String objId = application.getObj_id();
			TableModel tableModel = dataSharingMgrService.getTableModelById(objId);
			application.setColumns(tableModel.getAttachedColumns());
		}
		
		return daos;
	}
	
	@RequestMapping("/listby-applid.load")
	public @ResponseBody Map<String,GenericDao> listLogicRequestsByApplId(HttpServletRequest request,
			@RequestParam("appl_id") String applId) throws DataSharingMgrError {
		List<GenericDao> daos = aoDaoService.selectByApplId(applId);
		Map<String,GenericDao> map = new HashMap<String, GenericDao>();
		for (GenericDao dao : daos) {
			ApplicationObjectDao aoDao = (ApplicationObjectDao) dao;
			aoDao.setObj_name( dataSharingMgrService.getTableModelNameById(aoDao.getObj_id()));
			map.put(aoDao.getId(), aoDao);
		}
		return map;
	}
	
	@RequestMapping(value = { "/del-reqs" }, method = RequestMethod.POST)
	public @ResponseBody String delete(String appl_id,
			@RequestParam(value="appl_reqs",required=true) String applReqs) throws Exception{
		aoDaoService.batchDelete(applReqs);
		return "ok";
	}
}