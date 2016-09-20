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
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "farmSeedRuleManager")
public class FarmSeedRuleManagerImpl implements IFarmSeedRuleManager{
	private static final Log log = LogFactory.getLog(FarmSeedRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="farmSeedRuleDao")
	private IFarmSeedRuleDao farmSeedRuleDao;

	@Override	
	public FarmSeedRule findById(Serializable id) throws NotFoundBeanException{
		return farmSeedRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<FarmSeedRule> list = farmSeedRuleDao.findPages("from FarmSeedRule a",map,start,start+pageSize);

		Page<FarmSeedRule> page = new Page<FarmSeedRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}