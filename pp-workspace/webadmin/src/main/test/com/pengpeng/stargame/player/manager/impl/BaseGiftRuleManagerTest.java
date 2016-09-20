package com.pengpeng.stargame.player.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.player.dao.IBaseGiftRuleDao;
import com.pengpeng.stargame.player.manager.IBaseGiftRuleManager;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class BaseGiftRuleManagerTest implements IBaseGiftRuleManager{
	private static final Log log = LogFactory.getLog(BaseGiftRuleManagerTest.class);
	
	@Autowired
	private IBaseGiftRuleDao baseGiftRuleDao;
	public BaseGiftRule createBean(BaseGiftRule baseGiftRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+baseGiftRule);
		}		
		baseGiftRuleDao.createBean(baseGiftRule);
		return baseGiftRule;
	}

	@Override	
	public void updateBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+baseGiftRule);
		}
		baseGiftRuleDao.updateBean(baseGiftRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		baseGiftRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+baseGiftRule);
		}
		baseGiftRuleDao.removeBean(baseGiftRule);
	}
	
	@Override	
	public Page<BaseGiftRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<BaseGiftRule> list = baseGiftRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<BaseGiftRule> page = new Page<BaseGiftRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public BaseGiftRule findById(Serializable id) throws NotFoundBeanException{
		return baseGiftRuleDao.findById(id);
	}
}