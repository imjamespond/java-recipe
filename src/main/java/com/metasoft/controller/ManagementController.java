package com.metasoft.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.keymobile.DBServices.interfaces.DBService;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.MiscInfo;
import com.keymobile.dataSharingMgr.interfaces.Tenant;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.DBColumn;
import com.keymobile.dataSharingMgr.interfaces.resource.DBTable;
import com.keymobile.dataSharingMgr.interfaces.resource.ModelCatalog;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModelColumn;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModelTypes;
import com.keymobile.dataSharingMgr.interfaces.security.ExtDomainPrivilege;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.keymobile.metadataServices.interfaces.Column;
import com.keymobile.metadataServices.interfaces.MDService;
import com.keymobile.metadataServices.interfaces.MDServiceError;
import com.keymobile.metadataServices.interfaces.Table;
import com.keymobile.portal.entity.User;
import com.metasoft.model.AnalyticalModelDoc;
import com.metasoft.model.Constant;
import com.metasoft.model.DataSource;
import com.metasoft.model.Document;
import com.metasoft.model.ExtConvertor;
import com.metasoft.model.ExtTableModel;
import com.metasoft.model.ExtUserWrapper;
import com.metasoft.model.KpiDoc;
import com.metasoft.model.Notice;
import com.metasoft.model.PrivilegeError;
import com.metasoft.model.TableModelColumnWrapper;
import com.metasoft.model.TableWrapper;
import com.metasoft.model.TenantWrapper;
import com.metasoft.model.dao.SubUserTableModelDao;
import com.metasoft.model.exception.RestInvokeException;
import com.metasoft.service.DataQueryService;
import com.metasoft.service.DataSharingDBService;
import com.metasoft.service.HomePageService;
import com.metasoft.service.KpiFullTextSearchService;
import com.metasoft.service.LocalizationService;
import com.metasoft.service.LogStatsService;
import com.metasoft.service.TableModelFullTextSearchService;
import com.metasoft.service.dao.SubUserTableModelDaoService;
import com.metasoft.service.dao.SubUserTableModelDaoService.FavoriteIndicator;
import com.metasoft.util.JsonTreeHelper;
import com.metasoft.util.JsonTreeHelper.JsonNode;
import com.metasoft.util.Attr;
import com.metasoft.util.AttributeUtil;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping(value = "/management")
public class ManagementController {

	public static final String kRoot = "数据共享管理中心";
	public static final String Default_CatalogName = "default";
	public static final int SampleSize = 5;
	private Logger logger = LoggerFactory.getLogger(ManagementController.class);

	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	@Autowired
	private MDService mdService;
	@Autowired
	private LogStatsService logStatsService;
	@Autowired
	private DataSharingDBService dataSharingDBService;
	@Autowired
	private HomePageService homePageService;
	@Autowired
	private TableModelFullTextSearchService tableModelFullTextSearchService;
	@Autowired
	private KpiFullTextSearchService kpiFullTextSearchService;
	@Autowired
	private DataQueryService dataQueryService;
	@Autowired
	private DBService dbService;
	@Autowired
	private SubUserTableModelDaoService subscriptionService;
	@Autowired
	private LocalizationService localizationService;
	
	private void logMiscInfo(MiscInfo miscInfo, HttpSession session) {
//		long currentTs = System.currentTimeMillis();
//		if (miscInfo.getCreator() == null || miscInfo.getCreator().length() == 0) {
//			miscInfo.setCreator(PrivilegeCheckingHelper.getUserId(session));
//			miscInfo.setCreateDate(currentTs);
//		}
//		miscInfo.setModifier(PrivilegeCheckingHelper.getUserId(session));
//		miscInfo.setModifyDate(currentTs);
	}
	
	private void displayMiscInfo(MiscInfo miscInfo) throws DataSharingMgrError {
//		ExtUser modifier = dataSharingMgrService.getExtUserById(miscInfo.getModifier());
//		ExtUser creator = dataSharingMgrService.getExtUserById(miscInfo.getCreator());
//		
//		miscInfo.setModifier(modifier == null ? "deleted" : modifier.getName());
//		miscInfo.setCreator(creator == null ? "deleted" : creator.getName());
	}
	
	/**
	 * Add a new tenant.
	 */
	@RequestMapping(value = "/tenant-add.post", method = RequestMethod.POST)
	public @ResponseBody String addTenant(HttpServletRequest request, TenantWrapper tenantWrapper) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();		
		if (!PrivilegeCheckingHelper.isSysAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Required);
		
		Tenant tenant = ExtConvertor.cast(tenantWrapper);
		logMiscInfo(tenant, session);
		dataSharingMgrService.addTenant(tenant);
		return "ok";
	}
	
	/**
	 * List all tenants.
	 */
	@RequestMapping(value = "/tenant.load", method = RequestMethod.GET)
	public @ResponseBody List<TenantWrapper> showTenants(HttpServletRequest request) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();

		List<Tenant> founded = new ArrayList<>();
		if (PrivilegeCheckingHelper.isSysAdmin(session)) {
			founded.addAll(dataSharingMgrService.getAllTenants());
		} else if (PrivilegeCheckingHelper.isTenantAdmin(session)) {
			String selfTenantId = PrivilegeCheckingHelper.getTenantId(session);
			founded.add(dataSharingMgrService.getTenantById(selfTenantId));
		} else {
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		}
		
		List<TenantWrapper> ret = new ArrayList<>();
		for (Tenant tenant : founded) {
			TenantWrapper tenantWrapper = ExtConvertor.cast(tenant);
			displayMiscInfo(tenantWrapper);
			ret.add(tenantWrapper);
		}
		return ret;
	}
	
	/**
	 * List all tenants.
	 */
	@RequestMapping(value = "/tenants-all.load", method = RequestMethod.GET)
	public @ResponseBody List<TenantWrapper> showAllTenants(HttpServletRequest request) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		List<TenantWrapper> ret = new ArrayList<>();
		for (Tenant tenant : dataSharingMgrService.getAllTenants()) {
			TenantWrapper tenantWrapper = ExtConvertor.cast(tenant);
			displayMiscInfo(tenantWrapper);
			ret.add(tenantWrapper);
		}
		return ret;
	}
	
	
	/**
	 * Update a tenant.
	 */
	@RequestMapping(value = "/tenant-update.post", method = RequestMethod.POST)
	public @ResponseBody String updateTenant(HttpServletRequest request, TenantWrapper tenantWrapper) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();		
		if (!PrivilegeCheckingHelper.isSysAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Required);
		
		Tenant tenant = ExtConvertor.cast(tenantWrapper);
		Tenant original = dataSharingMgrService.getTenantById(tenantWrapper.getDomainId()); // todo: using tenantId instead
		//complements missing info
		tenant.setCreator(original.getCreator());
		tenant.setCreateDate(original.getCreateDate());
		logMiscInfo(tenant, session);
		dataSharingMgrService.updateTenant(tenant);
		return "ok";
	}
	
	/**
	 * Delete a tenant.
	 */
	@RequestMapping(value = "/tenant-del.post", method = RequestMethod.POST) 
	public @ResponseBody String updateTenant(HttpServletRequest request, @RequestParam("tenantId") String tenantId) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();		
		if (!PrivilegeCheckingHelper.isSysAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Required);
		
		dataSharingMgrService.deleteTenant(tenantId);
		return "ok";
	}
	
	/**
	 * Add a new tenant user.
	 */
	@RequestMapping(value = "/user-add.post", method = RequestMethod.POST)
	public @ResponseBody String addUser(HttpServletRequest request, ExtUserWrapper userWrapper,
			@RequestParam("tenantId") String tenantId) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		if (PrivilegeCheckingHelper.isTenantAdmin(session) && !PrivilegeCheckingHelper.getTenantId(session).equals(tenantId))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		
		ExtUser toBeCreate = ExtConvertor.cast(userWrapper);
		logMiscInfo(toBeCreate, session);
		dataSharingMgrService.addTenantUser(toBeCreate, tenantId);
		return "ok";
	}
	
	/**
	 * List all users of a given tenant.
	 */
	@RequestMapping("/user.load")
	public @ResponseBody List<ExtUserWrapper> showUsers(HttpServletRequest request, @RequestParam("tenantId") String tenantId) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		if (PrivilegeCheckingHelper.isTenantAdmin(session) && !PrivilegeCheckingHelper.getTenantId(session).equals(tenantId))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		
		List<ExtUser> founded = dataSharingMgrService.getAllTenantUsers(tenantId);
		List<ExtUserWrapper> ret = new ArrayList<>();
		for (ExtUser user : founded) {
			ExtUserWrapper userWrapper = ExtConvertor.cast(user);
			displayMiscInfo(userWrapper);
			ret.add(userWrapper);
		}
		return ret;
	}
	
	/**
	 * Update a tenant user.
	 */
	@RequestMapping(value = "/user-update.post", method = RequestMethod.POST)
	public @ResponseBody String updateUser(HttpServletRequest request, ExtUserWrapper userDef) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		ExtUser original = dataSharingMgrService.getExtUserById(userDef.getUserId());

		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		if (PrivilegeCheckingHelper.isTenantAdmin(session) && !PrivilegeCheckingHelper.getTenantId(session).equals(original.getTenantId()))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		
		ExtUser toBeUpdate = ExtConvertor.cast(userDef);
		//complements missing info
		toBeUpdate.setDomainId(original.getDomainId()); 
		toBeUpdate.setCreator(original.getCreator());
		toBeUpdate.setCreateDate(original.getCreateDate());
		logMiscInfo(toBeUpdate, session);
		dataSharingMgrService.updateExtUser(toBeUpdate);
		return "ok";		
	}
	
	/**
	 * Delete a tenant user.
	 */
	@RequestMapping(value = "/user-del.post", method = RequestMethod.POST) 
	public @ResponseBody String deleteUser(HttpServletRequest request, @RequestParam("userId") String userId) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();		
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		ExtUser user = dataSharingMgrService.getExtUserById(userId);
		if (user != null) {
			if (PrivilegeCheckingHelper.isTenantAdmin(session) && !PrivilegeCheckingHelper.getTenantId(session).equals(user.getTenantId())) {
				throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
			}
			dataSharingMgrService.deleteExtUser(user.getUserId());
		}
		
		return "ok";
	}

	/**
	 * List all data source.
	 */
	@RequestMapping("/resource.load")
	public @ResponseBody Map<String, Object> showDataSources(HttpServletRequest request, 
			@RequestParam(value="isShowAll", defaultValue="false") boolean isShowAll, boolean isFilterKpiType) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		Map<String, Object> data = new HashMap<String, Object>();
		List<Attr> attrList  = new ArrayList<Attr>();
		for (Attr attr : AttributeUtil.GetAttributes(DataSource.class)) {
			if (!"dbAddressId".equals(attr.attr) && !"dbPwd".equals(attr.attr))
				attrList.add(attr);
		}
		data.put("attributes", attrList);

		List<DataSource> dataSources = new ArrayList<DataSource>();
		List<Tenant> allTenants = new ArrayList<Tenant>();
		if (isShowAll || PrivilegeCheckingHelper.isSysAdmin(session)) {
			allTenants.addAll(dataSharingMgrService.getAllTenants());
		} else {
			allTenants.add(dataSharingMgrService.getTenantByName(PrivilegeCheckingHelper.getTenantName(session)));
		}
		for (Tenant tenant : allTenants) {
			for(DBAddress dbAddress: dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId())) {
				displayMiscInfo(dbAddress);
				if (isFilterKpiType) {
					if (!dbAddress.getType().equals("kpi")) 
						dataSources.add(new DataSource(dbAddress, tenant.getName()));
				} else {
					dataSources.add(new DataSource(dbAddress, tenant.getName()));
				}
			}
		}
		data.put("items", dataSources);
		return data;		
	}
	
	/**
	 * Add a new data source.
	 */
	@RequestMapping(value = "/resource-add.post", method = RequestMethod.POST)
	public @ResponseBody String addDBResource(HttpServletRequest request, DBAddress dbAddress)
			throws DataSharingMgrError, PrivilegeError, MDServiceError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);

		logMiscInfo(dbAddress, session);
		DBAddress created = dataSharingMgrService.addDBAddress(dbAddress, PrivilegeCheckingHelper.getTenantId(session));
		ModelCatalog defaultCatalog = new ModelCatalog();
		defaultCatalog.setName(Default_CatalogName);
		logMiscInfo(defaultCatalog, session);
		dataSharingMgrService.addModelCatalog(defaultCatalog, created.getDbAddressId());
		return "ok";
	}
	
	/**
	 * Update a data source.
	 */
	@RequestMapping(value = "/resource-update.post", method = RequestMethod.POST)
	public @ResponseBody String updateDBResource(HttpServletRequest request, DBAddress dbAddress)
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress origin = dataSharingMgrService.getDBAddressById(dbAddress.getDbAddressId());
		if (!origin.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		logMiscInfo(dbAddress, session);
		dataSharingMgrService.updateDBAddress(dbAddress);
		return "ok";
	}
	
	/**
	 * Data source connection test.
	 */
	@RequestMapping(value = "/jdbcTest.post", method = RequestMethod.POST)
	public @ResponseBody boolean testJDBCConnectivity(HttpServletRequest request, 
			@RequestParam("jdbcURL") String jdbcURL, @RequestParam("userName") String userName, 
			@RequestParam("userPwd") String userPwd) throws MDServiceError {
		return mdService.testDataSourceConnectivity(jdbcURL, userName, userPwd);
	}
	
	/**
	 * List data source schema.
	 */
	@RequestMapping("/jdbcSchema.load")
	public @ResponseBody Map<String, Object> getJdbcSchemas(HttpServletRequest request, DBAddress dbAddress) 
			throws MDServiceError, DataSharingMgrError {
		HttpSession session = request.getSession();
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("items", mdService.getAllSchemasMetadata(dbAddress.getJdbcURL(), 
				dbAddress.getJdbcURL().startsWith("jdbc:drill") ? PrivilegeCheckingHelper.getTenantName(session) : dbAddress.getDbUser(), dbAddress.getDbPwd()));
		return data;
	}

	/**
	 * List data source table.
	 * @throws IOException 
	 */
	@RequestMapping("/jdbcTable.load")
	public @ResponseBody Map<String, Object> getJdbcTables(HttpServletRequest request,
			@RequestParam("schemaName") String schemaName, DBAddress dbAddress) throws MDServiceError, DataSharingMgrError, IOException {
		HttpSession session = request.getSession();

		Map<String, Object> data = new HashMap<String, Object>();
		List<TableWrapper> ret = new ArrayList<>();		
		List<String> dbTableNames = dataSharingMgrService.getDBTableNames(dbAddress.getDbAddressId());

		List<Table> sourceTables = mdService.getAllTablesMetadata(dbAddress.getJdbcURL(), dbAddress.getJdbcURL().startsWith("jdbc:drill") ? PrivilegeCheckingHelper.getTenantName(session) : dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName);
		for (Table sourceTable : sourceTables) {
			if (dbTableNames.contains(schemaName + "." + sourceTable.getName()))
				ret.add(new TableWrapper(sourceTable, false));
			else		
				ret.add(new TableWrapper(sourceTable, true));
		}
		
		data.put("items", ret);
		return data;
	}

	/**
	 * Show data source table info.
	 * @throws IOException 
	 */
	@RequestMapping("/jdbcTableDetail.load")
	public @ResponseBody Table getJdbcTableDetail(HttpServletRequest request,
			@RequestParam("schemaName") String schemaName, @RequestParam("tableName") String tableName, DBAddress dbAddress) throws MDServiceError, IOException {
		HttpSession session = request.getSession();
		Table jdbcTable = mdService.getTableWithColumnsMetadata(dbAddress.getJdbcURL(), dbAddress.getJdbcURL().startsWith("jdbc:drill") ? PrivilegeCheckingHelper.getTenantName(session) : dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName, tableName);
		return jdbcTable;
	}
		
	@RequestMapping("/jdbcTableSampleData.load")
	public @ResponseBody Map<String, Object> getJdbcTableSampleData(HttpServletRequest request,@RequestParam("schemaName") String schemaName, @RequestParam("tableName") String tableName, 
			@RequestParam("dbAddressId") String dbAddressId) throws SQLException, DataSharingMgrError {
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (dbAddress.getJdbcURL().startsWith("jdbc:drill")) {
			String sql = "select * from " + schemaName + ".`" + PrivilegeCheckingHelper.getTenantName(request.getSession()) + "/" + tableName + "/*.xlsx`";
			
			return dbService.executeSQL(sql, dbAddress.getJdbcURL(), dbAddress.getDbUser(), dbAddress.getDbPwd(), SampleSize);
		} else if (dbAddress.getJdbcURL().startsWith("kpi:")) {
			return dbService.executeQuerykpiData(dbAddress.getJdbcURL(), schemaName+"/"+tableName, SampleSize);
		} else {
			return dbService.executeQueryByPagination(schemaName, tableName, dbAddress.getJdbcURL(), dbAddress.getDbUser(), dbAddress.getDbPwd(), 0, SampleSize);
		}
	}
	
	/**
	 * Add a table metadata.
	 * @throws IOException 
	 */
	@RequestMapping(value = "/addDbTable.post", method = RequestMethod.POST)
	public @ResponseBody String addDbTable(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId, 
			@RequestParam("tableFullNames[]") List<String> tableFullNames)
					throws DataSharingMgrError, MDServiceError, PrivilegeError, IOException {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		int dbTableAdded = 0;
		int dbTableFailed = 0;
		for (String tableFullName : tableFullNames) {
			String[] nameParts = tableFullName.split("\\.");
			String tableName = nameParts[nameParts.length - 1];
			String schemaName = tableFullName.substring(0, tableFullName.length() - tableName.length() - 1);		
			Table jdbcTable = mdService.getTableWithColumnsMetadata(dbAddress.getJdbcURL(), dbAddress.getJdbcURL().startsWith("jdbc:drill") ? PrivilegeCheckingHelper.getTenantName(session) : dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName, tableName);
			
			try {
				DBTable dbTable = makeDBTable(jdbcTable.getColumns(), schemaName + "." + tableName, jdbcTable.getRemarks(), jdbcTable.getCatalog(), jdbcTable.getSelfRefColName());
				if (dbAddress.getJdbcURL().startsWith("jdbc:drill")) {
					dbTable.setNameInSource(schemaName + ".`" + PrivilegeCheckingHelper.getTenantName(session) + File.separator + tableName + File.separator + "*.xlsx`");
				}
				dataSharingMgrService.addDBTable(dbTable, dbAddressId);
				dbTableAdded++;
			} catch (DataSharingMgrError e) {
				dbTableFailed++;
				logger.error("addDBTable " + tableName + " failed.", e);
			}
		}
		if (dbTableFailed == 0)
			return "ok";
		else
			return "Sucessed :" + dbTableAdded + ", Failed :" + dbTableFailed;
	}
	
	private DBTable makeDBTable(List<Column> jdbcColumns, String dbTableName, String remarks, String catalog, String selfRefColName) {
		DBColumn[] dbColumns = new DBColumn[jdbcColumns.size()];
		for (int i = 0; i < jdbcColumns.size(); i++) {
			Column jdbcColumn = jdbcColumns.get(i);
			DBColumn dbColumn = new DBColumn(jdbcColumn.getName(), jdbcColumn.getSqlDataType(), jdbcColumn.getSize(), 
					jdbcColumn.getDecimalDigit(), jdbcColumn.getCharOctetLength(), jdbcColumn.getNullable(), jdbcColumn.getOrdinalPosition(), jdbcColumn.getRemarks());
			dbColumns[i] = dbColumn;
		}
		DBTable dbTable = new DBTable(dbTableName);
		dbTable.setColumns(dbColumns);
		dbTable.setRemarks(remarks);
		dbTable.setCatalog(catalog);
		dbTable.setSelfRefColName(selfRefColName);
		return dbTable;
	}
	
	/**
	 * Add all tables metadata of a given schema
	 */
	@RequestMapping(value = "/addDbTableBySchema", method = RequestMethod.POST)
	public @ResponseBody String addDbTableJson(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId,
			@RequestParam("schemaName") String schemaName)
					throws DataSharingMgrError, MDServiceError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		Map<Table, List<Column>> allJDBCTableColumns = mdService.getAllTableColumnsMetadata(dbAddress.getJdbcURL(), 
				dbAddress.getDbUser(), dbAddress.getDbPwd(), schemaName);
		int dbTableAdded = 0;
		int dbTableFailed = 0;
		for (Table jdbcTable : allJDBCTableColumns.keySet()) {
			List<Column> jdbcColumns = allJDBCTableColumns.get(jdbcTable);
			try {
				dataSharingMgrService.addDBTable(makeDBTable(jdbcColumns, jdbcTable.getSchema() + "." + jdbcTable.getName(), jdbcTable.getRemarks(), jdbcTable.getCatalog(), jdbcTable.getSelfRefColName()), dbAddressId);
				dbTableAdded++;
			} catch (DataSharingMgrError e) {
				dbTableFailed++;
				logger.error("addDBTable " + jdbcTable.getName() + " failed.", e);
			}
		}
		if (dbTableFailed == 0)
			return "ok";
		else
			return "Sucessed :" + dbTableAdded + ", Failed :" + dbTableFailed;
	}

	/**
	 * List all added physical model names.
	 */
	@RequestMapping("/dbTableNames.load")
	public @ResponseBody List<String> showDbTables(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId)
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		List<String> dbTableNames = dataSharingMgrService.getDBTableNames(dbAddressId);
		return dbTableNames;
	}
	
	@RequestMapping("/dbTableNamesWithRef.load")
	public @ResponseBody List<String> showDbTablesWithRef(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId)
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		List<String> dbTableNames = dataSharingMgrService.getDBTablesWithRef(dbAddressId);
		return dbTableNames;
	}
	
	@RequestMapping("/dbTableNamesWithoutRef.load")
	public @ResponseBody List<String> showDbTablesWithoutRef(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId)
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		List<String> dbTableNames = dataSharingMgrService.getDBTablesWithoutRef(dbAddressId);
		return dbTableNames;
	}
	
	/**
	 * Remove physical models by name.
	 */
	@RequestMapping(value = "/deleteDBTables.post", method = RequestMethod.POST)
	public @ResponseBody String deleteDBTable(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId, 
			@RequestParam("dbTableNames[]") List<String> dbTableNames) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
		    throw new PrivilegeError(Constant.TenAdmin_Required);
		
		for (String dbTableName : dbTableNames) {
			DBTable dbTable = dataSharingMgrService.getDBTableByName(dbAddressId, dbTableName);
			if (dbTable != null)
				dataSharingMgrService.deleteDBTable(dbTable.getDBTableId());
		}
		return "ok";
	}
	
	/**
	 * Show detail of a physical model.
	 */
	@RequestMapping("/dbTableDetail.load")
	public @ResponseBody DBTable showDbTableDetail(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId,
			@RequestParam("dbTableName") String dbTableName) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		return dataSharingMgrService.getDBTableByName(dbAddressId, dbTableName);
	}
	
	/**
	 * Add a new ModelCatalog.
	 */
	@RequestMapping(value = "/modelCatalog-add.post", method = RequestMethod.POST)
	public @ResponseBody ModelCatalog addModelCatalog(HttpServletRequest request, 
			@RequestParam("catalogName") String catalogName, @RequestParam("dbAddressId") String dbAddressId)
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		ModelCatalog catalog = new ModelCatalog();
		catalog.setName(catalogName);
		logMiscInfo(catalog, session);
		ModelCatalog created = dataSharingMgrService.addModelCatalog(catalog, dbAddressId);
		return created;
	}
	
	/**
	 * List all catalogs under given data source.
	 */
	@RequestMapping("/modelCatalogs.load")
	public @ResponseBody Map<String, Object> getModelCatalogByDBAddressId(HttpServletRequest request, 
			@RequestParam("dbAddressId") String dbAddressId) throws DataSharingMgrError {
		List<TableModel> tableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddressId);
		Map<String, Object> ret = new HashMap<>(); 
		if (tableModels.size() == 0) {
			ret.put("emptyModels", 1);
		} else {
			ret.put("emptyModels", 2);
		}
		List<ModelCatalog> catalogs = dataSharingMgrService.getModelCatalog(dbAddressId);
		for (ModelCatalog catalog : catalogs) {
			displayMiscInfo(catalog);
		}
		ret.put("ModelCatalog", catalogs);
		return ret;
	}

	/**
	 * Delete a ModelCatalog.
	 */
	@RequestMapping(value = "/modelCatalog-del.post", method = RequestMethod.POST)
	public @ResponseBody String deleteModelCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		ModelCatalog catalog = dataSharingMgrService.getModelCatalogById(catalogId);
		if (catalog != null) {
//			if (!catalog.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
//				throw new PrivilegeError(Constant.TenAdmin_Required);
			//todo: correct catalog object
			dataSharingMgrService.deleteModelCatalog(catalogId);
		}
		return "ok";
	}
	
	/**
	 * Rename a ModelCatalog.
	 */
	@RequestMapping(value = "/modelCatalog-rename.post", method = RequestMethod.POST)
	public @ResponseBody String renameModelCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId, 
			@RequestParam("catalogName") String catalogName) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		ModelCatalog modelCatalog = dataSharingMgrService.getModelCatalogById(catalogId);
		if (modelCatalog != null) {
			if (!modelCatalog.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
				throw new PrivilegeError(Constant.TenAdmin_Required);
			
			if (modelCatalog.getName() != catalogName) {
				modelCatalog.setName(catalogName);
				logMiscInfo(modelCatalog, session);
				dataSharingMgrService.updateModelCatalog(modelCatalog);
			}
		}
		return "ok";
	}
		
	@RequestMapping(value = "/listTableModelsByCatalog.load")
	public @ResponseBody List<TableModel> listTableModelsByCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId) 
		throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		
		List<TableModel> tableModels = dataSharingMgrService.getTableModelsByCatalog(catalogId);
		
		return tableModels;
	}
	
	@RequestMapping(value = "/listPrivilegeTableModelsByCatalog.load")
	public @ResponseBody List<TableModel> listPrivilegeTableModelsByCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId) 
		throws DataSharingMgrError, PrivilegeError, IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isSysAdmin(session) && !PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.SysAdmin_Or_TenAdmin_Required);
		
		List<TableModel> catalogTableModels = dataSharingMgrService.getTableModelsByCatalog(catalogId);
		List<ExtTableModel> privilegeTableModels = getPrivielgedTableModels(request);
		List<TableModel> tableModels = new ArrayList<>();
		for (TableModel tableModel : catalogTableModels) {
			if (tableModel.getType().equals("dbType")) continue;
			for (TableModel privilegeTable : privilegeTableModels) {
				if (privilegeTable.getTableModelId().equals(tableModel.getTableModelId())) {
					tableModels.add(tableModel);
					break;
				}
			}
		}
		return tableModels;
	}
	
	@RequestMapping(value = "/listEnabledTableModelsByCatalog.load")
	public @ResponseBody List<ExtTableModel> listEnabledTableModelsByCatalog(HttpServletRequest request, @RequestParam("catalogId") String catalogId)
			throws DataSharingMgrError {
		List<TableModel> tableModels = dataSharingMgrService.getTableModelsByCatalog(catalogId);
		Iterator<TableModel> i = tableModels.iterator();
		while (i.hasNext()) {
			TableModel tableModel = i.next();
			if (!tableModel.isEnabled())
				i.remove();
		}
		
		List<String> tableModelIds = new ArrayList<>();
		tableModels.forEach(t -> tableModelIds.add(t.getTableModelId()));
		Set<String> accessible 
			= dataSharingMgrService.checkTableModelsAccessible(tableModelIds, PrivilegeCheckingHelper.getUserId(request.getSession()));
		List<ExtTableModel> ret = new ArrayList<>();
		tableModels.forEach(t -> ret.add(new ExtTableModel(t, accessible.contains(t.getTableModelId()))));
				
		return ret;		
	}
	
	@RequestMapping(value = "/listQuickViewsByDBAddress.load")
	public @ResponseBody List<ExtTableModel> listQuickViewsByDBAddress(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId)
		throws DataSharingMgrError {
		List<TableModel> tableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddressId);
		Iterator<TableModel> i = tableModels.iterator();
		while (i.hasNext()) {
			TableModel tableModel = i.next();
			if (!tableModel.isEnabled() || !tableModel.getType().equals(TableModelTypes.QUICK))
				i.remove();
		}
		
		List<String> tableModelIds = new ArrayList<>();
		tableModels.forEach(t -> tableModelIds.add(t.getTableModelId()));
		Set<String> accessible 
			= dataSharingMgrService.checkTableModelsAccessible(tableModelIds, PrivilegeCheckingHelper.getUserId(request.getSession()));
		List<ExtTableModel> ret = new ArrayList<>();
		tableModels.forEach(t -> ret.add(new ExtTableModel(t, accessible.contains(t.getTableModelId()))));
				
		return ret;	
	}

	/**
	 * Catalog a TableModel.
	 */
	@RequestMapping(value = "/catalogTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String catalogTableModel(HttpServletRequest request, 
			@RequestParam("catalogIds[]") List<String> catalogIds, @RequestParam("tableModelId") String tableModelId) 
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		if (tableModel != null) {
			if (!tableModel.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session))) 
				throw new PrivilegeError(Constant.TenAdmin_Required);
			
			for (String catalogId : catalogIds) {
				ModelCatalog catalog = dataSharingMgrService.getModelCatalogById(catalogId);
				if (!catalog.getName().equals(Default_CatalogName))
					dataSharingMgrService.addTableModelIntoCatalog(tableModelId, catalogId);
			}
		}
		return "ok";
	}
	
	/**
	 * Uncatalog a TableModel from a given catalog.
	 */
	@RequestMapping(value = "/uncatalogTableModel.post", method = RequestMethod.POST)
	public @ResponseBody String uncatalogTableModel(HttpServletRequest request, @RequestParam("catalogId") String catalogId, 
			@RequestParam("tableModelIds[]") List<String> tableModelIds) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		ModelCatalog catalog = dataSharingMgrService.getModelCatalogById(catalogId);
		if (catalog.getName().equals(Default_CatalogName))
			return "ok";
		
		for (String tableModelId : tableModelIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
			if (tableModel != null) {
				if (!tableModel.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session))) 
					throw new PrivilegeError(Constant.TenAdmin_Required);
			
				dataSharingMgrService.removeTableModelFromCatalog(tableModelId, catalogId);
			}
		}
		return "ok";
	}
	
	@RequestMapping(value = "/moveTableModels.post", method = RequestMethod.POST)
	public @ResponseBody String moveTableModels(HttpServletRequest request, @RequestParam("sourceCatalogId") String sCatalogId, 
			@RequestParam("tableModelIds") List<String> tableModelIds, @RequestParam("targetCatalogId") String tCatalogId) 
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		for (String tableModelId : tableModelIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
			if (tableModel != null) {
				if (!tableModel.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session))) 
					throw new PrivilegeError(Constant.TenAdmin_Required);
			
				ModelCatalog sCatalog = dataSharingMgrService.getModelCatalogById(sCatalogId);
				if (!sCatalog.getName().equals(Default_CatalogName))
					dataSharingMgrService.removeTableModelFromCatalog(tableModelId, sCatalogId);
				dataSharingMgrService.addTableModelIntoCatalog(tableModelId, tCatalogId);
			}
		}
		
		return "ok";
	}
	
	/**
	 * Find catalogs of a TableModel.
	 */
	@RequestMapping(value = "/listCatalogsOfTableModel.load")
	public @ResponseBody List<String> getCatalogsOfTableModel(HttpServletRequest request, 
			@RequestParam("tableModelId") String tableModelId, @RequestParam("dbAddressId") String dbAddressId) throws DataSharingMgrError, PrivilegeError {
		List<ModelCatalog> catalogs = dataSharingMgrService.searchCatalogsByTableModel(tableModelId, dbAddressId);
		List<String> ret = new ArrayList<>();
		for (ModelCatalog catalog : catalogs) {
			ret.add(catalog.getModelCatalogId());
		}
		return ret;
	}
		
	/**
	 * Show a TableModel.
	 */
	@RequestMapping(value = "/tableModel.load", method = RequestMethod.GET)
	public @ResponseBody TableModel getTableModelById(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId) 
			throws DataSharingMgrError {
		return dataSharingMgrService.getTableModelById(tableModelId);
	}
	
	/**
	 * Show a DBTable.
	 */
	@RequestMapping(value = "/getDBTableById", method = RequestMethod.GET)
	public @ResponseBody DBTable getDBTableById(HttpServletRequest request, @RequestParam("dbTableId") String dbTableId) 
			throws DataSharingMgrError {
		return dataSharingMgrService.getDBTableById(dbTableId);
	}
		
	/**
	 * Make a TableModel.
	 * @throws IOException 
	 */
	@RequestMapping(value = "/makeSimpleTableModel.post")
	public @ResponseBody TableModel makeTableModelByMold(HttpServletRequest request, @RequestParam String sql, 
			@RequestParam("dbAddressId") String dbAddressId, @RequestParam("remarks") String remarks)
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		TableModel tableModel = new TableModel(sql, TableModelTypes.SIMPLE);
		tableModel.setRemarks(remarks);
		tableModel = dataSharingMgrService.addTableModel(tableModel, dbAddressId);
		ModelCatalog defaultCatalog = dataSharingMgrService.getModelCatalogByName(Default_CatalogName, dbAddressId);
		if (defaultCatalog != null)
			dataSharingMgrService.addTableModelIntoCatalog(tableModel.getTableModelId(), defaultCatalog.getModelCatalogId());
		tableModelFullTextSearchService.indexTableModel(tableModel);
		return tableModel;
	}
	
	@RequestMapping(value = "/makeDerivedTableModel.post")
	public @ResponseBody TableModel makeDerivedTableModel(HttpServletRequest request, @RequestParam String sql, 
			@RequestParam("dbAddressId") String dbAddressId, @RequestParam("remarks") String remarks) 
					throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		TableModel tableModel = new TableModel(sql, TableModelTypes.VIEW);
		tableModel.setIsEnabled(1);
		tableModel.setRemarks(remarks);
		tableModel = dataSharingMgrService.addTableModel(tableModel, dbAddressId);

		return tableModel;
	}
	
	@RequestMapping(value = "/makeTableModelsByDBTables.post")
	public @ResponseBody String makeTableModelsByDBTables(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId, 
			@RequestParam("dbTableNames[]") List<String> dbTableNames) throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(dbAddressId);
		if (!dbAddress.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		for (String baseTableName : dbTableNames) {
			DBTable baseTable = dataSharingMgrService.getDBTableByName(dbAddressId, baseTableName);
			if (baseTable == null)
				continue;
			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			sb.append(Arrays.stream(baseTable.getColumns())
					.map(p -> p.getDBColumnId() + " as " + (p.getRemarks() == null ? p.getName() : p.getRemarks())).collect(Collectors.joining(",")));
			sb.append(" from ");
			sb.append(baseTable.getDBTableId() + " as " + baseTable.getName().split("\\.")[1]);
			String sql = sb.toString();
			TableModel tableModel = new TableModel(sql, TableModelTypes.SIMPLE);
			tableModel.setRemarks(baseTable.getRemarks());
			
			tableModel = dataSharingMgrService.addTableModel(tableModel, baseTable.getDBAddressId());
			ModelCatalog defaultCatalog = dataSharingMgrService.getModelCatalogByName(Default_CatalogName, baseTable.getDBAddressId());
			if (defaultCatalog != null)
				dataSharingMgrService.addTableModelIntoCatalog(tableModel.getTableModelId(), defaultCatalog.getModelCatalogId());
			tableModelFullTextSearchService.indexTableModel(tableModel);
		}
		
		return "ok";
	}
	
	/**
	 * Delete TableModels.
	 */
	@RequestMapping(value = "/tableModels-del.post", method = RequestMethod.POST)
	public @ResponseBody String deleteTableModels(HttpServletRequest request, @RequestParam("tableModelIds") List<String> tableModelIds) 
			throws DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		for (String tableModelId : tableModelIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
			if (tableModel != null) {
				if (!tableModel.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session))) 
					throw new PrivilegeError(Constant.TenAdmin_Required);
				dataSharingMgrService.disableTableModel(tableModel.getTableModelId());
				dataSharingMgrService.deleteTableModelById(tableModelId);
				tableModelFullTextSearchService.unindexTableModel(tableModelId);
			}
		}
		return "ok";
	}
	
	@RequestMapping(value = "/enableTableModels.post", method = RequestMethod.POST)
	public @ResponseBody String enableTableModel(HttpServletRequest request, @RequestParam List<String> tableModelIds) throws DataSharingMgrError {
		for (String id : tableModelIds) {
			dataSharingMgrService.enableTableModel(id);
		}
		return "ok";
	}
	
	@RequestMapping(value = "/disableTableModels.post", method = RequestMethod.POST)
	public @ResponseBody String disableTableModel(HttpServletRequest request, @RequestParam List<String> tableModelIds) throws DataSharingMgrError {
		for (String id : tableModelIds) {
			dataSharingMgrService.disableTableModel(id);
		}
		return "ok";
	}
	
	/**
	 * Show DBColumns which form a TableModel.
	 */
	@RequestMapping(value = "/listUnderlyDBColumns", method = RequestMethod.GET)
	public @ResponseBody List<DBColumn> getUnderlyDBColumns(HttpServletRequest request, @RequestParam String tableModelId) 
			throws DataSharingMgrError {
		List<DBColumn> dbColumns = new ArrayList<>();

		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		if (tableModel.getType().equals(TableModelTypes.QUICK)) {
			tableModel.getAttachedColumns().forEach(t -> dbColumns.add(t.getDbColumn()));
		} else {	
			List<TableModelColumn> tableModelColumns = tableModel.getAttachedColumns();
			for (TableModelColumn tableModelColumn : tableModelColumns) {
				dbColumns.add(dataSharingMgrService.getDBColumnById(tableModelColumn.getDBColumnId()));
			}
		}
		
		return dbColumns;
	}
	
	/**
	 * Show Columns of a given TableModel.
	 */
	@RequestMapping(value = "/listUnderlyTableModelColumns", method = RequestMethod.GET)
	public @ResponseBody List<TableModelColumnWrapper> getUnderlyTableModelColumns(HttpServletRequest request, 
			@RequestParam String tableModelId) 
			throws DataSharingMgrError {
		List<TableModelColumnWrapper> ret = new ArrayList<>();
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		for (TableModelColumn tableModelColumn : tableModel.getAttachedColumns()) {
			ret.add(new TableModelColumnWrapper(tableModelColumn));
		}
		return ret;
	}
	
	@RequestMapping(value = "/privilegedQuickViews.load", method = RequestMethod.GET)
	public @ResponseBody List<TableModel> getPrivielgedQuickViews(HttpServletRequest request) throws DataSharingMgrError, IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		List<TableModel> ret = new ArrayList<>();
		if (PrivilegeCheckingHelper.isTenantAdmin(session)) {
			Map<String, ExtDomainPrivilege> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(PrivilegeCheckingHelper.getTenantId(session));
			for (String tableModelId : domainPrivileges.keySet()) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null && tableModel.isEnabled() && tableModel.getType().equals(TableModelTypes.QUICK)) 
					ret.add(tableModel);
			}
			for (DBAddress dbAddress : dataSharingMgrService.getDBAddressesByTenant(PrivilegeCheckingHelper.getTenantId(session))) {
				for (TableModel tableModel : dataSharingMgrService.getTableModelsByDBAddress(dbAddress.getDbAddressId())) {
					if (tableModel.isEnabled() && tableModel.getType().equals(TableModelTypes.QUICK)) 
						ret.add(tableModel);
				}
			}
		} else {
			Set<String> privilegedTableModelIds 
				= dataSharingMgrService.getPrivilegedTableModelIds(PrivilegeCheckingHelper.getUserId(session), PrivilegeCheckingHelper.getTenantId(session)).keySet();
			for (String tableModelId : privilegedTableModelIds) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null && tableModel.isEnabled() && tableModel.getType().equals(TableModelTypes.QUICK))
					ret.add(tableModel);	
			}
		}
		return ret;
	}

	@RequestMapping(value = "/privilegedTableModels.load", method = RequestMethod.GET)
	public @ResponseBody List<ExtTableModel> getPrivielgedTableModels(HttpServletRequest request) throws DataSharingMgrError, IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		List<TableModel> ret = new ArrayList<>();
		if (PrivilegeCheckingHelper.isTenantAdmin(session)) {
			Map<String, ExtDomainPrivilege> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(PrivilegeCheckingHelper.getTenantId(session));
			for (String tableModelId : domainPrivileges.keySet()) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null && tableModel.isEnabled()) 
					ret.add(tableModel);
			}
			for (DBAddress dbAddress : dataSharingMgrService.getDBAddressesByTenant(PrivilegeCheckingHelper.getTenantId(session))) {
				for (TableModel tableModel : dataSharingMgrService.getTableModelsByDBAddress(dbAddress.getDbAddressId())) {
					if (tableModel.isEnabled()) 
						ret.add(tableModel);
				}
			}
		} else {
			Set<String> privilegedTableModelIds 
				= dataSharingMgrService.getPrivilegedTableModelIds(PrivilegeCheckingHelper.getUserId(session), PrivilegeCheckingHelper.getTenantId(session)).keySet();
			for (String tableModelId : privilegedTableModelIds) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null && tableModel.isEnabled())
					ret.add(tableModel);	
			}
		}
		return checkIfTableModelIsSubscribedAndAddKpiDef(ret, PrivilegeCheckingHelper.getUserId(session));
	}
	
	private List<ExtTableModel> checkIfTableModelIsSubscribedAndAddKpiDef(List<TableModel> ret, String userId) throws IllegalAccessException, InvocationTargetException {
		List<ExtTableModel> extTableModels = new ArrayList<>();
		Map<String, SubUserTableModelDao> userModels = new HashMap<>();
		try {
			userModels = subscriptionService.getUserSubTableModelds(userId);
			new Thread(new CheckTableModelIsValidInSub(userId, ret)).start();
		} catch (Exception e) {
			logger.error("getUserSubTableModelIds error", e);
		}
		for (TableModel m : ret) {
			ExtTableModel extTableModel = new ExtTableModel(m, true);
			String tmodelId = m.getTableModelId();
			if (userModels.keySet().contains(tmodelId)) {
				extTableModel.setSubscribed(true);
				if (userModels.get(tmodelId).getSelectFlag() == SubUserTableModelDao.SELECT_FLAG) {
						extTableModel.setSelected(true);
					}
			}
			if (extTableModel.getSingleFlag() == 0 && extTableModel.isSubscribed()) {
				extTableModel.setSelectable(true);
			}
			if (extTableModel.getType().equals("kpi")) {
				extTableModel.setMisc(kpiFullTextSearchService.getKpiDefByNameAndCatalog(extTableModel.getName(), extTableModel.getNameInSource().split("\\.")[0]));
			}
			
			extTableModels.add(extTableModel);
		}
		return extTableModels;
	}
	
	private class CheckTableModelIsValidInSub implements Runnable{
		
		private String userId;
		private List<TableModel> extTableModels;
		
		public CheckTableModelIsValidInSub(String userId, List<TableModel> ret) {
			this.userId = userId;
			this.extTableModels = ret;
		}
		@Override
		public void run() {
			subscriptionService.checkTableModelIsValidInSub(userId, extTableModels);
		}
		
	}

	@RequestMapping(value = "/listPrivilegedTableModelsByTenant.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getPrivilegedTableModelsByTenant(HttpServletRequest request, 
			@RequestParam("tenantId") String tenantId) throws PrivilegeError, DataSharingMgrError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		Tenant tenant = dataSharingMgrService.getTenantById(tenantId);
		List<Map<String, Object> > ret = new ArrayList<>();
		
		Map<String, Set<ExtDomainPrivilege>> allGranted = dataSharingMgrService.getAllGrantedDomainPrivileges(PrivilegeCheckingHelper.getTenantId(session));
		if (allGranted.get(tenantId) == null)
			return ret;
		
		for (ExtDomainPrivilege granted : allGranted.get(tenantId)) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(granted.getObject());
			if (tableModel == null)
				continue;
			Map<String, Object> detail = new HashMap<>();
			detail.put("tableModel", tableModel);
			detail.put("privilegedColumns", getPrivilegedColumns(granted.getMask(), tableModel.getAttachedColumns().size()));
			detail.put("granteeName", tenant.getName());
			detail.put("granteeId", tenant.getTenantId());
			ret.add(detail);
		}
		return ret;
	}
	
	private List<Integer> getPrivilegedColumns(Integer mask, int columnsSize) {
		List<Integer> privilegedColumns = new ArrayList<>();
		if (mask == 0)
			return privilegedColumns;
		
		for (int i = 0; i < columnsSize; i++) {
			int check = (int) Math.pow(2, i);
			if ((check & mask) == check)
				privilegedColumns.add(i + 1);
		}
		
		return privilegedColumns;
	}
	
	@RequestMapping(value = "/listPrivilegedTableModelsByUser.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getPrivilegedTableModelsByUser(HttpServletRequest request, 
			@RequestParam("userId") String userId) throws PrivilegeError, DataSharingMgrError {
		HttpSession session = request.getSession();
		ExtUser user = dataSharingMgrService.getExtUserById(userId);
		List<Map<String, Object> > ret = new ArrayList<>();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session)) 
			throw new PrivilegeError(Constant.TenAdmin_Required);
		Map<String, Integer> allGranted = dataSharingMgrService.getPrivilegedTableModelIds(userId, PrivilegeCheckingHelper.getTenantId(session));
		for (String granted : allGranted.keySet()) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(granted);
			if (tableModel == null)
				continue;
			Map<String, Object> detail = new HashMap<>();
			detail.put("tableModel", tableModel);
			detail.put("privilegedColumns", getPrivilegedColumns(allGranted.get(granted), tableModel.getAttachedColumns().size()));
			detail.put("granteeName", user.getName());
			detail.put("granteeId", user.getUserId());
			detail.put("type", tableModel.getType());

			ret.add(detail);
		}
		return ret;
	}
	
	@RequestMapping(value = "/grantDomainPrivileges.post", method = RequestMethod.POST)
	public @ResponseBody String grantDoaminPrivileges(HttpServletRequest request, 
			@RequestParam("tableModelIds[]") List<String> tableModelIds, @RequestParam("granteeTenantIds[]") List<String> granteeTenantIds) 
			throws PrivilegeError, DataSharingMgrError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		for (String tableModelId : tableModelIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
			if (!tableModel.getTenantId().equals(PrivilegeCheckingHelper.getTenantId(session)))
				continue;
			for (String grantee : granteeTenantIds) {
				dataSharingMgrService.grantDomainPrivilege(PrivilegeCheckingHelper.getTenantId(session), grantee, tableModelId);
			}
		}
		return "ok";
	}
	
	@RequestMapping(value = "/revokeDomainPrivileges.post", method = RequestMethod.POST)
	public @ResponseBody String revokeDomainPrivilege(HttpServletRequest request, @RequestParam("tableModelIds") String tableModelIdsJson,
			@RequestParam("granteeTenantIds[]") List<String> granteeTenantIds) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Gson gson = new Gson();
		Map<String, List<Integer>> tableModelIds = gson.fromJson(tableModelIdsJson, Map.class);
		for (String tableModelId : tableModelIds.keySet()) {
			for (String granteeTenantId : granteeTenantIds) {
				dataSharingMgrService.revokePrivilege(PrivilegeCheckingHelper.getUserId(session), granteeTenantId, tableModelId);
			}
		}
		
		return "ok";
	}
	
	@RequestMapping(value = "/grantPrivileges.post", method = RequestMethod.POST)
	public @ResponseBody String grantPrivileges(HttpServletRequest request, 
			@RequestParam("tableModelIds[]") List<String> tableModelIds, @RequestParam("tenantUserIds[]") List<String> tenantUserIds) 
			throws PrivilegeError, DataSharingMgrError {
		HttpSession session = request.getSession();
		if (!PrivilegeCheckingHelper.isTenantAdmin(session))
			throw new PrivilegeError(Constant.TenAdmin_Required);
		
		for (String tableModelId : tableModelIds) {
			for (String userId : tenantUserIds) {
				dataSharingMgrService.grantPrivilege(PrivilegeCheckingHelper.getUserId(session), userId, tableModelId, PrivilegeCheckingHelper.getTenantId(session));
			}
		}
		return "ok";
	}
	
	@RequestMapping(value = "/revokePrivileges.post", method = RequestMethod.POST)
	public @ResponseBody String revokePrivilege(HttpServletRequest request, @RequestParam("tableModelIds") String tableModelIdsJson,
			@RequestParam("tenantUserIds[]") List<String> tenantUserIds) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Gson gson = new Gson();
		Map<String, List<Integer>> tableModelIds = gson.fromJson(tableModelIdsJson, Map.class);
		for (String tableModelId : tableModelIds.keySet()) {
			for (String tenantUserId : tenantUserIds) {
				dataSharingMgrService.revokePrivilege(PrivilegeCheckingHelper.getTenantId(session), tenantUserId, tableModelId);
			}
		}
	
		return "ok";
	}
	
	@RequestMapping(value = "/modelCatalogStats.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Map<String, Integer>> getModelCatalogStats(HttpServletRequest request) 
			throws DataSharingMgrError {
		Map<String, Map<String, Integer>> stats = new HashMap<>();
		
		List<Tenant> allTenants = dataSharingMgrService.getAllTenants();
		for (Tenant tenant : allTenants) {
			List<DBAddress> dbAddresses = dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId());
			for (DBAddress dbAddress : dbAddresses) {
				stats.put(dbAddress.getName(), dataSharingMgrService.getModelCatalogStats(dbAddress.getDbAddressId()));				
			}
		}
		return stats;
	}
	
	@RequestMapping(value = "/modelCatalogSummary.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getModelCatalogSummary(HttpServletRequest request) throws DataSharingMgrError {
		Map<String, Integer> stats = new HashMap<>();
		
		List<Tenant> allTenants = dataSharingMgrService.getAllTenants();
		for (Tenant tenant : allTenants) {
			List<DBAddress> dbAddresses = dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId());
			for (DBAddress dbAddress : dbAddresses) {
				Map<String, Integer> countByCatalog = dataSharingMgrService.getModelCatalogStats(dbAddress.getDbAddressId());
				for (String catalogName : countByCatalog.keySet()) {
					Integer count = stats.get(catalogName);
					if (count == null) {
						stats.put(catalogName, countByCatalog.get(catalogName));
					} else {
						stats.put(catalogName, count + countByCatalog.get(catalogName));
					}
				}
			}
		}
		return stats;
	}
	
	@RequestMapping(value = "/dataSourceSummary.load", method = RequestMethod.GET) 
	public @ResponseBody Map<String, Integer> getDataSourceSummary(HttpServletRequest request) throws DataSharingMgrError {
		Map<String, Integer> stats = new HashMap<>();
		
		List<Tenant> allTenants = dataSharingMgrService.getAllTenants();
		for (Tenant tenant : allTenants) {
			List<DBAddress> dbAddresses = dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId());
			for (DBAddress dbAddress : dbAddresses) {
				stats.put(dbAddress.getName(), dataSharingMgrService.getTableModelsCountByDBAddress(dbAddress.getDbAddressId()));
			}
		}
		return stats;
	}
	
	@RequestMapping(value = "/dataSourceSummaryByType.load", method = RequestMethod.GET) 
	public @ResponseBody Map<String, Integer> getDataSourceSummaryByType(HttpServletRequest request) throws DataSharingMgrError {
		Map<String, Integer> stats = new HashMap<>();
		
		List<Tenant> allTenants = dataSharingMgrService.getAllTenants();
		for (Tenant tenant : allTenants) {
			List<DBAddress> dbAddresses = dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId());
			for (DBAddress dbAddress : dbAddresses) {
				if (stats.get(dbAddress.getType()) == null)
					stats.put(dbAddress.getType(), 0);
				stats.put(dbAddress.getType(), 
						stats.get(dbAddress.getType()) + dataSharingMgrService.getTableModelsCountByDBAddress(dbAddress.getDbAddressId()));
			}
		}
		return stats;
	}
	
	/**
	 * 
	 * 数据访问情况月累计
	 * 
	 * @param request
	 * @param recentlyMonth
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/monthActiveResourceNumRecently.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getMonthActiveResourceNumRecently(HttpServletRequest request, @RequestParam Integer recentlyMonth) throws DataSharingMgrError {
		return logStatsService.getMonthActiveResourceNumRecently(recentlyMonth);
	}
	
	/**
	 * 
	 * 最近N天活跃的用户数统计
	 * 
	 * @param request
	 * @param recentlyDay
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/dayActiveUserNumRecently.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getDayActiveUserNumRecently(HttpServletRequest request, @RequestParam Integer recentlyDay) throws DataSharingMgrError {
		return logStatsService.getDayActiveUserNumRecently(recentlyDay);
	}
	
	/**
	 * 
	 * 最近N月活跃的用户数统计
	 * 
	 * @param request
	 * @param recentlyMonth
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/monthActiveUserNumRecently.load", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getMonthActiveUserNumRecently(HttpServletRequest request, @RequestParam Integer recentlyMonth) throws DataSharingMgrError {
		return logStatsService.getMonthActiveUserNumRecently(recentlyMonth);
	}
	
	
	/** 
	 * 按照月统计租户热门数据表
	 * 
	 * @param request
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/monthTenantResourceVisitedState.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Long> getTenantResourceVisitedStateByMonth(HttpServletRequest request) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		if (localizationService.isAdminVisitCount()) {
			return logStatsService.getAdminResourceVisitedState(10, calendar.getTime().getTime(), System.currentTimeMillis());
		} else {
			if (PrivilegeCheckingHelper.isSysAdmin(session)) {
				return logStatsService.getAdminResourceVisitedState(10, calendar.getTime().getTime(), System.currentTimeMillis());
			} else {
				String tenantId = PrivilegeCheckingHelper.getTenantId(session);
				return logStatsService.getTenantResourceVisitedState(tenantId, 10, calendar.getTime().getTime(), System.currentTimeMillis());
			}
		}
	}
	
	/**
	 * 按照年统计租户热门数据表
	 * 
	 * @param request
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/yearTenantResourceVisitedState.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Long> getTenantResourceVisitedStateByYear(HttpServletRequest request) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		if (localizationService.isAdminVisitCount()) {
			return logStatsService.getAdminResourceVisitedState(10, calendar.getTime().getTime(), System.currentTimeMillis());
		} else {
			if (PrivilegeCheckingHelper.isSysAdmin(session)) {
				return logStatsService.getAdminResourceVisitedState(10, calendar.getTime().getTime(), System.currentTimeMillis());
			} else {
				String tenantId = PrivilegeCheckingHelper.getTenantId(session);
				return logStatsService.getTenantResourceVisitedState(tenantId, 10, calendar.getTime().getTime(), System.currentTimeMillis());
			}
		}
	}
	
	
	/**
	 * 租户访问最多的资源按照月统计
	 * 
	 * @param request
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/monthTenantVisitResourceState.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Long> getTenantVisitResourceStateByMonth(HttpServletRequest request) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		String userId = PrivilegeCheckingHelper.getUserId(session);
		ExtUser user = dataSharingMgrService.getExtUserById(userId);
		Tenant tenant = dataSharingMgrService.getTenantById(user.getTenantId());
		return logStatsService.getTenantVisitResourceState(tenant.getTenantId(), 10, calendar.getTime().getTime(), System.currentTimeMillis());
	}
	
	/**
	 * 租户访问最多的资源按照年统计
	 * 
	 * @param request
	 * @return
	 * @throws DataSharingMgrError
	 */
	@RequestMapping(value = "/yearTenantVisitResourceState.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Long> getTenantVisitResourceStateByYear(HttpServletRequest request) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		String userId = PrivilegeCheckingHelper.getUserId(session);
		ExtUser user = dataSharingMgrService.getExtUserById(userId);
		Tenant tenant = dataSharingMgrService.getTenantById(user.getTenantId());
		return logStatsService.getTenantVisitResourceState(tenant.getTenantId(), 10, calendar.getTime().getTime(), System.currentTimeMillis());
	}
	
	@RequestMapping(value = "/yearDataVisitState.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getDataVisitStateByYear(HttpServletRequest request) throws DataSharingMgrError, RestInvokeException {
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		String userId = PrivilegeCheckingHelper.getUserId(session);
		String tenantId = PrivilegeCheckingHelper.getTenantId(session);
		Map<String, Integer> dataVisitState = new HashMap<>();
		long beginTime = calendar.getTimeInMillis();
		long endTime = System.currentTimeMillis();
		int pc_query_state = 0;
		int phone_query_state = 0;
		int subscribe_state = 0;
		if (PrivilegeCheckingHelper.isSysAdmin(session)) {
			pc_query_state = logStatsService.getAdminPcDataVisitState(beginTime, endTime);
			subscribe_state = subscriptionService.countAllSubTableModel();
			phone_query_state = logStatsService.getMobileDataVisitState(tenantId, beginTime, endTime);
		} else {
			pc_query_state = logStatsService.getPcDataVisitState(tenantId, beginTime, endTime);
			subscribe_state = subscriptionService.getUserSubTableModelds(userId).size();
			phone_query_state = logStatsService.getAdminMobileDataVisitState(beginTime, endTime);
		}
		dataVisitState.put("PC端的数据查看次数", pc_query_state/2); //查询的时候分页控件会默认发送两次请求,这里除以2
		dataVisitState.put("指标资源移动端查看数据次数", phone_query_state);
		dataVisitState.put("指标资源订阅数", subscribe_state);
		return dataVisitState;
	}
	
	// todo: counting by actor's role
	@RequestMapping(value = "/dataResourceSummary.load", method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getDataResourceSummary(HttpServletRequest request) throws DataSharingMgrError, RestInvokeException {
		return dataSharingMgrService.countTableModelsByType(Arrays.asList("kpi", "analyticalModel", "db"));
	}
	
	@RequestMapping(value = "/downloadResources.load", method = RequestMethod.GET)
	public @ResponseBody List<Document> getDownloadResources(HttpServletRequest request) throws DataSharingMgrError {
		return homePageService.listDocs();
	}
	
	@RequestMapping(value = "/notices.load", method = RequestMethod.GET)
	public @ResponseBody List<Notice> getNotices(HttpServletRequest request) throws DataSharingMgrError {
		return homePageService.listNotices();
	}
	
	@RequestMapping(value = "/favoriteIndicatorValues.load", method = RequestMethod.GET)
	public @ResponseBody List<FavoriteIndicator> getFavoriteIndicatorValues(HttpServletRequest request) throws RestInvokeException, DataSharingMgrError  {
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession())) {
			return subscriptionService.getDefaultFavoriteIndicatorValues(); 
		} else {
			return subscriptionService.getUserFavoriteIndicatorValues(PrivilegeCheckingHelper.getUserId(request.getSession()));
		}
	}
	
	@RequestMapping(value = "/searchTableModels.load", method = RequestMethod.GET)
	public @ResponseBody List<ExtTableModel> searchTableModels(HttpServletRequest request, @RequestParam String query) throws DataSharingMgrError {	
		List<TableModel> tableModels = tableModelFullTextSearchService.searchTableModelsByName(query);
		List<String> tableModelIds = new ArrayList<>();
		tableModels.forEach(t -> tableModelIds.add(t.getTableModelId()));
		Set<String> accessible 
			= dataSharingMgrService.checkTableModelsAccessible(tableModelIds, PrivilegeCheckingHelper.getUserId(request.getSession()));
		List<ExtTableModel> ret = new ArrayList<>();
		for (TableModel t : tableModels) {
			ExtTableModel extended = new ExtTableModel(t, accessible.contains(t.getTableModelId()));
			ret.add(extended);
			if (t.getType().equals("kpi")) {
				extended.setMisc(kpiFullTextSearchService.getKpiDefByNameAndCatalog(t.getName(), t.getNameInSource().split("\\.")[0]));
			}
		}
						
		return ret;
	}
	
	@RequestMapping(value = "/searchMyTableModels.load", method = RequestMethod.GET)
	public @ResponseBody List<ExtTableModel> searchTableModelsWithPrivilegesCheck(HttpServletRequest request, @RequestParam String query) throws DataSharingMgrError {	
		List<TableModel> tableModels = tableModelFullTextSearchService.searchTableModelsByName(query);
		List<String> tableModelIds = new ArrayList<>();
		tableModels.forEach(t -> tableModelIds.add(t.getTableModelId()));
		Set<String> accessible 
			= dataSharingMgrService.checkTableModelsAccessible(tableModelIds, PrivilegeCheckingHelper.getUserId(request.getSession()));
		List<ExtTableModel> ret = new ArrayList<>();
		for (TableModel t : tableModels) {
			if (accessible.contains(t.getTableModelId())) {
				ExtTableModel extended = new ExtTableModel(t, true);
				ret.add(extended);
				if (t.getType().equals("kpi")) 
					extended.setMisc(kpiFullTextSearchService.getKpiDefByNameAndCatalog(t.getName(), t.getNameInSource().split("\\.")[0]));
			}
		}
						
		return ret;
	}
	
	// rename
	@RequestMapping(value = "/getKpiTableModelByPath.load", method = RequestMethod.GET)
	public @ResponseBody List<ExtTableModel> getKPIsByPath(HttpServletRequest request, @RequestParam("path[]") String[] path) throws DataSharingMgrError, IOException {	
		List<TableModel> tableModels = tableModelFullTextSearchService.searchTableModelsByPath(String.join("/", path));
		List<String> tableModelIds = new ArrayList<>();
		tableModels.forEach(t -> tableModelIds.add(t.getTableModelId()));
		Set<String> accessible 
			= dataSharingMgrService.checkTableModelsAccessible(tableModelIds, PrivilegeCheckingHelper.getUserId(request.getSession()));
		List<ExtTableModel> ret = new ArrayList<>();
		tableModels.forEach(t -> ret.add(new ExtTableModel(t, accessible.contains(t.getTableModelId()))));
				
		return ret;
	}
	
	@RequestMapping(value = "/descKPIsRecursively.load", method = RequestMethod.GET)
	public @ResponseBody List<KpiDoc> descKpiByPathRecursively(HttpServletRequest request, @RequestParam("path[]") String[] path) throws DataSharingMgrError, IOException {	
		return kpiFullTextSearchService.searchKpiDefsByCategory(String.join("/", path));
	}
	
	@RequestMapping(value = "/descAnalyticalModelsRecursively.load", method = RequestMethod.GET)
	public @ResponseBody List<AnalyticalModelDoc> descAnalyticalModelsRecursively(HttpServletRequest request, @RequestParam("path[]") String[] path) throws DataSharingMgrError, IOException {	
		return kpiFullTextSearchService.searchAnalyticalModelDefsByCategory(String.join("/", path));
	}
	
	@RequestMapping(value = "/kpiCategories.load", method = RequestMethod.GET)
	public @ResponseBody JsonNode[] getKpiCategories(HttpServletRequest request) throws IOException, DataSharingMgrError {
		JsonNode root = JsonTreeHelper.toJsonTree(kpiFullTextSearchService.getKpiCategories());
		return root.nodes;
	}
	
	@RequestMapping(value = "/anlyticalModelCategories.load", method = RequestMethod.GET)
	public @ResponseBody JsonNode[] getAnlyticalModelCategories(HttpServletRequest request) throws IOException, DataSharingMgrError {
		JsonNode root = JsonTreeHelper.toJsonTree(kpiFullTextSearchService.getAnalyticalModelCategories());
		return root.nodes;
	}
	
	@RequestMapping(value = "/tableModelRefKpiDef.load", method = RequestMethod.GET)
	public @ResponseBody String getKpiDocByNameAndCatalog(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId) throws DataSharingMgrError {
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		if (tableModel != null)
			return kpiFullTextSearchService.getKpiDefByNameAndCatalog(tableModel.getName(), tableModel.getNameInSource().split("\\.")[0]);
		else
			return "";
	}
	
	@RequestMapping("authority")
	public String authority(HttpServletRequest request, Model model) {
		model.addAttribute("root", kRoot);
		return "service-center/authority";
	}
	
	@RequestMapping("/tableModelSampleData.load")
	public @ResponseBody Map<String, Object> queryDataSharingDB(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId) 
			throws SQLException, DataSharingMgrError, PrivilegeError {
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		StringBuilder sb = createAliasSelectSql(tableModel.getTableModelId());
		String[] credential = getDataSharingDBCredential(request.getSession());
		String sql = sb.toString();
		String tenantId = PrivilegeCheckingHelper.getTenantId(request.getSession());
		logStatsService.indexPcQueryState(tenantId, tableModel.getName());
		return dataSharingDBService.executeQueryByPagination(sql, credential[0], credential[1], 0, SampleSize);
	}
	
	@GetMapping("/queryDataSharingDbByTableModelId.load")
	public @ResponseBody Map<String, Object> queryDataSharingDB(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId, 
			@RequestParam("startIndex") Integer startIndex, @RequestParam("pageSize") Integer pageSize) throws SQLException, DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());
		if (!tableModel.getType().equals("db")) {
			StringBuilder sb = createAliasSelectSql(tableModel.getTableModelId());
			String[] credential = getDataSharingDBCredential(session);
			String sql = sb.toString();
			String tenantId = PrivilegeCheckingHelper.getTenantId(session);
			logStatsService.indexPcQueryState(tenantId, tableModel.getName());
			return dataSharingDBService.executeQueryByPagination(sql, credential[0], credential[1], startIndex, pageSize);
		}else {
			return queryDataSharingDB(session, dbAddress.getName(), tableModel.getName(), startIndex, pageSize);
		}
	}

	private StringBuilder createAliasSelectSql(String tableModelId) throws DataSharingMgrError {
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());
		StringBuilder sb = new StringBuilder("select ");
		List<String> selectColumn = new ArrayList<>();
		for (TableModelColumn column : tableModel.getAttachedColumns()) {
			if (isCountable(column.getSqlDataType()) && !StringUtils.isEmpty(tableModel.getUnit())) {
				selectColumn.add("\""+ column.getName() +"\" as " + "\""+ column.getName()+"("+tableModel.getUnit()+")\"");
			}else {
				selectColumn.add("\""+ column.getName() +"\" as " + "\""+ column.getName() +"\"");
			}
		}
		sb.append(String.join(",", selectColumn));
		sb.append(" from ");
		sb.append(dbAddress.getName() + "." + tableModel.getName());
		return sb;
	}
	
	@GetMapping("/groupByList.load")
	public @ResponseBody List<String> groupBy(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId) 
			throws DataSharingMgrError {
		List<String> groupByList = new ArrayList<String>();
		List<String> countableList = new ArrayList<String>();
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		for (TableModelColumn column : tableModel.getAttachedColumns()) {
			if (isCountable(column.getSqlDataType())) 
				countableList.add(column.getName());
			else	
				groupByList.add(column.getName());
		}
		
		return countableList.size() == 0 ? new ArrayList<>() : groupByList;
	}
	
	private boolean isCountable(int sqlDataType) {
		if (sqlDataType == Types.BIGINT || sqlDataType == Types.DECIMAL || sqlDataType == Types.DOUBLE 
				|| sqlDataType == Types.FLOAT || sqlDataType == Types.INTEGER || sqlDataType == Types.NUMERIC
					|| sqlDataType == Types.REAL || sqlDataType == Types.SMALLINT || sqlDataType == Types.TINYINT) {
			return true;
		} else {
			return false;
		}
	}
	
	@GetMapping("/getGroupingSumByTableModelId.load")
	public @ResponseBody Map<String, Object> queryDataSharingDB(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId, 
			@RequestParam("groupByList") List<String> groupByList, @RequestParam("startIndex") Integer startIndex, @RequestParam("pageSize") Integer pageSize) throws SQLException, DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		String tenantId = PrivilegeCheckingHelper.getTenantId(session);		
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());
		
		List<String> countableList = new ArrayList<>();
		for (TableModelColumn column : tableModel.getAttachedColumns()) {
			if (isCountable(column.getSqlDataType())) {
				String sumAliaName = column.getName();
				if (!StringUtils.isEmpty(tableModel.getUnit())) sumAliaName += "("+tableModel.getUnit()+")";
				countableList.add("sum(\"" + column.getName() + "\") as \"" + sumAliaName + "(sum)" + "\"");
			}
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append("select ");
		sb.append(String.join(",", groupByList.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList())));
		sb.append(",");
		sb.append(String.join(",", countableList));
		sb.append(" from ");
		sb.append(dbAddress.getName() + "." + tableModel.getName());
		sb.append(" group by ");
		sb.append(String.join(",", groupByList.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList())));
		
		String[] credential = getDataSharingDBCredential(session);
		String sql = sb.toString();
		logStatsService.indexPcQueryState(tenantId, tableModel.getName());
		return dataSharingDBService.executeQueryByPagination(sql, credential[0], credential[1], startIndex, pageSize);
	}
	
	
	private String[] getDataSharingDBCredential(HttpSession session) throws DataSharingMgrError {
		User portalUser = (User) session.getAttribute(Constant.Session_Keymobile_SSO);
		String[] credential = new String[2];
		
		if (dataSharingDBService.getIsUsePortalUser().equals("false")) {
			String userId = PrivilegeCheckingHelper.getUserId(session);
			ExtUser extUser = dataSharingMgrService.getExtUserById(userId);
			credential[0] = extUser.getName() +"." + PrivilegeCheckingHelper.getTenantName(session);
			credential[1] = extUser.getPassword();
		}
		else {
			credential[0] = portalUser.getUserId();
			credential[1] = portalUser.getPassword();
		}
		
		return credential;
	}
	
	private Map<String, Object> queryDataSharingDB(HttpSession session, String schema, String table, Integer startIndex, 
			Integer pageSize) throws SQLException, DataSharingMgrError {
		String tenantId = PrivilegeCheckingHelper.getTenantId(session);		
		String[] credential = getDataSharingDBCredential(session);
		
		Map<String, Object> map = dataSharingDBService.executeQueryByPagination(schema, table, 
				credential[0], credential[1], startIndex, pageSize);
		logger.debug("schema={" + schema + "}, table={"+ table +"}, userName={" + credential[0] + "}, pwd={" + credential[1] + "}");
		logStatsService.indexPcQueryState(tenantId, table);
		return map;
	}
	
	@GetMapping("/queryPrivilegeKpiAsTree.load")
	public @ResponseBody JsonNode[] queryPrivilegesTableModelsAsTree(HttpServletRequest request) throws DataSharingMgrError, IllegalAccessException, InvocationTargetException, PrivilegeError {
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession()))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		List<ExtTableModel> TableModel = getPrivielgedTableModels(request);
		JsonNode[] jsonNode = null;
		jsonNode = dataQueryService.queryPrivilegeKpiAsTree(TableModel).nodes;
		return jsonNode == null ? new JsonNode[]{} : jsonNode;
	}
	
	@GetMapping("/queryPrivilegeAnalyticalModelAsTree.load")
	public @ResponseBody JsonNode[] queryPrivilegeAnalyticalModelAsTree(HttpServletRequest request) throws DataSharingMgrError, IllegalAccessException, InvocationTargetException, PrivilegeError {
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession()))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		List<ExtTableModel> TableModel =  getPrivielgedTableModels(request);
		JsonNode[] jsonNode = null;
		jsonNode = dataQueryService.queryPrivilegeAnalyticalModelAsTree(TableModel).nodes;
		return jsonNode == null ? new JsonNode[]{} : jsonNode;
	}
	
	@GetMapping("/queryResource.load")
	public @ResponseBody Map<String, Object> queryResource(HttpServletRequest request) throws DataSharingMgrError, IllegalAccessException, InvocationTargetException, PrivilegeError {
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession()))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		return showDataSources(request, true, true); 
	}
	
	@GetMapping("/quickViewResource.load")
	public @ResponseBody Map<String, Object> quickViewResource(HttpServletRequest request) throws PrivilegeError, DataSharingMgrError {
		if (PrivilegeCheckingHelper.isSysAdmin(request.getSession()))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Attr> attrList  = new ArrayList<Attr>();
		for (Attr attr : AttributeUtil.GetAttributes(DataSource.class)) {
			if (!"dbAddressId".equals(attr.attr) && !"dbPwd".equals(attr.attr))
				attrList.add(attr);
		}
		data.put("attributes", attrList);
		
		List<DataSource> dataSources = new ArrayList<DataSource>();
		List<Tenant> allTenants = new ArrayList<Tenant>();
		allTenants.addAll(dataSharingMgrService.getAllTenants());
		
		for (Tenant tenant : allTenants) {
			for(DBAddress dbAddress: dataSharingMgrService.getDBAddressesByTenant(tenant.getTenantId())) {
				if (dbAddress.getType().equals("kpi")) 
					dataSources.add(new DataSource(dbAddress, tenant.getName()));				
			}
		}
		data.put("items", dataSources);
		return data;		
	}
	
	@GetMapping(value = "/queryPrivilegeTableModels.load")
	public @ResponseBody List<TableModel> queryPrivilegeTableModels(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId) 
		throws DataSharingMgrError, PrivilegeError, IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		if (PrivilegeCheckingHelper.isSysAdmin(session))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		List<TableModel> dbTableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddressId);
		List<ExtTableModel> privilegeTableModels = getPrivielgedTableModels(request);
		List<TableModel> tableModels = new ArrayList<>();
		for (TableModel tableModel : dbTableModels) {
			if (!tableModel.getType().equals("db") || !tableModel.isEnabled()) 
				continue;
			for (TableModel privilegeTable : privilegeTableModels) {
				if (privilegeTable.getTableModelId().equals(tableModel.getTableModelId())) {
					tableModels.add(tableModel);
					break;
				}
			}
		}
		return tableModels;
	}
	
	@GetMapping(value = "/queryPrivilegeQuickViews.load")
	public @ResponseBody List<TableModel> queryPrivilegeQuickViews(HttpServletRequest request, @RequestParam("dbAddressId") String dbAddressId) 
		throws DataSharingMgrError, PrivilegeError, IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		if (PrivilegeCheckingHelper.isSysAdmin(session))
			throw new PrivilegeError(Constant.User_Or_TenAdmin_Required);
		
		List<TableModel> dbTableModels = dataSharingMgrService.getTableModelsByDBAddress(dbAddressId);
		List<ExtTableModel> privilegeTableModels = getPrivielgedTableModels(request);
		List<TableModel> tableModels = new ArrayList<>();
		for (TableModel tableModel : dbTableModels) {
			if (!tableModel.getType().equals(TableModelTypes.QUICK) || !tableModel.isEnabled()) 
				continue;
			for (TableModel privilegeTable : privilegeTableModels) {
				if (privilegeTable.getTableModelId().equals(tableModel.getTableModelId())) {
					tableModels.add(tableModel);
					break;
				}
			}
		}
		return tableModels;
	}
	
	@RequestMapping(value = "/tableModels.load", method = RequestMethod.GET)
	public @ResponseBody List<TableModel> getTableModelsByIds(HttpServletRequest request, @RequestParam("tableModelIds") List<String> tableModelIds) 
			throws DataSharingMgrError {
		List<TableModel> tableModels = new ArrayList<>();
		for (String tModelId : tableModelIds) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tModelId);
			if (tableModel != null) tableModels.add(tableModel);
		}
		return tableModels;
	}
	
	@RequestMapping(value = "/saveQuickView.post", method = RequestMethod.POST) 
	public @ResponseBody String saveQuickView(HttpServletRequest request, @RequestParam("tableModelIds[]") List<String> tableModelIds, 
			@RequestParam("quickViewName") String quickViewName, @RequestParam("remark") String remark) throws DataSharingMgrError {
		HttpSession session = request.getSession();
		DBAddress kpiDBAddress = null;
		for (DBAddress dbAddress : dataSharingMgrService.getDBAddressesByTenant(PrivilegeCheckingHelper.getTenantId(session))) {
			if (dbAddress.getType().equals("kpi"))
				kpiDBAddress = dbAddress;
		}
		
		if (kpiDBAddress !=  null) {
			TableModel tableModel = new TableModel(genQuickViewSql(tableModelIds), TableModelTypes.QUICK);
			tableModel.setName(quickViewName);
			tableModel.setRemarks(remark);
			tableModel = dataSharingMgrService.addTableModel(tableModel, kpiDBAddress.getDbAddressId());
			dataSharingMgrService.enableTableModel(tableModel.getTableModelId());
			ModelCatalog defaultCatalog = dataSharingMgrService.getModelCatalogByName(Default_CatalogName, kpiDBAddress.getDbAddressId());
			if (defaultCatalog != null)
				dataSharingMgrService.addTableModelIntoCatalog(tableModel.getTableModelId(), defaultCatalog.getModelCatalogId());
			tableModelFullTextSearchService.indexTableModel(tableModel);
		}
			
		return "ok";
	}
	
	@RequestMapping(value = "/testQuickViewBeforeSave.post", method = RequestMethod.POST) 
	public @ResponseBody Map<String, String> testQuickView(HttpServletRequest request, @RequestParam("tableModelIds[]") List<String> tableModelIds) 
			throws DataSharingMgrError, SQLException {	
		String[] credential = getDataSharingDBCredential(request.getSession());
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, Object> result = dataSharingDBService.executeQueryByPagination(genQuickViewSql(tableModelIds), credential[0], credential[1], 0, tableModelIds.size() * 2);
		List<List<String>> rows = (List<List<String>>) result.get("data");
		for (int i = 0; i < rows.size(); i = i + 2) {
			String key = rows.get(i).get(0);
			String value = rows.get(i + 1).get(0);
			
			ret.put(key, value);
		}
		
		return ret;
	}
	
	private String genQuickViewSql(List<String> tableModelIds) throws DataSharingMgrError {
		List<String> pSql = new ArrayList<String>();
		for (int i = 0; i < tableModelIds.size(); i++) {
			TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelIds.get(i));
			DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());			
			StringBuilder sb = new StringBuilder();

			List<String> countableList = new ArrayList<>();
			for (TableModelColumn column : tableModel.getAttachedColumns()) {
				if (isCountable(column.getSqlDataType())) {
					if (StringUtils.isEmpty(tableModel.getUnit()))  
						countableList.add("sum(\"" + column.getName() + "\")");
					else
						countableList.add("sum(\"" + column.getName() + "\") || '" + tableModel.getUnit() + "'");
				}
			}
			
			if (countableList.size() >= 1) {
				sb.append("select '" + tableModel.getName() + "' ");
				sb.append(" union ");
				sb.append("select ").append(countableList.get(0));
				sb.append(" from ");
				sb.append(dbAddress.getName() + "." + tableModel.getName());
			
				pSql.add("select * from (" + sb.toString() + ") t" + i);
			}
		}
		
		String sql = String.join(" union ", pSql);
		return sql;
	}
	
	@GetMapping("/queryQuickViewById.load")
	public @ResponseBody Map<String, String> queryQuickViewById(HttpServletRequest request, @RequestParam("tableModelId") String tableModelId) 
			throws SQLException, DataSharingMgrError, PrivilegeError {
		HttpSession session = request.getSession();
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		DBAddress dbAddress = dataSharingMgrService.getDBAddressById(tableModel.getDBAddressId());
		
		int lastIndex = 0;
		int count = 0;
		while(lastIndex != -1) {
		    lastIndex = tableModel.getSQL().indexOf("select", lastIndex);
		    if(lastIndex != -1) {
		        count ++;
		        lastIndex += "select".length();
		    }
		}
		
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, Object> result = queryDataSharingDB(session, dbAddress.getName(), tableModel.getName(), 0, count / 3 * 2);
		
		List<List<String>> rows = (List<List<String>>) result.get("data");
		for (int i = 0; i < rows.size(); i = i + 2) {
			String key = rows.get(i).get(0);
			String value = rows.get(i + 1).get(0);
			
			ret.put(key, value);
		}
		
		return ret;
	}
	
}
