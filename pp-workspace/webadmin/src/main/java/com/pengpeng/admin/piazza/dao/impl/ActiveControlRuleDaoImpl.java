package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IActiveControlRuleDao;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
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
@Repository(value = "activeControlRuleDao")
public class ActiveControlRuleDaoImpl implements IActiveControlRuleDao{
	private static final Log log = LogFactory.getLog(ActiveControlRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(ActiveControlRule activeControlRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+activeControlRule);
		}
		try{
			findById(activeControlRule.getId());
		}catch(NotFoundBeanException e){
            template.save(activeControlRule);
		}		
	}
	
	@Override
	public void updateBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+activeControlRule);
		}
		findById(activeControlRule.getId());
        template.update(activeControlRule);
	}
	
	@Override
	public void removeBean(ActiveControlRule activeControlRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+activeControlRule);
		}
		findById(activeControlRule.getId());
        template.delete(activeControlRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        ActiveControlRule activeControlRule = findById(id);
        template.delete(activeControlRule);
	}

	@Override
	public ActiveControlRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		ActiveControlRule activeControlRule = (ActiveControlRule)template.get(ActiveControlRule.class, id);;
		if (null==activeControlRule)
			throw new NotFoundBeanException();
		return activeControlRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveControlRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <ActiveControlRule> list = (List<ActiveControlRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveControlRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <ActiveControlRule> list = (List<ActiveControlRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}