package com.pengpeng.stargame.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.farm.dao.IFarmOrderRuleDao;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FarmOrderRuleDaoTest implements IFarmOrderRuleDao{
	private static final Log log = LogFactory.getLog(FarmOrderRuleDaoTest.class);

	@Override
	public void createBean(FarmOrderRule farmOrderRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmOrderRule);
		}
		try{
			findById(farmOrderRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmOrderRule);
		}
		findById(farmOrderRule.getId());
	}
	
	@Override
	public void removeBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmOrderRule);
		}
		findById(farmOrderRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FarmOrderRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmOrderRule farmOrderRule = null;
		return farmOrderRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmOrderRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FarmOrderRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmOrderRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FarmOrderRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFarmOrderRuleDao dao = ctx.getBean(IFarmOrderRuleDao.class);
		 FarmOrderRule farmOrderRule = new FarmOrderRule();
		 dao.createBean(farmOrderRule);
    
    }	
}