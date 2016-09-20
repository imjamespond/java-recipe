package com.pengpeng.admin.stargame.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.admin.stargame.model.PlayerActivityActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerActivityActionModelManager{
	
	public PlayerActivityActionModel createBean(PlayerActivityActionModel playerActivityActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerActivityActionModel playerActivityActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerActivityActionModel playerActivityActionModel) throws NotFoundBeanException;
	
	public PlayerActivityActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerActivityActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize);

}