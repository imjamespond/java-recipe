package com.pengpeng.stargame.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.piazza.dao.IMoneyTreeRuleDao;
import com.pengpeng.stargame.piazza.manager.IMoneyTreeRuleManager;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class MoneyTreeRuleManagerTest implements IMoneyTreeRuleManager{
	private static final Log log = LogFactory.getLog(MoneyTreeRuleManagerTest.class);
	
	@Autowired
	private IMoneyTreeRuleDao moneyTreeRuleDao;
	public MoneyTreeRule createBean(MoneyTreeRule moneyTreeRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+moneyTreeRule);
		}		
		moneyTreeRuleDao.createBean(moneyTreeRule);
		return moneyTreeRule;
	}

	@Override	
	public void updateBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+moneyTreeRule);
		}
		moneyTreeRuleDao.updateBean(moneyTreeRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		moneyTreeRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+moneyTreeRule);
		}
		moneyTreeRuleDao.removeBean(moneyTreeRule);
	}
	
	@Override	
	public Page<MoneyTreeRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<MoneyTreeRule> list = moneyTreeRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<MoneyTreeRule> page = new Page<MoneyTreeRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public MoneyTreeRule findById(Serializable id) throws NotFoundBeanException{
		return moneyTreeRuleDao.findById(id);
	}
}