package com.pengpeng.admin.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.farm.dao.IFarmSeedRuleDao;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
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
@Repository(value = "farmSeedRuleDao")
public class FarmSeedRuleDaoImpl implements IFarmSeedRuleDao{
	private static final Log log = LogFactory.getLog(FarmSeedRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FarmSeedRule farmSeedRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmSeedRule);
		}
		try{
			findById(farmSeedRule.getId());
		}catch(NotFoundBeanException e){
            template.save(farmSeedRule);
		}		
	}
	
	@Override
	public void updateBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmSeedRule);
		}
		findById(farmSeedRule.getId());
        template.update(farmSeedRule);
	}
	
	@Override
	public void removeBean(FarmSeedRule farmSeedRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmSeedRule);
		}
		findById(farmSeedRule.getId());
        template.delete(farmSeedRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FarmSeedRule farmSeedRule = findById(id);
        template.delete(farmSeedRule);
	}

	@Override
	public FarmSeedRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmSeedRule farmSeedRule = (FarmSeedRule)template.get(FarmSeedRule.class, id);;
		if (null==farmSeedRule)
			throw new NotFoundBeanException();
		return farmSeedRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmSeedRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmSeedRule> list = (List<FarmSeedRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmSeedRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmSeedRule> list = (List<FarmSeedRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}