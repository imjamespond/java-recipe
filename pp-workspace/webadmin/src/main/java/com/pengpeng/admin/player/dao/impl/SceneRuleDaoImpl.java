package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.ISceneRuleDao;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import java.util.Map;
import com.tongyi.dao.IBaseDao;
import java.util.ArrayList;
import java.util.List;

/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "sceneRuleDao")
public class SceneRuleDaoImpl implements ISceneRuleDao{
	private static final Log log = LogFactory.getLog(SceneRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(SceneRule sceneRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+sceneRule);
		}
		try{
			findById(sceneRule.getId());
		}catch(NotFoundBeanException e){
            template.save(sceneRule);
		}		
	}
	
	@Override
	public void updateBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+sceneRule);
		}
		findById(sceneRule.getId());
        template.update(sceneRule);
	}
	
	@Override
	public void removeBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+sceneRule);
		}
		findById(sceneRule.getId());
        template.delete(sceneRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        SceneRule sceneRule = findById(id);
        template.delete(sceneRule);
	}

	@Override
	public SceneRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		SceneRule sceneRule = (SceneRule)template.get(SceneRule.class, id);;
		if (null==sceneRule)
			throw new NotFoundBeanException();
		return sceneRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SceneRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <SceneRule> list = (List<SceneRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SceneRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <SceneRule> list = (List<SceneRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}