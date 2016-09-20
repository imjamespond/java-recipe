package com.pengpeng.stargame.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.piazza.dao.IActiveControlRuleDao;
import com.pengpeng.stargame.piazza.manager.IActiveControlRuleManager;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class ActiveControlRuleManagerTest implements IActiveControlRuleManager{
	private static final Log log = LogFactory.getLog(ActiveControlRuleManagerTest.class);
	
	@Autowired
	private IActiveControlRuleDao activeControlRuleDao;
	public ActiveControlRule createBean(ActiveControlRule activeControlRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+activeControlRule);
		}		
		activeControlRuleDao.createBean(activeControlRule);
		return activeControlRule;
	}

	@Override	
	public void updateBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+activeControlRule);
		}
		activeControlRuleDao.updateBean(activeControlRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		activeControlRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+activeControlRule);
		}
		activeControlRuleDao.removeBean(activeControlRule);
	}
	
	@Override	
	public Page<ActiveControlRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<ActiveControlRule> list = activeControlRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<ActiveControlRule> page = new Page<ActiveControlRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public ActiveControlRule findById(Serializable id) throws NotFoundBeanException{
		return activeControlRuleDao.findById(id);
	}
}