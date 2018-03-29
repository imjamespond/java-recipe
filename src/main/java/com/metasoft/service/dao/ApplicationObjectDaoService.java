package com.metasoft.service.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.metasoft.model.GenericDao;
import com.metasoft.model.dao.ApplicationObjectDao;
import com.metasoft.service.DaoHelperFactory;
import com.metasoft.util.Attr;
import com.metasoft.util.Commons;
import com.metasoft.util.DBType;
import com.metasoft.util.DbUtil;
import com.metasoft.util.GenericDaoService;

@Service
public class ApplicationObjectDaoService extends GenericDaoService<ApplicationObjectDao> {
	
	static Logger log = LoggerFactory.getLogger(ApplicationObjectDaoService.class);
	final String kUpdateModeSql;
	
	@Autowired
	private DaoHelperFactory daoHelperFactory;
	
	private DBType dbType;

	public ApplicationObjectDaoService(){
		kUpdateModeSql = initUpdateMode();
	}

	public void init(){
		super.init();
		this.createSeq();
		this.dbType = daoHelperFactory.getDBType();
	}
	
	private String initReplaceInto(){
		List<Attr> attrList = DbUtil.GetFieldList(this.kDaoClass);
		String insertFields = "";
		String mergeFields ="";
        String updateFields = "";
        String insertHolders = "";
		for(Attr attr : attrList){
			if(insertFields.length()>0)
        		insertFields+=",";
        	insertFields+=attr.field;
        	
        	mergeFields+=",merge."+attr.field;
        	
        	if(updateFields.length()>0)
        		updateFields+=",";
        	updateFields+="tab."+attr.field+" = merge."+attr.field;
        	
			if(insertHolders.length()>0)
        		insertHolders+=",";		
			insertHolders+=":"+attr.attr;
		}
		
		return "MERGE INTO "+this.kTable+" AS tab "
		+ "USING (VALUES "
		+ "("+insertHolders+") ) AS merge ("+insertFields+") "
		+ "ON tab.user_id = merge.user_id AND tab.obj_id = merge.obj_id AND tab.appl_id='' "
		+ "WHEN MATCHED THEN "
		+ "UPDATE SET "+updateFields
		+ " WHEN NOT MATCHED THEN "
		+ "INSERT ("+this.kId.field+","+insertFields+") "
		+ "VALUES ("+"TRIM(CAST(CAST(NEXT VALUE FOR "+this.kSeq+" AS CHAR(50)) AS VARCHAR(50)))"+mergeFields+")";
	}
	
	public int[] batchReplaceInto(List<ApplicationObjectDao> daos) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if (dbType.isDB2()) {
			List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
			for(ApplicationObjectDao dao:daos){
				params.add(getMapedParams(dao));
			}
			return this.getNamedParameterJdbcTemplate().batchUpdate(initReplaceInto(), params.toArray(new SqlParameterSource[0])); 
		} else if (dbType.isMySql()) {
			return batchReplaceIntoMySql(daos);
		} else {
			return null;
		}
	}

	private int[] batchReplaceIntoMySql(List<ApplicationObjectDao> daos)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<GenericDao> genericDaos = select(" where appl_id = '' ");
		List<ApplicationObjectDao> applicationObjectDaos = new ArrayList<ApplicationObjectDao>();
		for (GenericDao one : genericDaos) {
			applicationObjectDaos.add((ApplicationObjectDao)one);
		}
		
		List<SqlParameterSource> updateParams = new ArrayList<SqlParameterSource>();
		List<SqlParameterSource> insertParams = new ArrayList<SqlParameterSource>();
		for (ApplicationObjectDao dao : daos) {
			String userId = dao.getUser_id();
			String objId = dao.getObj_id();
			String matchId = match(userId,objId,applicationObjectDaos);
			if (!StringUtils.isEmpty(matchId)) {
				dao.setId(matchId);
				updateParams.add(getMapedParams(dao));
			} else {
				insertParams.add(getMapedParams(dao));
			}
		}
		
		String insertSql = daoHelperFactory.getDaoHelper().getBatchInsertSql(this);
		String updateSql = "update DS_APPLICATION_OBJECT set USER_ID=:user_id"
				+ " , DOMAIN_ID=:domain_id , APPL_ID=:appl_id , OBJ_ID"
				+ "=:obj_id , OBJ_NAME=:obj_name , OBJ_TYPE=:obj_type ,"
				+ " OBJ_MODE=:obj_mode , REMARK=:remark , CONSTRAINT_=:constraint where ID=:id";
		this.getNamedParameterJdbcTemplate().batchUpdate(updateSql, updateParams.toArray(new SqlParameterSource[0]));
		int[] inserts = this.getNamedParameterJdbcTemplate().batchUpdate(insertSql, insertParams.toArray(new SqlParameterSource[0]));
		return inserts;
	}
	
	private String match(String userId, String objId, List<ApplicationObjectDao> applicationObjectDaos) {
		for (ApplicationObjectDao aoDao : applicationObjectDaos) {
			if (userId.equals(aoDao.getUser_id()) && objId.equals(aoDao.getObj_id())) {
				return aoDao.getId();
			}
		}
		return "";
	}

	private String initUpdateMode() {
		return "update "+this.kTable+" set obj_mode=:mode, appl_id=:appl_id where id=:id";
	}
	
	public int[] batchUpdateMode(String json,String appl_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Map<String,Object>> list = Commons.FromJson(json);
		List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
		for(Map<String,Object> map:list){
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("id", map.get("id"));
			param.addValue("mode", map.get("mode"));
			param.addValue("appl_id", appl_id);
			params.add(param);
		}
		return this.getNamedParameterJdbcTemplate().batchUpdate(this.kUpdateModeSql, params.toArray(new SqlParameterSource[0])); 
	}
	
	public int[] batchDelete(String json) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Map<String,Object>> list = Commons.FromJson(json);
		List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
		for(Map<String,Object> map:list){
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("id", map.get("id"));
			params.add(param);
		}
		final String sql = "delete from "+this.kTable+" where id=:id";
		return this.getNamedParameterJdbcTemplate().batchUpdate(sql, params.toArray(new SqlParameterSource[0])); 
	}

	public void deleteByApplId(String appl_id) {
		final String sql = "delete from "+this.kTable+" where appl_id=?";
		Object[] args = {appl_id};
		this.getJdbcTemplate().update(sql, args);
	}

	public List<GenericDao> selectByApplId( String applId) {
		Object[] objs = {applId};
		return this.select(" where appl_id=?", objs);
	}

	public void delete(String modelId, String userId) {
		final String sql = "delete from " + this.kTable + " where OBJ_ID=? and USER_ID=?";
		this.getJdbcTemplate().update(sql, modelId, userId);
	}
	
}
