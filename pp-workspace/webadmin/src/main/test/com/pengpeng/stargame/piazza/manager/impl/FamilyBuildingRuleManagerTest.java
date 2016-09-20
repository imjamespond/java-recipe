package com.pengpeng.stargame.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.stargame.piazza.dao.IFamilyBuildingRuleDao;
import com.pengpeng.stargame.piazza.manager.IFamilyBuildingRuleManager;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;

/**
 * 生成模版 managerImpl.vm
 * @author fangyaoxia
 */
public class FamilyBuildingRuleManagerTest implements IFamilyBuildingRuleManager{
	private static final Log log = LogFactory.getLog(FamilyBuildingRuleManagerTest.class);
	
	@Autowired
	private IFamilyBuildingRuleDao familyBuildingRuleDao;
	public FamilyBuildingRule createBean(FamilyBuildingRule familyBuildingRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+familyBuildingRule);
		}		
		familyBuildingRuleDao.createBean(familyBuildingRule);
		return familyBuildingRule;
	}

	@Override	
	public void updateBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+familyBuildingRule);
		}
		familyBuildingRuleDao.updateBean(familyBuildingRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		familyBuildingRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+familyBuildingRule);
		}
		familyBuildingRuleDao.removeBean(familyBuildingRule);
	}
	
	@Override	
	public Page<FamilyBuildingRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		int start = (pageNo-1)*pageSize;
		List<FamilyBuildingRule> list = familyBuildingRuleDao.findPages("find_pages",params,start,start+pageSize);

		Page<FamilyBuildingRule> page = new Page<FamilyBuildingRule>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setItems(list);
		return page;
	}
	
	@Override	
	public FamilyBuildingRule findById(Serializable id) throws NotFoundBeanException{
		return familyBuildingRuleDao.findById(id);
	}
}