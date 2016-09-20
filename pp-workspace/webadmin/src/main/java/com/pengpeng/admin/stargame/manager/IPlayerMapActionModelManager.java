package com.pengpeng.admin.stargame.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.admin.stargame.model.PlayerMapActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IPlayerMapActionModelManager{
	
	public PlayerMapActionModel createBean(PlayerMapActionModel playerMapActionModel) throws BeanAreadyException;
	
	public void updateBean(PlayerMapActionModel playerMapActionModel) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(PlayerMapActionModel playerMapActionModel) throws NotFoundBeanException;
	
	public PlayerMapActionModel findById(Serializable id) throws NotFoundBeanException;
	
	public Page<PlayerMapActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize);

}