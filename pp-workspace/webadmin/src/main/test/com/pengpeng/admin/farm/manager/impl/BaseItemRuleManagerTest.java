package com.pengpeng.admin.farm.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.farm.dao.IBaseItemRuleDao;
import com.pengpeng.admin.farm.manager.IBaseItemRuleManager;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class BaseItemRuleManagerTest implements IBaseItemRuleManager{
	private static final Log log = LogFactory.getLog(BaseItemRuleManagerTest.class);
	
	@Autowired
	private IBaseItemRuleDao baseItemRuleDao;
	public BaseItemRule createBean(BaseItemRule baseItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+baseItemRule);
		}		
		baseItemRuleDao.createBean(baseItemRule);
		return baseItemRule;
	}

	@Override	
	public void updateBean(BaseItemRule baseItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+baseItemRule);
		}
		baseItemRuleDao.updateBean(baseItemRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		baseItemRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(BaseItemRule baseItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+baseItemRule);
		}
		baseItemRuleDao.removeBean(baseItemRule);
	}
	
	@Override	
	public Page<BaseItemRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<BaseItemRule> list = baseItemRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<BaseItemRule> page = new Page<BaseItemRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public BaseItemRule findById(Serializable id) throws NotFoundBeanException{
		return baseItemRuleDao.findById(id);
	}
}