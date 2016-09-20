package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IIdentityRuleDao;
import com.pengpeng.stargame.piazza.rule.IdentityRule;
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
@Repository(value = "identityRuleDao")
public class IdentityRuleDaoImpl implements IIdentityRuleDao{
	private static final Log log = LogFactory.getLog(IdentityRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(IdentityRule identityRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+identityRule);
		}
		try{
			findById(identityRule.getId());
		}catch(NotFoundBeanException e){
            template.save(identityRule);
		}		
	}
	
	@Override
	public void updateBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+identityRule);
		}
		findById(identityRule.getId());
        template.update(identityRule);
	}
	
	@Override
	public void removeBean(IdentityRule identityRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+identityRule);
		}
		findById(identityRule.getId());
        template.delete(identityRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        IdentityRule identityRule = findById(id);
        template.delete(identityRule);
	}

	@Override
	public IdentityRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		IdentityRule identityRule = (IdentityRule)template.get(IdentityRule.class, id);;
		if (null==identityRule)
			throw new NotFoundBeanException();
		return identityRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IdentityRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <IdentityRule> list = (List<IdentityRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IdentityRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <IdentityRule> list = (List<IdentityRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}