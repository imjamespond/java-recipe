package com.pengpeng.admin.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.piazza.dao.IMoneyTreeRuleDao;
import com.pengpeng.admin.piazza.manager.IMoneyTreeRuleManager;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "moneyTreeRuleManager")
public class MoneyTreeRuleManagerImpl implements IMoneyTreeRuleManager{
	private static final Log log = LogFactory.getLog(MoneyTreeRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="moneyTreeRuleDao")
	private IMoneyTreeRuleDao moneyTreeRuleDao;

	@Override	
	public MoneyTreeRule findById(Serializable id) throws NotFoundBeanException{
		return moneyTreeRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<MoneyTreeRule> list = moneyTreeRuleDao.findPages("from MoneyTreeRule a",map,start,start+pageSize);

		Page<MoneyTreeRule> page = new Page<MoneyTreeRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}