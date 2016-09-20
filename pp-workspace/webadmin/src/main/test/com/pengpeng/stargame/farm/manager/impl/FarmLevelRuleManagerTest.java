package com.pengpeng.stargame.farm.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.farm.dao.IFarmLevelRuleDao;
import com.pengpeng.stargame.farm.manager.IFarmLevelRuleManager;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FarmLevelRuleManagerTest implements IFarmLevelRuleManager{
	private static final Log log = LogFactory.getLog(FarmLevelRuleManagerTest.class);
	
	@Autowired
	private IFarmLevelRuleDao farmLevelRuleDao;
	public FarmLevelRule createBean(FarmLevelRule farmLevelRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmLevelRule);
		}		
		farmLevelRuleDao.createBean(farmLevelRule);
		return farmLevelRule;
	}

	@Override	
	public void updateBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmLevelRule);
		}
		farmLevelRuleDao.updateBean(farmLevelRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		farmLevelRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmLevelRule);
		}
		farmLevelRuleDao.removeBean(farmLevelRule);
	}
	
	@Override	
	public Page<FarmLevelRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FarmLevelRule> list = farmLevelRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FarmLevelRule> page = new Page<FarmLevelRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FarmLevelRule findById(Serializable id) throws NotFoundBeanException{
		return farmLevelRuleDao.findById(id);
	}
}