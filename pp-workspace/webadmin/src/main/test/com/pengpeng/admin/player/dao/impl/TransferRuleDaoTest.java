package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.ITransferRuleDao;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class TransferRuleDaoTest implements ITransferRuleDao{
	private static final Log log = LogFactory.getLog(TransferRuleDaoTest.class);

	@Override
	public void createBean(TransferRule transferRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+transferRule);
		}
		try{
			findById(transferRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+transferRule);
		}
		findById(transferRule.getId());
	}
	
	@Override
	public void removeBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+transferRule);
		}
		findById(transferRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public TransferRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		TransferRule transferRule = null;
		return transferRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransferRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<TransferRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransferRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<TransferRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 ITransferRuleDao dao = ctx.getBean(ITransferRuleDao.class);
		 TransferRule transferRule = new TransferRule();
		 dao.createBean(transferRule);
    
    }	
}