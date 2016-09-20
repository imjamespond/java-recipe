package com.pengpeng.admin.farm.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.farm.dao.IFarmSeedRuleDao;
import com.pengpeng.admin.farm.manager.IFarmSeedRuleManager;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FarmSeedRuleManagerTest implements IFarmSeedRuleManager{
	private static final Log log = LogFactory.getLog(FarmSeedRuleManagerTest.class);
	
	@Autowired
	private IFarmSeedRuleDao farmSeedRuleDao;
	public FarmSeedRule createBean(FarmSeedRule farmSeedRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmSeedRule);
		}		
		farmSeedRuleDao.createBean(farmSeedRule);
		return farmSeedRule;
	}

	@Override	
	public void updateBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmSeedRule);
		}
		farmSeedRuleDao.updateBean(farmSeedRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		farmSeedRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmSeedRule);
		}
		farmSeedRuleDao.removeBean(farmSeedRule);
	}
	
	@Override	
	public Page<FarmSeedRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FarmSeedRule> list = farmSeedRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FarmSeedRule> page = new Page<FarmSeedRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FarmSeedRule findById(Serializable id) throws NotFoundBeanException{
		return farmSeedRuleDao.findById(id);
	}
}