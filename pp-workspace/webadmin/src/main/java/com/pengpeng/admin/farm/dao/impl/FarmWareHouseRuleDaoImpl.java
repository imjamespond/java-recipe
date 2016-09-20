package com.pengpeng.admin.farm.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.farm.dao.IFarmWareHouseRuleDao;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
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
@Repository(value = "farmWareHouseRuleDao")
public class FarmWareHouseRuleDaoImpl implements IFarmWareHouseRuleDao{
	private static final Log log = LogFactory.getLog(FarmWareHouseRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FarmWareHouseRule farmWareHouseRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+farmWareHouseRule);
		}
		try{
			findById(farmWareHouseRule.getId());
		}catch(NotFoundBeanException e){
            template.save(farmWareHouseRule);
		}		
	}
	
	@Override
	public void updateBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+farmWareHouseRule);
		}
		findById(farmWareHouseRule.getId());
        template.update(farmWareHouseRule);
	}
	
	@Override
	public void removeBean(FarmWareHouseRule farmWareHouseRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+farmWareHouseRule);
		}
		findById(farmWareHouseRule.getId());
        template.delete(farmWareHouseRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FarmWareHouseRule farmWareHouseRule = findById(id);
        template.delete(farmWareHouseRule);
	}

	@Override
	public FarmWareHouseRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FarmWareHouseRule farmWareHouseRule = (FarmWareHouseRule)template.get(FarmWareHouseRule.class, id);;
		if (null==farmWareHouseRule)
			throw new NotFoundBeanException();
		return farmWareHouseRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmWareHouseRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmWareHouseRule> list = (List<FarmWareHouseRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FarmWareHouseRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FarmWareHouseRule> list = (List<FarmWareHouseRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}