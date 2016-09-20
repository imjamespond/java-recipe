package com.pengpeng.admin.player.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface ISceneRuleManager{
	
	public SceneRule createBean(SceneRule sceneRule) throws BeanAreadyException;
	
	public void updateBean(SceneRule sceneRule) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(SceneRule sceneRule) throws NotFoundBeanException;
	
	public SceneRule findById(Serializable id) throws NotFoundBeanException;
	
	public Page<SceneRule> findPages(Map<String,Object> params,int pageNo,int pageSize);

}