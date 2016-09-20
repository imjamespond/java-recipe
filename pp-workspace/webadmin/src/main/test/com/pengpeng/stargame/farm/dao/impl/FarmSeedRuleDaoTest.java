package com.pengpeng.stargame.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.farm.dao.IFarmSeedRuleDao;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FarmSeedRuleDaoTest implements IFarmSeedRuleDao{
	private static final Log log = LogFactory.getLog(FarmSeedRuleDaoTest.class);

	@Override
	public void createBean(FarmSeedRule farmSeedRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmSeedRule);
		}
		try{
			findById(farmSeedRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmSeedRule);
		}
		findById(farmSeedRule.getId());
	}
	
	@Override
	public void removeBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmSeedRule);
		}
		findById(farmSeedRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FarmSeedRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmSeedRule farmSeedRule = null;
		return farmSeedRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmSeedRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FarmSeedRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmSeedRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FarmSeedRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFarmSeedRuleDao dao = ctx.getBean(IFarmSeedRuleDao.class);
		 FarmSeedRule farmSeedRule = new FarmSeedRule();
		 dao.createBean(farmSeedRule);
    
    }	
}