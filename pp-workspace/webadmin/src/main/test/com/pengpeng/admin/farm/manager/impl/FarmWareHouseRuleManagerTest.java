package com.pengpeng.admin.farm.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.farm.dao.IFarmWareHouseRuleDao;
import com.pengpeng.admin.farm.manager.IFarmWareHouseRuleManager;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FarmWareHouseRuleManagerTest implements IFarmWareHouseRuleManager{
	private static final Log log = LogFactory.getLog(FarmWareHouseRuleManagerTest.class);
	
	@Autowired
	private IFarmWareHouseRuleDao farmWareHouseRuleDao;
	public FarmWareHouseRule createBean(FarmWareHouseRule farmWareHouseRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmWareHouseRule);
		}		
		farmWareHouseRuleDao.createBean(farmWareHouseRule);
		return farmWareHouseRule;
	}

	@Override	
	public void updateBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmWareHouseRule);
		}
		farmWareHouseRuleDao.updateBean(farmWareHouseRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		farmWareHouseRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmWareHouseRule);
		}
		farmWareHouseRuleDao.removeBean(farmWareHouseRule);
	}
	
	@Override	
	public Page<FarmWareHouseRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FarmWareHouseRule> list = farmWareHouseRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FarmWareHouseRule> page = new Page<FarmWareHouseRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FarmWareHouseRule findById(Serializable id) throws NotFoundBeanException{
		return farmWareHouseRuleDao.findById(id);
	}
}