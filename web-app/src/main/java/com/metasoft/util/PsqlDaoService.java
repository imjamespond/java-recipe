package com.metasoft.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

import com.metasoft.model.annotation.AutoId;
import com.metasoft.model.annotation.Index;
import com.metasoft.util.GenericDaoHelper.Attr;
import com.metasoft.util.GenericDaoHelper.GenericDao; 

/**
 * @author metasoft
 * Generic data accessing object
 * @param <T>
 */
public class PsqlDaoService<T_DAO extends GenericDao> {
	static Logger log = LoggerFactory.getLogger(PsqlDaoService.class);
	
	protected final Class<?> kDaoClass;
	
	protected final String kCreateSql;
	protected final String kSelectSql;
	protected final String kCountSql;
	protected final String kInsertSql;
	protected final String kUpdateSql;
	protected final String kDeleteSql;
	protected final String kSelectByIdSql;
	protected final String kUpdateByIdSql;
	protected final String kDeleteByIdSql;
	protected final String kInsertByAutoIdSql;
	protected final String kInsertReturnAutoIdSql;
	protected final String kTable;
	protected final Attr kId;
	protected final String kSeq;
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;

    public PsqlDaoService() {
        Type superclass = getClass().getGenericSuperclass(); 
        ParameterizedType parameterized = (ParameterizedType) superclass;
        kDaoClass = (Class<?>) parameterized.getActualTypeArguments()[0];
        
        kTable = GenericDaoHelper.GetTable(kDaoClass);
        Assert.notNull(kTable, "@Table不能为空");
        kSeq = kTable+"_SEQ";
        kId = GenericDaoHelper.GetIdField(kDaoClass);
        
        kCreateSql = PsqlHelper.CreateSql(this);
        kSelectSql = PsqlHelper.SelectSql(this);
        kCountSql = PsqlHelper.CountSql(this);
        kInsertSql = PsqlHelper.InsertSql(this);
        kUpdateSql = PsqlHelper.UpdateSql(this);
        kDeleteSql = PsqlHelper.DeleteSql(this);

        kUpdateByIdSql = PsqlHelper.UpdateByIdSql(this);
        kDeleteByIdSql = PsqlHelper.DeleteByIdSql(this);
        kSelectByIdSql = PsqlHelper.SelectByIdSql(this);
        kInsertByAutoIdSql = PsqlHelper.InsertByAutoIdSql(this);
        if(null!=kId)
        	kInsertReturnAutoIdSql = kInsertByAutoIdSql+" RETURNING id";
        else
        	kInsertReturnAutoIdSql = null;
        log.trace("\n{}; \n{}; \n{}",kInsertByAutoIdSql,kUpdateSql,kCreateSql);
    }

	@PostConstruct
    public void init(){
		log.debug("{} init",kDaoClass);
    	jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int insert(T_DAO dao) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	return jdbcTemplate.update(kInsertSql, getParams(dao));
    }
    public Long insertByAutoId(T_DAO dao) throws Exception{  	
    	Assert.notNull(kInsertReturnAutoIdSql,"自增字段为空");
    	Object[] parameters = getParamsWithoutId(dao);
    	return jdbcTemplate.queryForObject(kInsertReturnAutoIdSql, parameters, Long.class);
    }
    public int[] batchInsert(List<T_DAO> daos) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Object[]> params = new ArrayList<>();
    	for(T_DAO dao:daos){
    		params.add(getParams(dao).toArray());
    	}
    	return jdbcTemplate.batchUpdate(kInsertSql, params);	
    }
    public int[] batchInsertByAutoId(List<T_DAO> daos) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{  	
    	Assert.notNull(kInsertReturnAutoIdSql,"自增字段为空");
    	List<Object[]> params = new ArrayList<>();
    	for(T_DAO dao:daos){
    		params.add(getParamsWithoutId(dao));
    	}
    	return jdbcTemplate.batchUpdate(kInsertByAutoIdSql, params);
    }
    
    protected List<Object> getParams(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Attr> attrList = GenericDaoHelper.GetFieldList(kDaoClass);
    	List<Object> parameters = new ArrayList<>();
    	for(Attr attr : attrList){
    		parameters.add( PropertyUtils.getProperty(dao,  attr.name));
    	}
    	return parameters;
    }
    
    protected Object[] getParamsWithoutId(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Attr> attrList = GenericDaoHelper.GetFieldList(kDaoClass);
    	List<Object> parameters = new ArrayList<>();
    	for(Attr attr : attrList){
    		if(null!=attr.getAutoId()||null!=attr.getId())
    			continue;
    		parameters.add( PropertyUtils.getProperty(dao,  attr.name));
    	}
    	return parameters.toArray();
    }
    
    
    public GenericDao selectById(Object id ){
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(kSelectByIdSql,id);
    	if(list.size()>0)
    		return assembleObject(list.get(0));
    	return null;
    }
   
    public List<GenericDao> select(final String sql, Object... objs){
    	List<Map<String, Object>> list = null;
    	list = jdbcTemplate.queryForList(sql, objs);
    	return getGenericDaoList(list);
    }
    
    public List<GenericDao> selectAll(){
    	return this.select(this.kSelectSql, new Object[]{});
    }
    
    public List<GenericDao> selectByPagination(int pageSize,int start,String where, Object... whereParams){
    	List<Object> param = Arrays.asList(whereParams);
    	Integer pageSize_ = new Integer(pageSize);
    	Integer start_ = new Integer(start);
    	List<Object> paramList = new ArrayList<Object>(param);
    	paramList.add(pageSize_);
    	paramList.add(start_);
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(kSelectSql+where, paramList.toArray());
    	return getGenericDaoList(list);
    }
    
    public Integer count(final String sql, Object... objs){
    	return jdbcTemplate.queryForObject(sql, Integer.class,objs);
    }
    public List<GenericDao> selectTest(){
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(kSelectSql+" limit 10 OFFSET 0");	
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
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
    }

    public int update(T_DAO dao,String sql, Object...objs) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	List<Object> parameters = getParams(dao);
    	for(Object obj: objs){
    		parameters.add(obj);
    	}
    	return jdbcTemplate.update(sql, parameters.toArray());
    }
    public int updateById(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {   	
    	List<Object> parameters = getParams(dao);
    	parameters.add(getId(dao));
    	return jdbcTemplate.update(kUpdateByIdSql, parameters.toArray());
    }
    
    private Object getId(T_DAO dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
    	Assert.notNull(kId, "");
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
    public void createTable(){
    	jdbcTemplate.execute(kCreateSql);
    }
    public void createSeq(){
    	AutoId id = GenericDaoHelper.GetAutoId(kDaoClass);
    	Assert.notNull(id, "");
    	String sql = "CREATE SEQUENCE "+kSeq+" start "+id.start()+" increment by "+id.stride();
    	try {
    		jdbcTemplate.execute(sql);
    	} catch (Exception e){
    		log.debug("[{}] not executed",sql);
    	}
    }
	
	public void createIndex() {
		List<Attr> attrList = GenericDaoHelper.GetFieldList(this.kDaoClass);
		for(Attr attr : attrList){
			Index index = attr.getIndex();
			String col = attr.getColumn();
			if(null==index)
				continue;
			String sql = PsqlHelper.CreateIndexSql(this.kTable, col, index);
			try {
	    		this.jdbcTemplate.execute(sql);
	    	} catch (Exception e){
	    		log.debug("[{}] not executed",sql);
	    	}
		}
	}
	
    public long nextval(){
    	return this.getJdbcTemplate().queryForObject("select nextval( '"+this.kSeq+"' )", Long.class);
    }
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

}