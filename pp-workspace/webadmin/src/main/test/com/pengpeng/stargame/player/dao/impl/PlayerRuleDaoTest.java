package com.pengpeng.stargame.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.player.dao.IPlayerRuleDao;
import com.pengpeng.stargame.player.rule.PlayerRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class PlayerRuleDaoTest implements IPlayerRuleDao{
	private static final Log log = LogFactory.getLog(PlayerRuleDaoTest.class);

	@Override
	public void createBean(PlayerRule playerRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerRule);
		}
		try{
			findById(playerRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerRule);
		}
		findById(playerRule.getId());
	}
	
	@Override
	public void removeBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerRule);
		}
		findById(playerRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public PlayerRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		PlayerRule playerRule = null;
		return playerRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<PlayerRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<PlayerRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IPlayerRuleDao dao = ctx.getBean(IPlayerRuleDao.class);
		 PlayerRule playerRule = new PlayerRule();
		 dao.createBean(playerRule);
    
    }	
}