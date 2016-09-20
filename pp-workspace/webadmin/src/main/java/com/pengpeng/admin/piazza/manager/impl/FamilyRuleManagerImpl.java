package com.pengpeng.admin.piazza.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.piazza.dao.IFamilyRuleDao;
import com.pengpeng.admin.piazza.manager.IFamilyRuleManager;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "familyRuleManager")
public class FamilyRuleManagerImpl implements IFamilyRuleManager{
	private static final Log log = LogFactory.getLog(FamilyRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="familyRuleDao")
	private IFamilyRuleDao familyRuleDao;

	@Override	
	public FamilyRule findById(Serializable id) throws NotFoundBeanException{
		return familyRuleDao.findById(id);
	}

	public FamilyRule createBean(FamilyRule familyRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+familyRule);
		}		
		familyRuleDao.createBean(familyRule);
		return familyRule;
	}

	@Override	
	public void updateBean(FamilyRule familyRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+familyRule);
		}
		familyRuleDao.updateBean(familyRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		familyRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(FamilyRule familyRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+familyRule);
		}
		familyRuleDao.removeBean(familyRule);
	}
	
	@Override	
	public Page<FamilyRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<FamilyRule> list = familyRuleDao.findPages("from FamilyRule a",map,start,start+pageSize);

		Page<FamilyRule> page = new Page<FamilyRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}