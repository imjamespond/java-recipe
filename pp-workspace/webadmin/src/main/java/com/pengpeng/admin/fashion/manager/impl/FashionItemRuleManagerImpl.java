package com.pengpeng.admin.fashion.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.fashion.dao.IFashionItemRuleDao;
import com.pengpeng.admin.fashion.manager.IFashionItemRuleManager;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "fashionItemRuleManager")
public class FashionItemRuleManagerImpl implements IFashionItemRuleManager{
	private static final Log log = LogFactory.getLog(FashionItemRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="fashionItemRuleDao")
	private IFashionItemRuleDao fashionItemRuleDao;

	@Override	
	public FashionItemRule findById(Serializable id) throws NotFoundBeanException{
		return fashionItemRuleDao.findById(id);
	}

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
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<FashionItemRule> list = fashionItemRuleDao.findPages("from FashionItemRule a",map,start,start+pageSize);

		Page<FashionItemRule> page = new Page<FashionItemRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}