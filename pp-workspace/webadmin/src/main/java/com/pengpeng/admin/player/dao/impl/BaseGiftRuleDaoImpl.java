package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.IBaseGiftRuleDao;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
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
@Repository(value = "baseGiftRuleDao")
public class BaseGiftRuleDaoImpl implements IBaseGiftRuleDao{
	private static final Log log = LogFactory.getLog(BaseGiftRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(BaseGiftRule baseGiftRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+baseGiftRule);
		}
		try{
			findById(baseGiftRule.getId());
		}catch(NotFoundBeanException e){
            template.save(baseGiftRule);
		}		
	}
	
	@Override
	public void updateBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+baseGiftRule);
		}
		findById(baseGiftRule.getId());
        template.update(baseGiftRule);
	}
	
	@Override
	public void removeBean(BaseGiftRule baseGiftRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+baseGiftRule);
		}
		findById(baseGiftRule.getId());
        template.delete(baseGiftRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        BaseGiftRule baseGiftRule = findById(id);
        template.delete(baseGiftRule);
	}

	@Override
	public BaseGiftRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		BaseGiftRule baseGiftRule = (BaseGiftRule)template.get(BaseGiftRule.class, id);;
		if (null==baseGiftRule)
			throw new NotFoundBeanException();
		return baseGiftRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseGiftRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <BaseGiftRule> list = (List<BaseGiftRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseGiftRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <BaseGiftRule> list = (List<BaseGiftRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}