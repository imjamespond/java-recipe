package com.pengpeng.admin.player.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.player.dao.ISceneRuleDao;
import com.pengpeng.admin.player.manager.ISceneRuleManager;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "sceneRuleManager")
public class SceneRuleManagerImpl implements ISceneRuleManager{
	private static final Log log = LogFactory.getLog(SceneRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="sceneRuleDao")
	private ISceneRuleDao sceneRuleDao;

	@Override	
	public SceneRule findById(Serializable id) throws NotFoundBeanException{
		return sceneRuleDao.findById(id);
	}

	public SceneRule createBean(SceneRule sceneRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+sceneRule);
		}		
		sceneRuleDao.createBean(sceneRule);
		return sceneRule;
	}

	@Override	
	public void updateBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+sceneRule);
		}
		sceneRuleDao.updateBean(sceneRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		sceneRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(SceneRule sceneRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+sceneRule);
		}
		sceneRuleDao.removeBean(sceneRule);
	}
	
	@Override	
	public Page<SceneRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<SceneRule> list = sceneRuleDao.findPages("from SceneRule a",map,start,start+pageSize);

		Page<SceneRule> page = new Page<SceneRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}