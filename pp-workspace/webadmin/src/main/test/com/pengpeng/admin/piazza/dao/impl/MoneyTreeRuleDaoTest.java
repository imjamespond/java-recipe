package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IMoneyTreeRuleDao;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class MoneyTreeRuleDaoTest implements IMoneyTreeRuleDao{
	private static final Log log = LogFactory.getLog(MoneyTreeRuleDaoTest.class);

	@Override
	public void createBean(MoneyTreeRule moneyTreeRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+moneyTreeRule);
		}
		try{
			findById(moneyTreeRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+moneyTreeRule);
		}
		findById(moneyTreeRule.getId());
	}
	
	@Override
	public void removeBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+moneyTreeRule);
		}
		findById(moneyTreeRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public MoneyTreeRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		MoneyTreeRule moneyTreeRule = null;
		return moneyTreeRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MoneyTreeRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<MoneyTreeRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MoneyTreeRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<MoneyTreeRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IMoneyTreeRuleDao dao = ctx.getBean(IMoneyTreeRuleDao.class);
		 MoneyTreeRule moneyTreeRule = new MoneyTreeRule();
		 dao.createBean(moneyTreeRule);
    
    }	
}