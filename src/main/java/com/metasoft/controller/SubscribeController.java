package com.metasoft.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.portal.entity.User;
import com.metasoft.model.Constant;
import com.metasoft.model.exception.RestInvokeException;
import com.metasoft.service.dao.SubUserTableModelDaoService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = "/subScribe")
public class SubscribeController {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SubscribeController.class);
	
	@Autowired
	private SubUserTableModelDaoService subscribeService;
	
	@Autowired
	private DataSharingMgrService dataSharingService;
	
	
	/**
	 * 订阅模型
	 * @param request
	 * @param tableModelId 模型ID
	 * @return
	 * @throws DataSharingMgrError
	 * @throws RestInvokeException
	 */
	@RequestMapping(value = "/subscribeTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String subscribeTableModel(HttpServletRequest request, String tableModelId) throws DataSharingMgrError, RestInvokeException {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		TableModel tableModel = dataSharingService.getTableModelById(tableModelId);
		subscribeService.subscribeTableModel(userId, tableModel);
		return "ok";
	}
	
	/**取消订阅模型
	 * @param request
	 * @param tableModelId 模型ID
	 * @return
	 * @throws DataSharingMgrError
	 * @throws RestInvokeException
	 */
	@RequestMapping(value = "/unSubscribeTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String unSubscribeTableModel(HttpServletRequest request, String tableModelId) throws DataSharingMgrError, RestInvokeException {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		subscribeService.unSubscribeTableModel(userId, tableModelId);
		return "ok";
	}
	
	/**精选模型
	 * @param request
	 * @param tableModelId 模型ID
	 * @return
	 * @throws DataSharingMgrError
	 * @throws RestInvokeException
	 */
	@RequestMapping(value = "/selectSubscribeTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String selectSubscribeTableModel(HttpServletRequest request, String tableModelId) throws DataSharingMgrError, RestInvokeException {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		TableModel tableModel = dataSharingService.getTableModelById(tableModelId);
		subscribeService.selectSubscribeTableModel(userId, tableModel.getTableModelId());
		return "ok";
	}
	
	/**取消精选模型
	 * @param request
	 * @param tableModelId 模型ID
	 * @return
	 * @throws DataSharingMgrError
	 * @throws RestInvokeException
	 */
	@RequestMapping(value = "/unSelectSubscribeTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String unSelectSubscribeTableModel(HttpServletRequest request, String tableModelId) throws DataSharingMgrError, RestInvokeException {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		TableModel tableModel = dataSharingService.getTableModelById(tableModelId);
		subscribeService.unSelectSubscribeTableModel(userId, tableModel.getTableModelId());
		return "ok";
	}
	
	/**
	 * 获取指标集合与默认时间接口
	 * @param cycle 周期  1:日,2:月 3:周 4:季度 5:半年 6:年
	 * @return
	 * @throws DataSharingMgrError 
	 */
	@RequestMapping(value = "/indicatorsByCycle.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getIndicatorsByCycle(HttpServletRequest request, String cycle) throws DataSharingMgrError {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		return subscribeService.getIndicatorsByCycle(userId, cycle);
	}
	
	
	/**
	 * 指标详情表格数据接口
	 * 
	 * @return
	 * @throws RestInvokeException 
	 * @throws DataSharingMgrError 
	 */
	@RequestMapping(value = "/detail.load", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getIndicatorDetailData(HttpServletRequest request, 
			@RequestParam(required = true, value = "kpiPath") String kpiPath,
			@RequestParam(required = true, value = "date") String date,
			@RequestParam(required = true, value = "cycle") String cycle) throws RestInvokeException, DataSharingMgrError {
		User portalUser = (User) request.getSession().getAttribute(Constant.Session_Keymobile_SSO);
		String areas = "";
		if (portalUser != null && portalUser.getDuties() != null) areas = portalUser.getDuties();
		logger.debug("kpiPath= {"+ kpiPath + "}, date= {"+ date+"}, cycle={"+cycle+"}, areas={"+areas+"}");
		return subscribeService.getIndicatorDetailData(kpiPath, date, cycle, areas);
	}
	
	
	/**
	 * 获取默认的日期
	 * @param request
	 * @param kpiPath
	 * @param cycle
	 * @return
	 * @throws DataSharingMgrError
	 * @throws RestInvokeException
	 */
	@RequestMapping(value = "/indicatorDefaultDate.load", method = RequestMethod.GET)
	public @ResponseBody String getIndicatorDefaultDate(HttpServletRequest request, String kpiPath, String cycle) throws DataSharingMgrError, RestInvokeException {
		logger.debug("kpiPath= {"+ kpiPath + "}, cycle={"+cycle+"} ");
		return subscribeService.getIndicatorDefaultDate(kpiPath, cycle);
	}
	
	
	@RequestMapping(value = "/distinbydate.load", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getDistinctDate(HttpServletRequest request, String parentName, String kpiPath, String cycle, int level) throws RestInvokeException, DataSharingMgrError {
		logger.debug("kpiPath= {"+ kpiPath + "}, parentName= {"+ parentName+"}, level={"+level+"}, cycle={"+cycle+"}");
		return subscribeService.getDistinctDate(kpiPath, parentName, cycle, level);
	}
	
	
}
