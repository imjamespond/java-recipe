package com.pengpeng.admin.farm.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IFarmSeedRuleManager{
	
	public FarmSeedRule createBean(FarmSeedRule farmSeedRule) throws BeanAreadyException;
	
	public void updateBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException;
	
	public FarmSeedRule findById(Serializable id) throws NotFoundBeanException;
	
	public Page<FarmSeedRule> findPages(Map<String,Object> params,int pageNo,int pageSize);

}