package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerDonationActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;
import java.util.Map;

/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerDonationActionModelManager {
	
	public PlayerDonationActionModel createBean(PlayerDonationActionModel playerDonationActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerDonationActionModel playerDonationActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerDonationActionModel playerDonationActionModel) throws NotFoundBeanException;
	
	public PlayerDonationActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerDonationActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);

}