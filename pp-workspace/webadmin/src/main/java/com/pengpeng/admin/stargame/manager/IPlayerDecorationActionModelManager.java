package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerDecorationActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerDecorationActionModelManager {
	
	public PlayerDecorationActionModel createBean(PlayerDecorationActionModel playerDecorationActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerDecorationActionModel playerDecorationActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerDecorationActionModel playerDecorationActionModel) throws NotFoundBeanException;
	
	public PlayerDecorationActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerDecorationActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}