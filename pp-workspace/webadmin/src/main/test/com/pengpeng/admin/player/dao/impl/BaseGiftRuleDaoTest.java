package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.IBaseGiftRuleDao;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class BaseGiftRuleDaoTest implements IBaseGiftRuleDao{
	private static final Log log = LogFactory.getLog(BaseGiftRuleDaoTest.class);

	@Override
	public void createBean(BaseGiftRule baseGiftRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+baseGiftRule);
		}
		try{
			findById(baseGiftRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+baseGiftRule);
		}
		findById(baseGiftRule.getId());
	}
	
	@Override
	public void removeBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+baseGiftRule);
		}
		findById(baseGiftRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public BaseGiftRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		BaseGiftRule baseGiftRule = null;
		return baseGiftRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseGiftRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<BaseGiftRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseGiftRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<BaseGiftRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IBaseGiftRuleDao dao = ctx.getBean(IBaseGiftRuleDao.class);
		 BaseGiftRule baseGiftRule = new BaseGiftRule();
		 dao.createBean(baseGiftRule);
    
    }	
}