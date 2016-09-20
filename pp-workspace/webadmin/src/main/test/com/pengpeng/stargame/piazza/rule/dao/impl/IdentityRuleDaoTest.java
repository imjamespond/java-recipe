package com.pengpeng.stargame.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.piazza.dao.IIdentityRuleDao;
import com.pengpeng.stargame.piazza.bean.IdentityRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class IdentityRuleDaoTest implements IIdentityRuleDao{
	private static final Log log = LogFactory.getLog(IdentityRuleDaoTest.class);

	@Override
	public void createBean(IdentityRule identityRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+identityRule);
		}
		try{
			findById(identityRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+identityRule);
		}
		findById(identityRule.getId());
	}
	
	@Override
	public void removeBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+identityRule);
		}
		findById(identityRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public IdentityRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		IdentityRule identityRule = null;
		return identityRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IdentityRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<IdentityRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IdentityRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<IdentityRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IIdentityRuleDao dao = ctx.getBean(IIdentityRuleDao.class);
		 IdentityRule identityRule = new IdentityRule();
		 dao.createBean(identityRule);
    
    }	
}