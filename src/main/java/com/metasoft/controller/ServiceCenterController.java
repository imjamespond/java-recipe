package com.metasoft.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.ModelCatalog;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.keymobile.metadataServices.interfaces.Column;
import com.keymobile.metadataServices.interfaces.MDService;
import com.keymobile.metadataServices.interfaces.MDServiceError;
import com.keymobile.metadataServices.interfaces.Table;
import com.metasoft.model.ExtTableModel;
import com.metasoft.service.DataSharingDBService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = "/service-center")
public class ServiceCenterController {
	
	public static final String kRoot = "数据共享服务中心";
	
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private DataSharingDBService dataSharingDBService;
	@Autowired
	private MDService mdService;
	          	
	/**
	 * return logical models belonging to given catalog  
	 */
	@RequestMapping(value = "/listTableModelsByCatalog.load")
	public @ResponseBody Map<String,Object> listTableModelsByCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId) 
			throws DataSharingMgrError {
		Map<String, Object> data = new HashMap<String, Object>();
		List<ExtTableModel> extTableModels = new ArrayList<ExtTableModel>();
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		String userDomainId = PrivilegeCheckingHelper.getTenantId(request.getSession());
		ModelCatalog modelCatalog = null;
		String otherCatalogId = "";
		// 增加other目录的获取逻辑
		if (!StringUtils.isEmpty(catalogId) && catalogId.trim().startsWith("other")) {
			otherCatalogId = catalogId;
			catalogId = catalogId.substring(catalogId.indexOf("=")+1,catalogId.length());
		}
		modelCatalog = dataSharingMgrService.getModelCatalogById(catalogId);
		boolean isSysAdmin = false;
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession())) 
			isSysAdmin = true;
		boolean isSelfDomain = false;
		if (modelCatalog.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(request.getSession())) 
				&& PrivilegeCheckingHelper.isTenantAdmin(request.getSession()))
			isSelfDomain = true;
		
		Set<String> privileged = new TreeSet<String>();
		Set<String> userPrivileges = dataSharingMgrService.getPrivilegedTableModelIds(userId, userDomainId).keySet();
		if (userPrivileges != null && userPrivileges.size() > 0)
			privileged.addAll(userPrivileges);
		if (!isSelfDomain && PrivilegeCheckingHelper.isTenantAdmin(request.getSession())) {
			Set<String> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(userDomainId).keySet();
			if (domainPrivileges != null && domainPrivileges.size() > 0)
				privileged.addAll(domainPrivileges);
		}
		List<TableModel> allTableModels = null;
		// 增加other目录的获取逻辑
		if (StringUtils.isEmpty(otherCatalogId)) 
			allTableModels = dataSharingMgrService.getTableModelsWithoutColumnsByCatalog(catalogId);
		else 
			allTableModels = getNotRelateModels(otherCatalogId);
		
		int accessNum = 0;
		for (TableModel tableModel : allTableModels) {
			boolean isPrivileged = false;
			if (isSysAdmin || isSelfDomain || privileged.contains(tableModel.getTableModelId())) {
				accessNum++;
				isPrivileged = true;
			}
			extTableModels.add(new ExtTableModel(tableModel, isPrivileged));
		}
		data.put("belongNum", extTableModels.size());
		data.put("accessNum", accessNum);
		data.put("tableModels",extTableModels);
		return data;
	}
	
	private List<TableModel> getNotRelateModels(String otherCatalogId) throws DataSharingMgrError {
		String dbAddressId = otherCatalogId.substring(otherCatalogId.indexOf("=")+1,otherCatalogId.length());
		List<ModelCatalog> catalogs = dataSharingMgrService.getModelCatalog(dbAddressId);
				
		List<String> catalogIds = new ArrayList<String>();
		for (ModelCatalog catalog : catalogs) {
			catalogIds.add(catalog.getModelCatalogId());
		}
		List<TableModel> hasRelateModels = dataSharingMgrService.getTableModelsWithoutColumnsByCatalogs(catalogIds);
		List<TableModel> tableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddressId);
		List<String> hasRelateModelIds = getHasRelateTableModelIds(hasRelateModels);
		List<TableModel> notRelateModels = new ArrayList<TableModel>(); 
		for (TableModel tbModel : tableModels) {
			if (!hasRelateModelIds.contains(tbModel.getTableModelId()))
				notRelateModels.add(tbModel);
		}
		return notRelateModels;
	}
	
	private List<String> getHasRelateTableModelIds(List<TableModel> models) {
		List<String> modelIds = new ArrayList<String>();
		for (TableModel model : models) {
			modelIds.add(model.getTableModelId());
		}
		return modelIds;
	}
	
	@RequestMapping(value = "/getTableModelById.load")
	public @ResponseBody TableModel getTableModelById(HttpServletRequest request, @RequestParam("catalogId") String tableModelId) 
			throws DataSharingMgrError {
		return dataSharingMgrService.getTableModelById(tableModelId);
	}
		
	@RequestMapping("/queryDataSharingDB.load")
	public @ResponseBody List<List<String>> queryDataSharingDB(HttpServletRequest request, @RequestParam("sql") String sql) 
			throws SQLException, DataSharingMgrError {
		String userId = PrivilegeCheckingHelper.getUserId(request.getSession());
		ExtUser user = dataSharingMgrService.getExtUserById(userId);
		
		List<List<String>> result = dataSharingDBService.executeQuery(sql, user.getName() + "." + PrivilegeCheckingHelper.getTenantName(request.getSession()), 
				user.getPassword(), 20);
		
		return result;
	}
	
	@RequestMapping("/schemaJdbcTable.load")
	public @ResponseBody List<Table> getJdbcSchemasByDbAddress(HttpServletRequest request,
			@RequestParam("dbAddressId") String dbAddressId, String schemaName) throws MDServiceError, DataSharingMgrError {
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		return mdService.getAllTablesMetadata(dbAddress.getJdbcURL(), dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName);
	}
	
	@RequestMapping("/schemaJdbcTableColums.load")
	public @ResponseBody List<Column> getJdbcSchemasByDbAddress(HttpServletRequest request,
			@RequestParam("dbAddressId") String dbAddressId, String schemaName, String tableName) throws MDServiceError, DataSharingMgrError {
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		Table jdbcTable =  mdService.getTableWithColumnsMetadata(dbAddress.getJdbcURL(), dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName, tableName);
		return jdbcTable.getColumns();
	}
	
	@RequestMapping("repository")
	public String repository(HttpServletRequest request) {
		return "service-center/repository";
	}

	@RequestMapping("authority")
	public String authority(HttpServletRequest request, Model model) {
		model.addAttribute("root", kRoot);
		return "service-center/authority";
	}
}