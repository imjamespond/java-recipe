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
import org.springframework.orm.hibernate3.HibernateTemplate;
import java.util.Map;
import com.tongyi.dao.IBaseDao;
import java.util.ArrayList;
import java.util.List;

/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "roomItemRuleDao")
public class RoomItemRuleDaoImpl implements IRoomItemRuleDao{
	private static final Log log = LogFactory.getLog(RoomItemRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(RoomItemRule roomItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+roomItemRule);
		}
		try{
			findById(roomItemRule.getId());
		}catch(NotFoundBeanException e){
            template.save(roomItemRule);
		}		
	}
	
	@Override
	public void updateBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+roomItemRule);
		}
		findById(roomItemRule.getId());
        template.update(roomItemRule);
	}
	
	@Override
	public void removeBean(RoomItemRule roomItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+roomItemRule);
		}
		findById(roomItemRule.getId());
        template.delete(roomItemRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        RoomItemRule roomItemRule = findById(id);
        template.delete(roomItemRule);
	}

	@Override
	public RoomItemRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		RoomItemRule roomItemRule = (RoomItemRule)template.get(RoomItemRule.class, id);;
		if (null==roomItemRule)
			throw new NotFoundBeanException();
		return roomItemRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoomItemRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <RoomItemRule> list = (List<RoomItemRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoomItemRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <RoomItemRule> list = (List<RoomItemRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}