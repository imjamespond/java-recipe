package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IMoneyTreeRuleDao;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
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
@Repository(value = "moneyTreeRuleDao")
public class MoneyTreeRuleDaoImpl implements IMoneyTreeRuleDao{
	private static final Log log = LogFactory.getLog(MoneyTreeRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(MoneyTreeRule moneyTreeRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+moneyTreeRule);
		}
		try{
			findById(moneyTreeRule.getId());
		}catch(NotFoundBeanException e){
            template.save(moneyTreeRule);
		}		
	}
	
	@Override
	public void updateBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+moneyTreeRule);
		}
		findById(moneyTreeRule.getId());
        template.update(moneyTreeRule);
	}
	
	@Override
	public void removeBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+moneyTreeRule);
		}
		findById(moneyTreeRule.getId());
        template.delete(moneyTreeRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        MoneyTreeRule moneyTreeRule = findById(id);
        template.delete(moneyTreeRule);
	}

	@Override
	public MoneyTreeRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		MoneyTreeRule moneyTreeRule = (MoneyTreeRule)template.get(MoneyTreeRule.class, id);;
		if (null==moneyTreeRule)
			throw new NotFoundBeanException();
		return moneyTreeRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MoneyTreeRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <MoneyTreeRule> list = (List<MoneyTreeRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MoneyTreeRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <MoneyTreeRule> list = (List<MoneyTreeRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}