package com.pengpeng.admin.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.farm.dao.IFarmOrderRuleDao;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
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
@Repository(value = "farmOrderRuleDao")
public class FarmOrderRuleDaoImpl implements IFarmOrderRuleDao{
	private static final Log log = LogFactory.getLog(FarmOrderRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FarmOrderRule farmOrderRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmOrderRule);
		}
		try{
			findById(farmOrderRule.getId());
		}catch(NotFoundBeanException e){
            template.save(farmOrderRule);
		}		
	}
	
	@Override
	public void updateBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmOrderRule);
		}
		findById(farmOrderRule.getId());
        template.update(farmOrderRule);
	}
	
	@Override
	public void removeBean(FarmOrderRule farmOrderRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmOrderRule);
		}
		findById(farmOrderRule.getId());
        template.delete(farmOrderRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FarmOrderRule farmOrderRule = findById(id);
        template.delete(farmOrderRule);
	}

	@Override
	public FarmOrderRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmOrderRule farmOrderRule = (FarmOrderRule)template.get(FarmOrderRule.class, id);;
		if (null==farmOrderRule)
			throw new NotFoundBeanException();
		return farmOrderRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmOrderRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmOrderRule> list = (List<FarmOrderRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmOrderRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmOrderRule> list = (List<FarmOrderRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}