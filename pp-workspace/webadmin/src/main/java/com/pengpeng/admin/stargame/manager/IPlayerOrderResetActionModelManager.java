package com.pengpeng.admin.stargame.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.admin.stargame.model.PlayerOrderResetActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerOrderResetActionModelManager{
	
	public PlayerOrderResetActionModel createBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws NotFoundBeanException;
	
	public PlayerOrderResetActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerOrderResetActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize);

}