package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerTaskActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerTaskActionModelManager {
	
	public PlayerTaskActionModel createBean(PlayerTaskActionModel playerTaskActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerTaskActionModel playerTaskActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerTaskActionModel playerTaskActionModel) throws NotFoundBeanException;
	
	public PlayerTaskActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerTaskActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}