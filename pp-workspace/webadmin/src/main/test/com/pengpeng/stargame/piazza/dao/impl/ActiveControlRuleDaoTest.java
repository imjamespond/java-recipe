package com.pengpeng.stargame.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.piazza.dao.IActiveControlRuleDao;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class ActiveControlRuleDaoTest implements IActiveControlRuleDao{
	private static final Log log = LogFactory.getLog(ActiveControlRuleDaoTest.class);

	@Override
	public void createBean(ActiveControlRule activeControlRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+activeControlRule);
		}
		try{
			findById(activeControlRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+activeControlRule);
		}
		findById(activeControlRule.getId());
	}
	
	@Override
	public void removeBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+activeControlRule);
		}
		findById(activeControlRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public ActiveControlRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		ActiveControlRule activeControlRule = null;
		return activeControlRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveControlRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<ActiveControlRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveControlRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<ActiveControlRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IActiveControlRuleDao dao = ctx.getBean(IActiveControlRuleDao.class);
		 ActiveControlRule activeControlRule = new ActiveControlRule();
		 dao.createBean(activeControlRule);
    
    }	
}