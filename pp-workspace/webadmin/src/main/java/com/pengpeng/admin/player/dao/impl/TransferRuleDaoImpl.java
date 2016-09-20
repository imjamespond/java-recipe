package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.ITransferRuleDao;
import com.pengpeng.stargame.player.rule.TransferRule;
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
@Repository(value = "transferRuleDao")
public class TransferRuleDaoImpl implements ITransferRuleDao{
	private static final Log log = LogFactory.getLog(TransferRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(TransferRule transferRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+transferRule);
		}
		try{
			findById(transferRule.getId());
		}catch(NotFoundBeanException e){
            template.save(transferRule);
		}		
	}
	
	@Override
	public void updateBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+transferRule);
		}
		findById(transferRule.getId());
        template.update(transferRule);
	}
	
	@Override
	public void removeBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+transferRule);
		}
		findById(transferRule.getId());
        template.delete(transferRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        TransferRule transferRule = findById(id);
        template.delete(transferRule);
	}

	@Override
	public TransferRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		TransferRule transferRule = (TransferRule)template.get(TransferRule.class, id);;
		if (null==transferRule)
			throw new NotFoundBeanException();
		return transferRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransferRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <TransferRule> list = (List<TransferRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransferRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <TransferRule> list = (List<TransferRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}