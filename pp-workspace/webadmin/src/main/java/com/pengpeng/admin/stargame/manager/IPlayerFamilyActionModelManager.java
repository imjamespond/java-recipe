package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerFamilyActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerFamilyActionModelManager {
	
	public PlayerFamilyActionModel createBean(PlayerFamilyActionModel playerFamilyActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException;
	
	public PlayerFamilyActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerFamilyActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}