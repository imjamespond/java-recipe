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
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "farmWareHouseRuleManager")
public class FarmWareHouseRuleManagerImpl implements IFarmWareHouseRuleManager{
	private static final Log log = LogFactory.getLog(FarmWareHouseRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="farmWareHouseRuleDao")
	private IFarmWareHouseRuleDao farmWareHouseRuleDao;

	@Override	
	public FarmWareHouseRule findById(Serializable id) throws NotFoundBeanException{
		return farmWareHouseRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<FarmWareHouseRule> list = farmWareHouseRuleDao.findPages("from FarmWareHouseRule a",map,start,start+pageSize);

		Page<FarmWareHouseRule> page = new Page<FarmWareHouseRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}