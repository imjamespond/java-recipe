package com.pengpeng.admin.player.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.player.dao.IWordRuleDao;
import com.pengpeng.admin.player.manager.IWordRuleManager;
import com.pengpeng.stargame.player.rule.WordRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class WordRuleManagerTest implements IWordRuleManager{
	private static final Log log = LogFactory.getLog(WordRuleManagerTest.class);
	
	@Autowired
	private IWordRuleDao wordRuleDao;
	public WordRule createBean(WordRule wordRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+wordRule);
		}		
		wordRuleDao.createBean(wordRule);
		return wordRule;
	}

	@Override	
	public void updateBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+wordRule);
		}
		wordRuleDao.updateBean(wordRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		wordRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+wordRule);
		}
		wordRuleDao.removeBean(wordRule);
	}
	
	@Override	
	public Page<WordRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<WordRule> list = wordRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<WordRule> page = new Page<WordRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public WordRule findById(Serializable id) throws NotFoundBeanException{
		return wordRuleDao.findById(id);
	}
}