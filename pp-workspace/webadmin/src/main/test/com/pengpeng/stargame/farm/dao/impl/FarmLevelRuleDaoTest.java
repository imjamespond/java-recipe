package com.pengpeng.stargame.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.farm.dao.IFarmLevelRuleDao;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FarmLevelRuleDaoTest implements IFarmLevelRuleDao{
	private static final Log log = LogFactory.getLog(FarmLevelRuleDaoTest.class);

	@Override
	public void createBean(FarmLevelRule farmLevelRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmLevelRule);
		}
		try{
			findById(farmLevelRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmLevelRule);
		}
		findById(farmLevelRule.getId());
	}
	
	@Override
	public void removeBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmLevelRule);
		}
		findById(farmLevelRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FarmLevelRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmLevelRule farmLevelRule = null;
		return farmLevelRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmLevelRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FarmLevelRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmLevelRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FarmLevelRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFarmLevelRuleDao dao = ctx.getBean(IFarmLevelRuleDao.class);
		 FarmLevelRule farmLevelRule = new FarmLevelRule();
		 dao.createBean(farmLevelRule);
    
    }	
}