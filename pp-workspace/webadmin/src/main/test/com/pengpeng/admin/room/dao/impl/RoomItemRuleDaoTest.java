package com.pengpeng.admin.room.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.room.dao.IRoomItemRuleDao;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class RoomItemRuleDaoTest implements IRoomItemRuleDao{
	private static final Log log = LogFactory.getLog(RoomItemRuleDaoTest.class);

	@Override
	public void createBean(RoomItemRule roomItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+roomItemRule);
		}
		try{
			findById(roomItemRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+roomItemRule);
		}
		findById(roomItemRule.getId());
	}
	
	@Override
	public void removeBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+roomItemRule);
		}
		findById(roomItemRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public RoomItemRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		RoomItemRule roomItemRule = null;
		return roomItemRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoomItemRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<RoomItemRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoomItemRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<RoomItemRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 IRoomItemRuleDao dao = ctx.getBean(IRoomItemRuleDao.class);
		 RoomItemRule roomItemRule = new RoomItemRule();
		 dao.createBean(roomItemRule);
    
    }	
}