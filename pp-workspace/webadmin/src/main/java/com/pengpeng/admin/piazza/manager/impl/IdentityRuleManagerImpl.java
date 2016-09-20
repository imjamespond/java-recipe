package com.pengpeng.admin.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.piazza.dao.IIdentityRuleDao;
import com.pengpeng.admin.piazza.manager.IIdentityRuleManager;
import com.pengpeng.stargame.piazza.rule.IdentityRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "identityRuleManager")
public class IdentityRuleManagerImpl implements IIdentityRuleManager{
	private static final Log log = LogFactory.getLog(IdentityRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="identityRuleDao")
	private IIdentityRuleDao identityRuleDao;

	@Override	
	public IdentityRule findById(Serializable id) throws NotFoundBeanException{
		return identityRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<IdentityRule> list = identityRuleDao.findPages("from IdentityRule a",map,start,start+pageSize);

		Page<IdentityRule> page = new Page<IdentityRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}