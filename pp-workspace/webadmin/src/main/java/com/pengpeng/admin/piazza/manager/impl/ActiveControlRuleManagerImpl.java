package com.pengpeng.admin.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.piazza.dao.IActiveControlRuleDao;
import com.pengpeng.admin.piazza.manager.IActiveControlRuleManager;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "activeControlRuleManager")
public class ActiveControlRuleManagerImpl implements IActiveControlRuleManager{
	private static final Log log = LogFactory.getLog(ActiveControlRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="activeControlRuleDao")
	private IActiveControlRuleDao activeControlRuleDao;

	@Override	
	public ActiveControlRule findById(Serializable id) throws NotFoundBeanException{
		return activeControlRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<ActiveControlRule> list = activeControlRuleDao.findPages("from ActiveControlRule a",map,start,start+pageSize);

		Page<ActiveControlRule> page = new Page<ActiveControlRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}