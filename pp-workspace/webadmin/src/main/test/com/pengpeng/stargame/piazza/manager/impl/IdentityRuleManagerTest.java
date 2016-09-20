package com.pengpeng.stargame.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.piazza.dao.IIdentityRuleDao;
import com.pengpeng.stargame.piazza.manager.IIdentityRuleManager;
import com.pengpeng.stargame.piazza.rule.IdentityRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class IdentityRuleManagerTest implements IIdentityRuleManager{
	private static final Log log = LogFactory.getLog(IdentityRuleManagerTest.class);
	
	@Autowired
	private IIdentityRuleDao identityRuleDao;
	public IdentityRule createBean(IdentityRule identityRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+identityRule);
		}		
		identityRuleDao.createBean(identityRule);
		return identityRule;
	}

	@Override	
	public void updateBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+identityRule);
		}
		identityRuleDao.updateBean(identityRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		identityRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+identityRule);
		}
		identityRuleDao.removeBean(identityRule);
	}
	
	@Override	
	public Page<IdentityRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<IdentityRule> list = identityRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<IdentityRule> page = new Page<IdentityRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public IdentityRule findById(Serializable id) throws NotFoundBeanException{
		return identityRuleDao.findById(id);
	}
}