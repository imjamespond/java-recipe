package com.metasoft.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.metasoft.model.GenericDao;
import com.metasoft.model.PrivilegeError;
import com.metasoft.model.dao.ApplicationProcedureDao;
import com.metasoft.model.dao.ApplicationProcedureDao.State;
import com.metasoft.util.Commons;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = { "/application-manage" })
public class ApplicationProcedureController {
	
	@Autowired
	private ApplFlowService applFlowService;
	
	@RequestMapping(value = { "/procedure.load" }, method = RequestMethod.GET)
	public @ResponseBody List<GenericDao> getProcedure(HttpServletRequest request,
			@RequestParam(value="appl_id",required=true) String appl_id) throws DataSharingMgrError, NumberFormatException, ApplFlowError {
		List<GenericDao> procedureDaos = new ArrayList<>();
		String tenantId = PrivilegeCheckingHelper.getTenantId(request.getSession());
		
		Appl appl = applFlowService.getApplById(Integer.parseInt(appl_id));
		List<Appr> apprs = applFlowService.getOnGoingApprByAppl(appl.getApplId());
		if (appl.getState().equals("tenantAdminProcess")) {
			if (!appl.getApplierId().startsWith("domain=")) { 
				ApplicationProcedureDao procedureDao0 = new ApplicationProcedureDao();
				procedureDao0.setAppl_id(Integer.toString(appl.getApplId()));
				procedureDao0.setProc_state(State.TOPROCESS.name);
				procedureDao0.setProc_token(1);
				procedureDaos.add(procedureDao0);
			}
			
			ApplicationProcedureDao procedureDao1 = new ApplicationProcedureDao();
			procedureDao1.setAppl_id(Integer.toString(appl.getApplId()));
			procedureDao1.setProc_state(State.CROSS_DOMAIN.name);
			procedureDao1.setProc_token(1);
			for (Appr appr : apprs) {
				if (appr.getApproverId().equals(tenantId))
					procedureDao1.setProc_token(0);
			}
			procedureDaos.add(procedureDao1);
		} else if (appl.getState().equals("adminProcess")) {
			ApplicationProcedureDao procedureDao = new ApplicationProcedureDao();
			procedureDao.setAppl_id(Integer.toString(appl.getApplId()));
			procedureDao.setProc_state(State.TOPROCESS.name);
			procedureDao.setProc_token(0);
			procedureDaos.add(procedureDao);
		} else if (appl.getState().equals("end")) {
			ApplicationProcedureDao procedureDao = new ApplicationProcedureDao();
			procedureDao.setAppl_id(Integer.toString(appl.getApplId()));
			procedureDao.setProc_state(State.CROSS_DOMAIN.name);
			procedureDao.setProc_token(1);
			procedureDaos.add(procedureDao);
		}
		
		return procedureDaos;
	}

	@RequestMapping(value = { "/audit" }, method = RequestMethod.POST)
	public @ResponseBody String approve(HttpServletRequest request,
			@RequestParam(value="appl_id",required=true) String appl_id,
			@RequestParam(value="opinion",required=true) String opinion,
			@RequestParam(value="proc_state",required=true) String state,
			@RequestParam(value="proc_token",required=true) int token,
			@RequestParam(value="approved_obj_map",required=true) String approved_objs) 
	throws PrivilegeError, DataSharingMgrError, IllegalArgumentException, IllegalAccessException, 
		InvocationTargetException, NoSuchMethodException, ApplFlowError {
		if (!PrivilegeCheckingHelper.isTenantAdmin(request.getSession())) 
			throw new PrivilegeError("TenantAdmin required.");
		
		List<Appr> apprs = applFlowService.listOnGoingAppr(PrivilegeCheckingHelper.getUserId(request.getSession()));
		for (Appr appr : apprs) {
			if (appr.getApplId() == Integer.parseInt(appl_id)) {
				if (token == 2)
					applFlowService.deny(appr.getApprId(), PrivilegeCheckingHelper.getUserId(request.getSession()), opinion);
				else
					applFlowService.approve(appr.getApprId(), PrivilegeCheckingHelper.getUserId(request.getSession()), 
							Commons.FromJson(approved_objs), opinion);
			}
		}
		
		return "ok";
	}
	
}