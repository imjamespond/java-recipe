package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerFamilyAwardActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerFamilyAwardActionModelManager {

	public PlayerFamilyAwardActionModel createBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws BeanAreadyException;

	public void updateBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws NotFoundBeanException;

	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws NotFoundBeanException;

	public PlayerFamilyAwardActionModel findById(Serializable id) throws NotFoundBeanException;

	public Page<PlayerFamilyAwardActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}