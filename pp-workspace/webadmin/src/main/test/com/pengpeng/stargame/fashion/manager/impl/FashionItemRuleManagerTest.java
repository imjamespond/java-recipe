package com.pengpeng.stargame.fashion.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.fashion.dao.IFashionItemRuleDao;
import com.pengpeng.stargame.fashion.manager.IFashionItemRuleManager;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FashionItemRuleManagerTest implements IFashionItemRuleManager{
	private static final Log log = LogFactory.getLog(FashionItemRuleManagerTest.class);
	
	@Autowired
	private IFashionItemRuleDao fashionItemRuleDao;
	public FashionItemRule createBean(FashionItemRule fashionItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+fashionItemRule);
		}		
		fashionItemRuleDao.createBean(fashionItemRule);
		return fashionItemRule;
	}

	@Override	
	public void updateBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+fashionItemRule);
		}
		fashionItemRuleDao.updateBean(fashionItemRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		fashionItemRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+fashionItemRule);
		}
		fashionItemRuleDao.removeBean(fashionItemRule);
	}
	
	@Override	
	public Page<FashionItemRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FashionItemRule> list = fashionItemRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FashionItemRule> page = new Page<FashionItemRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FashionItemRule findById(Serializable id) throws NotFoundBeanException{
		return fashionItemRuleDao.findById(id);
	}
}