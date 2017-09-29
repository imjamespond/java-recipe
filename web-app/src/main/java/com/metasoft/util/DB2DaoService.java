package com.metasoft.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.metasoft.model.annotation.AutoId;
import com.metasoft.util.GenericDaoHelper.Attr;
import com.metasoft.util.GenericDaoHelper.GenericDao;

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class DB2DaoService<T_DAO extends GenericDao> {
	static Logger log = LoggerFactory.getLogger(DB2DaoService.class);
	
	protected final Class<?> kDaoClass;
	
	final String kSelectSql;
	final String kCountSql;
	final String kInsertSql;
	final String kUpdateSql;
	final String kDeleteSql;
	final String kSelectByIdSql;
	final String kUpdateByIdSql;
	final String kDeleteByIdSql;
	final String kInsertByAutoIdSql;
	final String kInsertReturnAutoIdSql;
	protected final String kTable;
	protected final Attr kId;
	protected final String kSeq;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private DataSource dataSource;

    public DB2DaoService() {
        Type superclass = getClass().getGenericSuperclass(); 
        ParameterizedType parameterized = (ParameterizedType) superclass;
        kDaoClass = (Class<?>) parameterized.getActualTypeArguments()[0];
        
        kTable = GenericDaoHelper.GetTable(kDaoClass);
        Assert.notNull(kTable, "@Table不能为空");
        kSeq = kTable+"_SEQ";
        kId = GenericDaoHelper.GetIdField(kDaoClass);
        
        kSelectSql = DB2SQLHelper.SelectSql(this);
        kCountSql = DB2SQLHelper.CountSql(this);
        kInsertSql = DB2SQLHelper.InsertSql(this);
        kUpdateSql = DB2SQLHelper.UpdateSql(this);
        kDeleteSql = DB2SQLHelper.DeleteSql(this);

        kUpdateByIdSql = DB2SQLHelper.UpdateByIdSql(this);
        kDeleteByIdSql = DB2SQLHelper.DeleteByIdSql(this);
        kSelectByIdSql = DB2SQLHelper.SelectByIdSql(this);
        kInsertByAutoIdSql = DB2SQLHelper.InsertByAutoIdSql(this);
        if(null!=kId)
        	kInsertReturnAutoIdSql = "select "+kId.getColumn()+" from FINAL TABLE ("+kInsertByAutoIdSql+")";
        else
        	kInsertReturnAutoIdSql = null;
        log.debug("{}, {}, {}",kInsertSql,kUpdateSql,kDeleteSql);
    }

	@PostConstruct
    public void init(){
		log.debug("{} init",kDaoClass);
    	jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    public int insert(T_DAO dao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	return namedParameterJdbcTemplate.update(kInsertSql, getMapedParams(dao));
    }
    public String insertByAutoId(T_DAO dao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{  	
    	Assert.notNull(kInsertReturnAutoIdSql,"自增字段为空");
    	MapSqlParameterSource parameters = getMapedParams(dao);
    	return namedParameterJdbcTemplate.queryForObject(kInsertReturnAutoIdSql, parameters, String.class);
    }
    public int[] batchInsert(List<T_DAO> daos) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
    	for(T_DAO dao:daos){
    		params.add(getMapedParams(dao));
    	}
    	return namedParameterJdbcTemplate.batchUpdate(kInsertSql, params.toArray(new SqlParameterSource[0]));	
    }
    public int[] batchInsertByAutoId(List<T_DAO> daos) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{  	
    	Assert.notNull(kInsertReturnAutoIdSql,"自增字段为空");
    	List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
    	for(T_DAO dao:daos){
    		params.add(getMapedParams(dao));
    	}
    	return namedParameterJdbcTemplate.batchUpdate(kInsertByAutoIdSql, params.toArray(new SqlParameterSource[0]));
    }
    protected MapSqlParameterSource getMapedParams(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Attr> attrList = GenericDaoHelper.GetFieldList(kDaoClass);
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	for(Attr attr : attrList){
    		parameters.addValue(attr.getColumn(), BeanUtils.getProperty(dao, attr.name));
    	}
    	if(this.kId!=null)
    		parameters.addValue(kId.getColumn(), BeanUtils.getProperty(dao, kId.name));
    	return parameters;
    }
    
    public GenericDao selectById(Object id ){
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(kSelectByIdSql,id);
    	if(list.size()>0)
    		return assembleObject(list.get(0));
    	return null;
    }
    public List<GenericDao> select(String where, Object... objs){
    	List<Map<String, Object>> list = null;
    	if (where.equals("limit ? offset ?")) {
    		list = jdbcTemplate.queryForList(DB2SQLHelper.genPaginationSQL(kSelectSql), objs);
    	} else {
    		list = jdbcTemplate.queryForList(kSelectSql+where, objs);
    	}
    	return getGenericDaoList(list);
    }
    public Integer count(String where, Object... objs){
    	return jdbcTemplate.queryForObject(kCountSql+where, Integer.class,objs);
    }
    public List<GenericDao> selectTest(){
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(kSelectSql+" limit 0,10");	
    	return getGenericDaoList(list);
    }
    protected List<GenericDao> getGenericDaoList(List<Map<String, Object>> list){
    	List<GenericDao> daos = new ArrayList<GenericDao>();
    	for(Map<String, Object> objMap:list)
    		daos.add(assembleObject(objMap));
    	return daos;
    }
    private GenericDao assembleObject(Map<String, Object> objMap){
		try {
			GenericDao dao = (GenericDao) kDaoClass.newInstance();
			List<Attr> attrList = GenericDaoHelper.GetFieldList(kDaoClass);
			for(Attr attr : attrList){
				Object val = objMap.get(attr.getColumn());
				BeanUtils.setProperty(dao, attr.name, val);
			}
			if(kId!=null){
				Object val = objMap.get(kId.getColumn());
				BeanUtils.setProperty(dao, kId.name, val);
			}
			return dao;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
    }
    public int update(String set,String where,Object... objs) {
    	String sql = "update "+kTable+" set "+set;
    	if(!StringUtils.isEmpty(where)) {
    		sql += " where " + where;
    	}
       	return jdbcTemplate.update(sql, objs);
    }
    public int update(T_DAO dao,String where,Object...objs) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Object> parameters = getParameterList(dao);
    	for(Object obj: objs){
    		parameters.add(obj);
    	}
    	return jdbcTemplate.update(kUpdateSql+where, parameters.toArray());
    }
    public int updateById(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {   	
    	List<Object> parameters = getParameterList(dao);
    	parameters.add(getId(dao));
    	return jdbcTemplate.update(kUpdateByIdSql, parameters.toArray());
    }
    private List<Object> getParameterList(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Attr> attrList = GenericDaoHelper.GetFieldList(kDaoClass);
    	List<Object> parameters = new ArrayList<Object>();
    	for(Attr attr : attrList){
			parameters.add(BeanUtils.getProperty(dao, attr.name));
    	}
    	return parameters;
    }
    private Object getId(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	Assert.notNull(kId, "primary key is null");
    	return BeanUtils.getProperty(dao, kId.name);
    }
    
    public int deleteById(Object id){
    	return jdbcTemplate.update(kDeleteByIdSql, id);
    }
    
    public void executeScript(String script, boolean ignoreErr){
    	ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        if(ignoreErr)
        	databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource(script));
		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }
    public int createSeq(){
    	AutoId id = GenericDaoHelper.GetAutoId(kDaoClass);
    	Assert.notNull(id, "primary key is null");
    	//if(checkSeq()>0)
    		//return 0;
    	String sql = "create sequence "+kSeq+" AS bigint "
    			+ "START WITH "+id.start()+" INCREMENT BY "+id.stride()+" MINVALUE 1 NO MAXVALUE NO CYCLE NO CACHE ORDER";
    	try {
    	return jdbcTemplate.update(sql);
    	} catch (Exception e){
    		log.debug("{} not created",kSeq);
    	}
    	return 0;
    }
    /*private int checkSeq(){
    	String sql = "SELECT count(*) FROM SYSCAT.SEQUENCES "
    			+ "WHERE SEQNAME=?";//AND SEQSCHEMA='DB2INST1'
    	Object[] objs = {kSeq};
    	return jdbcTemplate.queryForObject(sql, objs, Integer.class);
    }*/
    public int dropSeq(){
    	String sql = "drop sequence "+kSeq;
    	return jdbcTemplate.update(sql);
    }
    public int dropTable(){
    	String sql = "drop table if exists "+kTable;
    	return jdbcTemplate.update(sql);
    }
    public JdbcTemplate getJdbcTemplate(){
    	return jdbcTemplate;
    }

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
    
}