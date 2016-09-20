package com.pengpeng.admin.fashion.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.fashion.dao.IFashionItemRuleDao;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class FashionItemRuleDaoTest implements IFashionItemRuleDao{
	private static final Log log = LogFactory.getLog(FashionItemRuleDaoTest.class);

	@Override
	public void createBean(FashionItemRule fashionItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+fashionItemRule);
		}
		try{
			findById(fashionItemRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+fashionItemRule);
		}
		findById(fashionItemRule.getId());
	}
	
	@Override
	public void removeBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+fashionItemRule);
		}
		findById(fashionItemRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public FashionItemRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FashionItemRule fashionItemRule = null;
		return fashionItemRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FashionItemRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<FashionItemRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FashionItemRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<FashionItemRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IFashionItemRuleDao dao = ctx.getBean(IFashionItemRuleDao.class);
		 FashionItemRule fashionItemRule = new FashionItemRule();
		 dao.createBean(fashionItemRule);
    
    }	
}