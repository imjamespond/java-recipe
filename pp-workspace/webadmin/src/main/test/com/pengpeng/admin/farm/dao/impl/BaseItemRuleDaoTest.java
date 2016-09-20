package com.pengpeng.admin.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.farm.dao.IBaseItemRuleDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class BaseItemRuleDaoTest implements IBaseItemRuleDao{
	private static final Log log = LogFactory.getLog(BaseItemRuleDaoTest.class);

	@Override
	public void createBean(BaseItemRule baseItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+baseItemRule);
		}
		try{
			findById(baseItemRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(BaseItemRule baseItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+baseItemRule);
		}
		findById(baseItemRule.getId());
	}
	
	@Override
	public void removeBean(BaseItemRule baseItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+baseItemRule);
		}
		findById(baseItemRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public BaseItemRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		BaseItemRule baseItemRule = null;
		return baseItemRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseItemRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<BaseItemRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseItemRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<BaseItemRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IBaseItemRuleDao dao = ctx.getBean(IBaseItemRuleDao.class);
		 BaseItemRule baseItemRule = new BaseItemRule();
		 dao.createBean(baseItemRule);
    
    }	
}