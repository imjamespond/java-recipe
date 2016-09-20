package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IFamilyBuildingRuleDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FamilyBuildingRuleDaoTest implements IFamilyBuildingRuleDao{
	private static final Log log = LogFactory.getLog(FamilyBuildingRuleDaoTest.class);

	@Override
	public void createBean(FamilyBuildingRule familyBuildingRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+familyBuildingRule);
		}
		try{
			findById(familyBuildingRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+familyBuildingRule);
		}
		findById(familyBuildingRule.getId());
	}
	
	@Override
	public void removeBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+familyBuildingRule);
		}
		findById(familyBuildingRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FamilyBuildingRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FamilyBuildingRule familyBuildingRule = null;
		return familyBuildingRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyBuildingRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FamilyBuildingRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyBuildingRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FamilyBuildingRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFamilyBuildingRuleDao dao = ctx.getBean(IFamilyBuildingRuleDao.class);
		 FamilyBuildingRule familyBuildingRule = new FamilyBuildingRule();
		 dao.createBean(familyBuildingRule);
    
    }	
}