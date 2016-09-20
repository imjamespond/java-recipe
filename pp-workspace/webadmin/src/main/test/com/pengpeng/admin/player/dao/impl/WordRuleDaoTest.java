package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.IWordRuleDao;
import com.pengpeng.stargame.player.rule.WordRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class WordRuleDaoTest implements IWordRuleDao{
	private static final Log log = LogFactory.getLog(WordRuleDaoTest.class);

	@Override
	public void createBean(WordRule wordRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+wordRule);
		}
		try{
			findById(wordRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+wordRule);
		}
		findById(wordRule.getId());
	}
	
	@Override
	public void removeBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+wordRule);
		}
		findById(wordRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public WordRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		WordRule wordRule = null;
		return wordRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WordRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<WordRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WordRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<WordRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IWordRuleDao dao = ctx.getBean(IWordRuleDao.class);
		 WordRule wordRule = new WordRule();
		 dao.createBean(wordRule);
    
    }	
}