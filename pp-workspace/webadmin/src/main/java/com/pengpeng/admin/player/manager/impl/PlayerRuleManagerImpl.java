package com.pengpeng.admin.player.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.player.dao.IPlayerRuleDao;
import com.pengpeng.admin.player.manager.IPlayerRuleManager;
import com.pengpeng.stargame.player.rule.PlayerRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "playerRuleManager")
public class PlayerRuleManagerImpl implements IPlayerRuleManager{
	private static final Log log = LogFactory.getLog(PlayerRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerRuleDao")
	private IPlayerRuleDao playerRuleDao;

	@Override	
	public PlayerRule findById(Serializable id) throws NotFoundBeanException{
		return playerRuleDao.findById(id);
	}

	public PlayerRule createBean(PlayerRule playerRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerRule);
		}		
		playerRuleDao.createBean(playerRule);
		return playerRule;
	}

	@Override	
	public void updateBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerRule);
		}
		playerRuleDao.updateBean(playerRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerRule);
		}
		playerRuleDao.removeBean(playerRule);
	}
	
	@Override	
	public Page<PlayerRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<PlayerRule> list = playerRuleDao.findPages("from PlayerRule a",map,start,start+pageSize);

		Page<PlayerRule> page = new Page<PlayerRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}