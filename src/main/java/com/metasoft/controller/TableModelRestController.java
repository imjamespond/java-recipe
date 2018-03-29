package com.metasoft.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModelColumn;
import com.metasoft.service.DataSharingDBService;

@RestController
@RequestMapping("/KPIs")
public class TableModelRestController {
	
	private Logger logger = LoggerFactory.getLogger(TableModelRestController.class);

	public static final int PageSize = 10;
	
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private DataSharingDBService dataSharingDBService;
	@Autowired
	private HttpServletRequest request;
	
		
	class KPI {
		String id;
		String name;
		String remark;
		
		KPI(String id, String name, String remark) {
			this.id = id;
			this.name = name;
			this.remark = remark;
		}
	}
	
	class Field {
		String name;
		String dataType;
		String nullable;
		
		Field(String name, String dataType, String nullable) {
			this.name = name;
			this.dataType = dataType;
			this.nullable = nullable;
		}
	}
	
	class RestResultInfo {
		
		EsbInfo esbInfo;
		QueryInfo queryInfo;
		Object resultInfo;
	}
	
	class EsbInfo {
		String instId;
		String returnStatus;
		String returnCode;
		String returnMsg;
		String requestTime;
		String responseTime;
	}
	
	class QueryInfo {
		int totalRecord;
		int totalPage;
		int pageSize;
		int currentPage;
	}
	
	
	
	public enum ResponseStatusEnum {

		SUCCESS("A00001", "成功", "S"),
		
		ERROR("A00002","失败", "E"),
		
		WARN("A00003","警告", "W");
		
		String statusCode;
		
		String message;
	
		String status;
		
		ResponseStatusEnum(String statusCode,String message, String status){
			this.statusCode = statusCode;
			this.message = message;
			this.status = status;
		}
		
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public  RestResultInfo listAllKPIs(@RequestParam String instId) throws DataSharingMgrError {
		RestResultInfo restResultInfo = createRestReturnInfo(instId);
		try{
			checkTokenIsValid();
			List<KPI> KPIs = new ArrayList<>();
			for (Tenant tenant : dataSharingMgrService.getAllTenants()) {
				for (DBAddress dbAddress : dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId())) {
					for (TableModel tableModel : dataSharingMgrService.getTableModelsByDBAddress(dbAddress.getDbAddressId())) {
						if (tableModel.isEnabled() && tableModel.getType().equals("kpi"))
							KPIs.add(new KPI(tableModel.getTableModelId().split("=")[1], tableModel.getName(), tableModel.getRemarks()));
					}
				}
			}
			restResultInfo.resultInfo = KPIs;
		} catch (Exception e) {
			setResultError(restResultInfo, e);
		}
		restResultInfo.esbInfo.responseTime = getCurrnetFormatTime();
		return restResultInfo;
	}

	@RequestMapping(value="/{tableModelId}/fields", method = RequestMethod.GET)
	public  RestResultInfo getKPI(@PathVariable String tableModelId, @RequestParam String instId) throws DataSharingMgrError {
		RestResultInfo restResultInfo = createRestReturnInfo(instId);
		try {
			checkTokenIsValid();
			TableModel tableModel = dataSharingMgrService.getTableModelById("TableModel=" + tableModelId);
			List<Field> fields = new ArrayList<>();
			for (TableModelColumn column : tableModel.getAttachedColumns()) {
				fields.add(new Field(column.getName(), column.getSqlDataTypeName(), column.getNullable()));
			}
			restResultInfo.resultInfo = fields;
		} catch (Exception e) {
			setResultError(restResultInfo, e);
		}
		restResultInfo.esbInfo.responseTime = getCurrnetFormatTime();
		return restResultInfo;
	}
	
	@RequestMapping(value="/{tableModelId}/data/{currentPage}", method = RequestMethod.GET)
	public  RestResultInfo getKpiData(@PathVariable String tableModelId, @PathVariable int currentPage, @RequestParam String instId) throws DataSharingMgrError, SQLException {
		RestResultInfo restResultInfo = createRestReturnInfo(instId);
		try {
			checkTokenIsValid();
			TableModel tableModel = dataSharingMgrService.getTableModelById("TableModel=" + tableModelId);
			DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());
			Map<String, Object> map = dataSharingDBService.executeQueryByPagination(dbAddress.getName(), tableModel.getName(), "sysadmin.sysadmin", "k1e2y3@pwd", (currentPage - 1) * 10, PageSize);
			restResultInfo.resultInfo = map;
			int totalRecord = (Integer) map.get("total");
			setPageReturnInfo(restResultInfo, currentPage, totalRecord); 
		} catch (Exception e) {
			setResultError(restResultInfo, e);
		}
		restResultInfo.esbInfo.responseTime = getCurrnetFormatTime();
		return restResultInfo;
	}
	
	private void checkTokenIsValid() {
		String token = request.getHeader("esbkey");
		logger.debug("token = {"+token+"}");
		if (StringUtils.isEmpty(token)) {
			throw new RuntimeException("token can not be null");
		} else if (!token.equals(dataSharingDBService.getToken())) {
			throw new RuntimeException("token is invalid");
		}
	}

	private void setPageReturnInfo(RestResultInfo restResultInfo, int page, int totalRecord) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.totalRecord = totalRecord;
		queryInfo.totalPage = (totalRecord / PageSize) + 1;
		queryInfo.pageSize = PageSize;
		queryInfo.currentPage = page;
		restResultInfo.queryInfo = queryInfo;
	}
	
	private String getCurrnetFormatTime() {
		SimpleDateFormat f = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		return f.format(System.currentTimeMillis());
	}
	
	private void setResultError(RestResultInfo restResultInfo, Exception e) {
		restResultInfo.esbInfo.returnStatus = ResponseStatusEnum.ERROR.status;
		restResultInfo.esbInfo.returnCode = ResponseStatusEnum.ERROR.statusCode;
		restResultInfo.esbInfo.returnMsg = e.getMessage();
	}
	
	private RestResultInfo createRestReturnInfo(String instId) {
		RestResultInfo restResultInfo = new RestResultInfo();
		EsbInfo esbInfo = new EsbInfo();
		esbInfo.instId = instId;
		esbInfo.requestTime = getCurrnetFormatTime();
		esbInfo.returnStatus = ResponseStatusEnum.SUCCESS.status;
		esbInfo.returnCode = ResponseStatusEnum.SUCCESS.statusCode;
		esbInfo.returnMsg = ResponseStatusEnum.SUCCESS.message;
		restResultInfo.esbInfo = esbInfo;
		return restResultInfo;
	}
}
