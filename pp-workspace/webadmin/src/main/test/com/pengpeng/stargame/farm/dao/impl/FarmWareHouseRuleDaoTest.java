package com.pengpeng.stargame.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.farm.dao.IFarmWareHouseRuleDao;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FarmWareHouseRuleDaoTest implements IFarmWareHouseRuleDao{
	private static final Log log = LogFactory.getLog(FarmWareHouseRuleDaoTest.class);

	@Override
	public void createBean(FarmWareHouseRule farmWareHouseRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmWareHouseRule);
		}
		try{
			findById(farmWareHouseRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmWareHouseRule);
		}
		findById(farmWareHouseRule.getId());
	}
	
	@Override
	public void removeBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmWareHouseRule);
		}
		findById(farmWareHouseRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FarmWareHouseRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmWareHouseRule farmWareHouseRule = null;
		return farmWareHouseRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmWareHouseRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FarmWareHouseRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmWareHouseRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FarmWareHouseRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFarmWareHouseRuleDao dao = ctx.getBean(IFarmWareHouseRuleDao.class);
		 FarmWareHouseRule farmWareHouseRule = new FarmWareHouseRule();
		 dao.createBean(farmWareHouseRule);
    
    }	
}