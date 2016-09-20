package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerMoneyTreeActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerMoneyTreeActionModelManager {
	
	public PlayerMoneyTreeActionModel createBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws NotFoundBeanException;
	
	public PlayerMoneyTreeActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerMoneyTreeActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}