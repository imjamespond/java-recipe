package com.tongyi.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

/**
 * 通用的DAO接口
 * @author 
 *
 * @param <T>
 */
public interface IBaseDao<T> {

	public void createBean(T bean) throws BeanAreadyException;
	
	public void updateBean(T bean) throws NotFoundBeanException;
	
	public void removeBean(T bean) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	
	public T findById(Serializable id) throws NotFoundBeanException;
	
//	public List<T> findPages(String sql,int begin,int offset);

	public List<T> findPages(String nameQuery, Map<String, Object> params, int begin, int offset);
	
	public List<T> findByNameQuery(String nameQuery, Map<String, Object> params);
	
}
