package com.pengpeng.stargame.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.player.dao.ISceneRuleDao;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;
/**
 * 生成模版 daoImpl.vm
 * @author fangyaoxia
 */
public class SceneRuleDaoTest implements ISceneRuleDao{
	private static final Log log = LogFactory.getLog(SceneRuleDaoTest.class);

	@Override
	public void createBean(SceneRule sceneRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+sceneRule);
		}
		try{
			findById(sceneRule.getId());
		}catch(NotFoundBeanException e){

		}		
	}
	
	@Override
	public void updateBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+sceneRule);
		}
		findById(sceneRule.getId());
	}
	
	@Override
	public void removeBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+sceneRule);
		}
		findById(sceneRule.getId());
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		findById(id);
	}

	@Override
	public SceneRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		SceneRule sceneRule = null;
		return sceneRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SceneRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
		List<SceneRule> list = null;
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SceneRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
		List<SceneRule> list = null;
		return list;
	}
	
    public static void main(String[] args) throws Exception{
    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.tongyi") ;
		 ISceneRuleDao dao = ctx.getBean(ISceneRuleDao.class);
		 SceneRule sceneRule = new SceneRule();
		 dao.createBean(sceneRule);
    
    }	
}