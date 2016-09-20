package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerGivingActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerGivingActionModelManager {
	
	public PlayerGivingActionModel createBean(PlayerGivingActionModel playerGivingActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerGivingActionModel playerGivingActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerGivingActionModel playerGivingActionModel) throws NotFoundBeanException;
	
	public PlayerGivingActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerGivingActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}