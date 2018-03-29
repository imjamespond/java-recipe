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
import org.copycat.framework.Page;
import org.copycat.framework.annotation.AutoId;
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

import com.metasoft.model.GenericDao;
import com.metasoft.service.DaoHelperFactory;

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class GenericDaoService<T_DAO extends GenericDao> {
	static Logger log = LoggerFactory.getLogger(GenericDaoService.class);
	
	protected final Class<?> kDaoClass;
	
	protected String kSelectSql;
	protected String kCountSql;
	protected  String kInsertSql;
	protected String kUpdateSql;
	protected String kDeleteSql;
	protected String kSelectByIdSql;
	protected String kUpdateByIdSql;
	protected String kDeleteByIdSql;
	protected final String kTable;
	protected final Attr kId;
	protected final String kSeq;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private GenericDaoHelper genericDaoHelper;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private DaoHelperFactory daoHelperFactory;

    public GenericDaoService() {
        Type superclass = getClass().getGenericSuperclass(); 
        ParameterizedType parameterized = (ParameterizedType) superclass;
        kDaoClass = (Class<?>) parameterized.getActualTypeArguments()[0];
        
        kTable = DbUtil.GetTable(kDaoClass);
        Assert.notNull(kTable, "@Table不能为空");
        kSeq = kTable+"_SEQ";
        kId = DbUtil.GetIdField(kDaoClass);
    }

	@PostConstruct
    public void init(){
		log.debug("{} init",kDaoClass);
    	jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
        //ini sql
        genericDaoHelper = daoHelperFactory.getDaoHelper();
        kSelectSql = genericDaoHelper.SelectSql(this);
        kCountSql = genericDaoHelper.CountSql(this);
        kInsertSql = genericDaoHelper.InsertSql(this);
        kUpdateSql = genericDaoHelper.UpdateSql(this);
        kDeleteSql = genericDaoHelper.DeleteSql(this);

        kUpdateByIdSql = genericDaoHelper.UpdateByIdSql(this);
        kDeleteByIdSql = genericDaoHelper.DeleteByIdSql(this);
        kSelectByIdSql = genericDaoHelper.SelectByIdSql(this);
    }
    
    public int insert(T_DAO dao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	return namedParameterJdbcTemplate.update(kInsertSql, getMapedParams(dao));
    }
    public String insertByAutoId(T_DAO dao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{  	
    	MapSqlParameterSource parameters = getMapedParams(dao);
    	String getNextIdSql = genericDaoHelper.getNextIdSql(this);
    	String nextId = jdbcTemplate.queryForObject(getNextIdSql, String.class);
    	String  kInsertByAutoIdSql = genericDaoHelper.InsertByAutoIdSql(this,nextId);
    	namedParameterJdbcTemplate.update(kInsertByAutoIdSql, parameters);
    	return nextId;
    }
    public int[] batchInsert(List<T_DAO> daos) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
    	for(T_DAO dao:daos){
    		params.add(getMapedParams(dao));
    	}
    	return namedParameterJdbcTemplate.batchUpdate(kInsertSql, params.toArray(new SqlParameterSource[0]));	
    }
    public int[] batchInsertByAutoId(List<T_DAO> daos) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{  	
//    	Assert.notNull(kInsertReturnAutoIdSql,"自增字段为空");
    	List<SqlParameterSource> params = new ArrayList<SqlParameterSource>();
    	for(T_DAO dao:daos){
    		params.add(getMapedParams(dao));
    	}
    	String batchUpdateSql = genericDaoHelper.getBatchInsertSql(this);
    	return namedParameterJdbcTemplate.batchUpdate(batchUpdateSql, params.toArray(new SqlParameterSource[0]));
    }
    protected MapSqlParameterSource getMapedParams(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Attr> attrList = DbUtil.GetFieldList(kDaoClass);
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	for(Attr attr : attrList){
    		parameters.addValue(attr.attr, BeanUtils.getProperty(dao, attr.attr));
    	}
    	if(this.kId!=null)
    		parameters.addValue(kId.attr, BeanUtils.getProperty(dao, kId.attr));
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
    		list = jdbcTemplate.queryForList(SQLHelper.genPaginationSQL(kSelectSql), objs);
    	} else {
    		list = jdbcTemplate.queryForList(kSelectSql+where, objs);
    	}
    	return getGenericDaoList(list);
    }
    
    public List<GenericDao> selectByPagination(Page page,String sql, Object... whereParams){
    	String pageSql = genericDaoHelper.getPageSql(sql,page);
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql, whereParams);
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
			List<Attr> attrList = DbUtil.GetFieldList(kDaoClass);
			for(Attr attr : attrList){
				Object val = objMap.get(attr.field);
				BeanUtils.setProperty(dao, attr.attr, val);
			}
			if(kId!=null){
				Object val = objMap.get(kId.field);
				BeanUtils.setProperty(dao, kId.attr, val);
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
    	List<Attr> attrList = DbUtil.GetFieldList(kDaoClass);
    	List<Object> parameters = new ArrayList<Object>();
    	for(Attr attr : attrList){
			parameters.add(BeanUtils.getProperty(dao, attr.attr));
    	}
    	return parameters;
    }
    private Object getId(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	Assert.notNull(kId);
    	return BeanUtils.getProperty(dao, kId.attr);
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
    	AutoId id = DbUtil.GetAutoIdField(kDaoClass);
    	Assert.notNull(id);
    	//if(checkSeq()>0)
    		//return 0;
    	String createSeqSql = genericDaoHelper.getCreateSeqSql(this);
    	try {
    	return jdbcTemplate.update(createSeqSql);
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