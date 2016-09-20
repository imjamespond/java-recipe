package com.pengpeng.admin.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.farm.dao.IFarmLevelRuleDao;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
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
@Repository(value = "farmLevelRuleDao")
public class FarmLevelRuleDaoImpl implements IFarmLevelRuleDao{
	private static final Log log = LogFactory.getLog(FarmLevelRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FarmLevelRule farmLevelRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmLevelRule);
		}
		try{
			findById(farmLevelRule.getId());
		}catch(NotFoundBeanException e){
            template.save(farmLevelRule);
		}		
	}
	
	@Override
	public void updateBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmLevelRule);
		}
		findById(farmLevelRule.getId());
        template.update(farmLevelRule);
	}
	
	@Override
	public void removeBean(FarmLevelRule farmLevelRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmLevelRule);
		}
		findById(farmLevelRule.getId());
        template.delete(farmLevelRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FarmLevelRule farmLevelRule = findById(id);
        template.delete(farmLevelRule);
	}

	@Override
	public FarmLevelRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmLevelRule farmLevelRule = (FarmLevelRule)template.get(FarmLevelRule.class, id);;
		if (null==farmLevelRule)
			throw new NotFoundBeanException();
		return farmLevelRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmLevelRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmLevelRule> list = (List<FarmLevelRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmLevelRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmLevelRule> list = (List<FarmLevelRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}