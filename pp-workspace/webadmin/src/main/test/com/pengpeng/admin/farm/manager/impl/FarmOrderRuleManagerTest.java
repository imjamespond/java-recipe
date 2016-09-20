package com.pengpeng.admin.farm.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.farm.dao.IFarmOrderRuleDao;
import com.pengpeng.admin.farm.manager.IFarmOrderRuleManager;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FarmOrderRuleManagerTest implements IFarmOrderRuleManager{
	private static final Log log = LogFactory.getLog(FarmOrderRuleManagerTest.class);
	
	@Autowired
	private IFarmOrderRuleDao farmOrderRuleDao;
	public FarmOrderRule createBean(FarmOrderRule farmOrderRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmOrderRule);
		}		
		farmOrderRuleDao.createBean(farmOrderRule);
		return farmOrderRule;
	}

	@Override	
	public void updateBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmOrderRule);
		}
		farmOrderRuleDao.updateBean(farmOrderRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		farmOrderRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmOrderRule);
		}
		farmOrderRuleDao.removeBean(farmOrderRule);
	}
	
	@Override	
	public Page<FarmOrderRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FarmOrderRule> list = farmOrderRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FarmOrderRule> page = new Page<FarmOrderRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FarmOrderRule findById(Serializable id) throws NotFoundBeanException{
		return farmOrderRuleDao.findById(id);
	}
}