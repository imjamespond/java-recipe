package com.metasoft.service.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.DBAddress;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.keymobile.dataSharingMgr.interfaces.security.ExtDomainPrivilege;
import com.keymobile.dataSharingMgr.interfaces.security.ExtUser;
import com.metasoft.model.GenericDao;
import com.metasoft.model.dao.SubUserTableModelDao;
import com.metasoft.model.exception.RestInvokeException;
import com.metasoft.service.DaoHelperFactory;
import com.metasoft.util.DBType;
import com.metasoft.util.GenericDaoService;
import com.metasoft.util.HttpClientUtils;

@Service
public class SubUserTableModelDaoService extends GenericDaoService<SubUserTableModelDao> {
	
	static Logger log = LoggerFactory.getLogger(SubUserTableModelDaoService.class);
	
	private static final int SELECT_FAVOURITE_MAX_NUM = 6;
	
	private static final String CYCLE_DAY = "1";
	private static final String CYCLE_MONTH = "2";
	private static final String CYCLE_WEEK = "3";
	private static final String CYCLE_QUARTER = "4";
	private static final String CYCLE_HALFYEAR = "5";
	private static final String CYCLE_YEAR = "6";
	
	@Autowired
	private DaoHelperFactory daoHelperFactory;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;
	
	@Value("${indicatorServiceUrl.url}")
	private String indicatorServiceUrl;
	
	private DBType dbType;

	public SubUserTableModelDaoService(){
		
	}

	public void init(){
		super.init();
		this.dbType = daoHelperFactory.getDBType();
	}
	
	public void subscribeTableModel(String userId, TableModel tableModel) throws RestInvokeException {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("tmodelId", tableModel.getTableModelId());
		paramMap.put("tmodelName", tableModel.getName());
		paramMap.put("dataCycle", tableModel.getCycle());
		Object[] args = {userId, tableModel.getTableModelId(), tableModel.getName(), tableModel.getCycle(), 0};
		this.getJdbcTemplate().update("insert into "+this.kTable+" (USER_ID,TMODEL_ID,TMODEL_NAME,DATA_CYCLE,SELECT_FLAG) values(?,?,?,?,?)", args);
	}
	
	public void unSubscribeTableModel(String userId, String tableModelId) throws RestInvokeException {
		final String sql = "delete from "+this.kTable+" where USER_ID=? and TMODEL_ID=?";
		Object[] args = {userId, tableModelId};
		this.getJdbcTemplate().update(sql, args);
	}
	
	

	public Map<String, SubUserTableModelDao> getUserSubTableModelds(String userId) {
		Object[] objs = {userId};
		List<GenericDao> daos = this.select(" where USER_ID = ?", objs);
		Map<String, SubUserTableModelDao> userTModels = new HashMap<>();
		for (GenericDao dao : daos) {
			SubUserTableModelDao aoDao = (SubUserTableModelDao) dao;
			userTModels.put(aoDao.getTmodelId(), aoDao);
		}
		return userTModels;
	}

	public void checkTableModelIsValidInSub(String userId, List<TableModel> tableModels) {
		Set<String> subTmodelIds = getUserSubTableModelds(userId).keySet();
		List<String> privilegeTmodelIds = new ArrayList<>();
		for (TableModel extTableModel : tableModels) {
			privilegeTmodelIds.add(extTableModel.getTableModelId());
		}
		String batchDeleteSql = "delete from  "+this.kTable+" where USER_ID =:userId and  TMODEL_ID=:tmodelId";
		List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
		for (String subTmodelId : subTmodelIds) {
			if (!privilegeTmodelIds.contains(subTmodelId)) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("userId", userId);
				param.addValue("tmodelId", subTmodelId);
				params.add(param);
			}
		}
		this.getNamedParameterJdbcTemplate().batchUpdate(batchDeleteSql, params.toArray(new SqlParameterSource[0]));
	}

	public int countAllSubTableModel() {
		return this.getJdbcTemplate().queryForObject("select count(1) from "+ this.kTable, Integer.class);
	}

	public List<FavoriteIndicator> getDefaultFavoriteIndicatorValues() {
		return (List<FavoriteIndicator>) this.getJdbcTemplate().query("select * from SUB_FAVORITE_INDICATOR_ORIGIN order by id", new FavoriteIndicator());
	}
	
	public static  class FavoriteIndicator implements RowMapper<FavoriteIndicator>, Serializable {
		private static final long serialVersionUID = 1L;
		public String cycle;
		public String date;
		public String name;
		public String unit;
		public String value;
		public String type; // amount area peoplenum percentage
		@Override
		public FavoriteIndicator mapRow(ResultSet rs, int rowNum) throws SQLException {
			FavoriteIndicator favoriteIndicator = new FavoriteIndicator();
			favoriteIndicator.cycle = rs.getString("cycle");
			favoriteIndicator.date = rs.getString("date");
			favoriteIndicator.name = rs.getString("name");
			favoriteIndicator.unit = rs.getString("unit");
			favoriteIndicator.value = rs.getString("value");
			favoriteIndicator.type = rs.getString("type");
			return favoriteIndicator;
		}
		
	}

	public List<FavoriteIndicator> getUserFavoriteIndicatorValues(String userId) throws RestInvokeException, DataSharingMgrError {
		List<String> kpiPaths = new ArrayList<>();
		Map<String, TableModel> privilegeTmodels = getPrivilegeTmodels(userId);// check privilege
		Object[] objs = {userId};
		List<GenericDao> daos = this.select(" where USER_ID = ? AND SELECT_FLAG = 1 ", objs);
		List<String> selectTmodelIds = new ArrayList<String>();
		for (GenericDao dao : daos) {
			SubUserTableModelDao aoDao = (SubUserTableModelDao) dao;
			selectTmodelIds.add(aoDao.getTmodelId());
		}
		for (String tableModelId : selectTmodelIds) {
			if (privilegeTmodels.get(tableModelId) == null) continue;
			TableModel tableModel = privilegeTmodels.get(tableModelId);
			if (tableModel != null) {
				String kpiPath = tableModel.getNameInSource().replace(".", "/").replace("\"", "");
				kpiPaths.add(kpiPath);
			}
		}
		String url = indicatorServiceUrl+"/resource/favorites";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String favKpiPath =  kpiPaths.toString().replace("[", "").replace("]", "");
		paramMap.put("fvts", favKpiPath);
		String returnData = HttpClientUtils.sendGetRequest(url, paramMap);
		List<FavoriteIndicator> favoriteIndicators = new Gson().fromJson(returnData, new TypeToken<List<FavoriteIndicator>>(){}.getType());
		
		for (FavoriteIndicator favoriteIndicator : favoriteIndicators) {
			if (StringUtils.isEmpty(favoriteIndicator.type)) favoriteIndicator.type = "amount";
		}
		
		if (favoriteIndicators.size() == SELECT_FAVOURITE_MAX_NUM) return favoriteIndicators;
		List<FavoriteIndicator> favoriteIndicators_origin = getDefaultFavoriteIndicatorValues();
		if (favoriteIndicators_origin.size() == 0 ) throw new RuntimeException("没有默认的精选指标");
		int originFavIndex = 0;
		for (int index = favoriteIndicators.size(); index < SELECT_FAVOURITE_MAX_NUM; index ++) {
			favoriteIndicators.add(favoriteIndicators_origin.get(originFavIndex));
			originFavIndex ++;
		}
		return favoriteIndicators;
	}
	
	public void selectSubscribeTableModel(String userId, String tmodelId) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("tmodelId", tmodelId);
		String updateSQL = "update "+this.kTable+ " set SELECT_FLAG = 1 where USER_ID=:userId  and  TMODEL_ID=:tmodelId";
		this.getNamedParameterJdbcTemplate().update(updateSQL, param);
	}

	public void unSelectSubscribeTableModel(String userId, String tmodelId) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("tmodelId", tmodelId);
		String updateSQL = "update "+this.kTable+ " set SELECT_FLAG = 0 where USER_ID=:userId  and  TMODEL_ID=:tmodelId";
		this.getNamedParameterJdbcTemplate().update(updateSQL, param);
	}
	
	private String getKpiPathByTModelId(String tableModelId) throws DataSharingMgrError{
		String kpiPath = "";
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		if (tableModel != null) kpiPath = tableModel.getNameInSource().replace(".", "/").replace("\"", "");
		return kpiPath;
	}

	public List<Map<String, Object>> getIndicatorsByCycle(String userId, String cycle) throws DataSharingMgrError {
		String querySQL = "select * from "+kTable + " where USER_ID=?  and  DATA_CYCLE=?";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("cycle", cycle);
		 List<SubUserTableModelDao> modelDaos = this.getJdbcTemplate().query(querySQL, new Object[]{userId, cycle}, new SubUserTableModelDao());
		Map<String, TableModel> privilegeTmodels = getPrivilegeTmodels(userId);
		List<Map<String, Object>> indicators = new ArrayList<Map<String, Object>>();
		for (SubUserTableModelDao modelDao : modelDaos) {
			if (privilegeTmodels.get(modelDao.getTmodelId()) == null) continue;
			Map<String, Object> indicator = new HashMap<String, Object>();
			TableModel tableModel = privilegeTmodels.get(modelDao.getTmodelId());
			indicator.put("id", tableModel.getTableModelId());
//			indicator.put("id", tableModel.getNameInSource().replace(".", "/").replace("\"", ""));
			indicator.put("name", tableModel.getName());
			indicators.add(indicator);
		}
		return indicators;
	}
	
	private Map<String, TableModel> getPrivilegeTmodels(String userId) throws DataSharingMgrError {
		ExtUser extUser = dataSharingMgrService.getExtUserById(userId);
		String tenantId = extUser.getTenantId();
		Map<String, TableModel> privilegeTmodels = new HashMap<String, TableModel>();
		if (extUser.getACL() == 1) {
			Map<String, ExtDomainPrivilege> domainPrivileges = dataSharingMgrService.getAllDomainPrivilege(tenantId);
			for (String tableModelId : domainPrivileges.keySet()) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null) 
					privilegeTmodels.put(tableModelId, tableModel);
			}
			for (DBAddress dbAddress : dataSharingMgrService.getDBAddressesByTenant(tenantId)) {
				for (TableModel tableModel : dataSharingMgrService.getTableModelsByDBAddress(dbAddress.getDbAddressId())) {
					privilegeTmodels.put(tableModel.getTableModelId(), tableModel);
				}
			}
		} else {
			Set<String> privilegedTableModelIds 
				= dataSharingMgrService.getPrivilegedTableModelIds(userId, tenantId).keySet();
			for (String tableModelId : privilegedTableModelIds) {
				TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				if (tableModel != null)
					privilegeTmodels.put(tableModelId, tableModel);	
			}
		}
		return privilegeTmodels;
	}
	
	public String getDateParamValue(String cycle){
		Calendar c = Calendar.getInstance();
		if(CYCLE_DAY.equals(cycle)){//日
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
			return fmt.format(c.getTime());
		}else if(CYCLE_MONTH.equals(cycle)){//月
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM");
			return fmt.format(c.getTime());
		}else if(CYCLE_WEEK.equals(cycle)){//周
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
			return fmt.format(c.getTime());
		}else if(CYCLE_QUARTER.equals(cycle)){//季度
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy");
			int month = c.get(Calendar.MONTH);
			return fmt.format(c.getTime()) + (month/3 + 1);
		}else if(CYCLE_HALFYEAR.equals(cycle)){//半年
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy");
			int month = c.get(Calendar.MONTH);
			return fmt.format(c.getTime()) + (month/6 + 1);
		}else if(CYCLE_YEAR.equals(cycle)){//年
			SimpleDateFormat fmt=new SimpleDateFormat("yyyy");
			return fmt.format(c.getTime());
		}
		return "";
	}

	public Map<String, Object> getIndicatorDetailData(String tableModelId, String date, String cycle, String area) throws RestInvokeException, DataSharingMgrError {
		// kpiPath this is a tableModelId
		String kpiPath = getKpiPathByTModelId(tableModelId);
		String restbydateUrl = indicatorServiceUrl+"/resource/restbydate";
		Map<String, Object> restByDateparamMap = new HashMap<String, Object>();
		restByDateparamMap.put("kpi", kpiPath);
		//userinfoutils.getUserInfo 销售 人数 面积
		restByDateparamMap.put("areas", area);
		restByDateparamMap.put("kpidate", date);
		String bydateStr = HttpClientUtils.sendGetRequest(restbydateUrl, restByDateparamMap);
		List<String[]> restByDates = new Gson().fromJson(bydateStr, new TypeToken<List<String[]>>(){}.getType());
//		List<String[]> restByDates = new ArrayList<String[]>(){
//			{
//				add(new String[]{"11", "12"});
//				add(new String[]{"21", "22"});
//			}
//		};
		String fieldsUrl = indicatorServiceUrl+"/resource/rest/fields";
		Map<String, Object> kpiFieldParamMap = new HashMap<String, Object>();
		kpiFieldParamMap.put("kpi", kpiPath);
		String fieldStr = HttpClientUtils.sendGetRequest(fieldsUrl, kpiFieldParamMap);
		List<kpiField> fieldDatas = new Gson().fromJson(fieldStr, new TypeToken<List<kpiField>>(){}.getType());
//		List<kpiField> fieldDatas = new ArrayList<kpiField>(){
//			{
//				add(new kpiField("列1", ""));
//				add(new kpiField("列2", ""));
//			}
//		};
		Map<String, Object> map = new HashMap<String, Object>();
		TableModel tableModel = dataSharingMgrService.getTableModelById(tableModelId);
		map.put("heading", getHeadings(fieldDatas, tableModel.getUnit()));
		map.put("data", restByDates.toArray());
		return map;
	}
	
	public List<String> getDistinctDate(String tableModelId, String parentName, String cycle, int level) throws RestInvokeException, DataSharingMgrError {
//		resource/distinbydate
		String kpiPath = getKpiPathByTModelId(tableModelId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String url = indicatorServiceUrl+"/resource/distinbydate";
		paramMap.put("kpi", kpiPath);
		String fieldStr = HttpClientUtils.sendGetRequest(url, paramMap);
		List<String> distinDate = new Gson().fromJson(fieldStr, new TypeToken<List<String>>(){}.getType());
//		List<String> distinDate = getDefatulDate(kpiPath);
		if (CYCLE_DAY.equals(cycle)) {
			distinDate = getDistinctDayDate(distinDate, parentName, level);
		} else if (CYCLE_MONTH.equals(cycle)) {
			distinDate = getDistinctMonthDate(distinDate, parentName, level);
		} else if (CYCLE_WEEK.equals(cycle)) {
			distinDate = getDistinctWeekDate(distinDate, parentName, level);
		} else if (CYCLE_YEAR.equals(cycle)) {
		}
		Collections.sort(distinDate, Collections.reverseOrder());
		return distinDate;
	}
	

	private static List<String> getDistinctWeekDate(List<String> distinDate, String parentName, int level) {
		//20170904--20170910
		Set<String> dates = new HashSet<String>(); 
		for (String date : distinDate) {
			if (level == 1) {
				dates.add(date.substring(0, 4));
			} else if (level == 2) {
				if (date.startsWith(parentName))
				dates.add(date.substring(4, 6));
			} else if (level == 3) {
				if (date.startsWith(parentName.replaceAll("-", ""))) 
				dates.add(date.substring(4, 8)+"--"+date.substring(14));
			}
		}
		return new ArrayList<String>(dates);
	}

	private static List<String> getDistinctMonthDate(List<String> distinDate, String parentName, int level) {
		//2017-01
		Set<String> dates = new HashSet<String>(); 
		for (String date : distinDate) {
			if (level == 1) {
				dates.add(date.substring(0, 4));
			} else if (level == 2) {
				if (date.startsWith(parentName))
				dates.add(date.substring(5, 7));
			} 
		}
		return new ArrayList<String>(dates);
	}

	private static List<String> getDistinctDayDate(List<String> distinDate, String parentName, int level) {
		//2017-09-18
		Set<String> dates = new HashSet<String>(); 
		for (String date : distinDate) {
			if (level == 1) {
				dates.add(date.substring(0, 4));
			} else if (level == 2) {
				if (date.startsWith(parentName))
				dates.add(date.substring(5, 7));
			} else {
				if (date.startsWith(parentName))
				dates.add(date.substring(8, 10));
			}
		}
		return new ArrayList<String>(dates);
	}
	
	public String getIndicatorDefaultDate(String tableModelId, String cycle) throws RestInvokeException, DataSharingMgrError {
		String kpiPath = getKpiPathByTModelId(tableModelId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String url = indicatorServiceUrl+"/resource/distinbydate";
		paramMap.put("kpi", kpiPath);
		String fieldStr = HttpClientUtils.sendGetRequest(url, paramMap);
		List<String> distinDate = new Gson().fromJson(fieldStr, new TypeToken<List<String>>(){}.getType());
//		List<String> distinDate = getDefatulDate(kpiPath);
		Collections.sort(distinDate, Collections.reverseOrder());
		return distinDate.size() > 0 ? distinDate.get(0) : ""; 
	}
	
	public static List<String> getDefatulDate(String kpiPath) {
		List<String> dates = new ArrayList<String>();
		if (kpiPath.equals("指标目录/房产板块/财务/营销/年签约额/年签约额")) {
			dates.add("2017");
			dates.add("2016");
			dates.add("2014");
			dates.add("2015");
		} else if (kpiPath.equals("指标目录/房产板块/财务/营销/周签约额/周签约额")) {
			dates.add("20170904--20170910");
			dates.add("20160101--20160107");
			dates.add("20150904--20150910");
			dates.add("20180206--20180212");
			dates.add("20150920--20150926");
			dates.add("20180212--20180218");
		} else if (kpiPath.equals("指标目录/房产板块/财务/营销/日签约额/日签约额")) {
			dates.add("2017-09-18");
			dates.add("2017-09-14");
			dates.add("2017-09-13");
			dates.add("2016-08-11");
			dates.add("2016-08-12");
			dates.add("2017-09-17");
			dates.add("2017-02-18");
			dates.add("2015-03-18");
			dates.add("2015-03-22");
			dates.add("2015-04-16");
			dates.add("2015-04-18");
			dates.add("2017-06-18");
			dates.add("2017-07-29");
			dates.add("2017-07-28");
		} else if (kpiPath.equals("指标目录/房产板块/财务/营销/月签约额/月签约额")){
			dates.add("2017-06");
			dates.add("2015-07");
			dates.add("2014-07");
			dates.add("2017-07");
			dates.add("2017-08");
			dates.add("2017-09");
		}
		return dates;
	}
	
	
	public static void main(String[] args) {
		String unit = "亿元";
		String columnName = "列1";
//		String columnName =  StringUtils.isEmpty(k.remarks) ? k.name : k.remarks;
    	if (!StringUtils.isEmpty(unit)) columnName += "("+unit+")";
    	
		System.out.println(columnName);
	}

	private List<String> getHeadings(List<kpiField> fields, String unit) {
        List<String> heading = new LinkedList<>();
        for (kpiField k : fields) {
        	String columnName =  StringUtils.isEmpty(k.remarks) ? k.name : k.remarks;
        	if (isCountable(k.sqlDataType) && !StringUtils.isEmpty(unit)) columnName += "("+unit+")";
        	heading.add(columnName);
        }
        return heading;
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
	
	class kpiField {
		
		public kpiField() {};
		
		public kpiField(String name, String remarks) {
			this.name = name;
			this.remarks = remarks;
		};
		
		public String name;
		public int sqlDataType;
		public int size;
		public Integer decimalDigit;
		public Integer charOctetLength;
		public String nullable;
		public int ordinalPosition;
		public String remarks;
	}


	
}
