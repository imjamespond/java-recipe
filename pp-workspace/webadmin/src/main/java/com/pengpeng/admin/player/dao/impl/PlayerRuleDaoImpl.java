package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.IPlayerRuleDao;
import com.pengpeng.stargame.player.rule.PlayerRule;
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
@Repository(value = "playerRuleDao")
public class PlayerRuleDaoImpl implements IPlayerRuleDao{
	private static final Log log = LogFactory.getLog(PlayerRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(PlayerRule playerRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerRule);
		}
		try{
			findById(playerRule.getId());
		}catch(NotFoundBeanException e){
            template.save(playerRule);
		}		
	}
	
	@Override
	public void updateBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerRule);
		}
		findById(playerRule.getId());
        template.update(playerRule);
	}
	
	@Override
	public void removeBean(PlayerRule playerRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerRule);
		}
		findById(playerRule.getId());
        template.delete(playerRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        PlayerRule playerRule = findById(id);
        template.delete(playerRule);
	}

	@Override
	public PlayerRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		PlayerRule playerRule = (PlayerRule)template.get(PlayerRule.class, id);;
		if (null==playerRule)
			throw new NotFoundBeanException();
		return playerRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <PlayerRule> list = (List<PlayerRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <PlayerRule> list = (List<PlayerRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}