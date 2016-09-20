package com.pengpeng.admin.qinma.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.qinma.dao.IQinMaFarmRuleDao;
import com.pengpeng.stargame.qinma.rule.QinMaFarmRule;
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
@Repository(value = "qinMaFarmRuleDao")
public class QinMaFarmRuleDaoImpl implements IQinMaFarmRuleDao{
	private static final Log log = LogFactory.getLog(QinMaFarmRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(QinMaFarmRule qinMaFarmRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+qinMaFarmRule);
		}
		try{
			findById(qinMaFarmRule.getId());
		}catch(NotFoundBeanException e){
            template.save(qinMaFarmRule);
		}		
	}
	
	@Override
	public void updateBean(QinMaFarmRule qinMaFarmRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+qinMaFarmRule);
		}
		findById(qinMaFarmRule.getId());
        template.update(qinMaFarmRule);
	}
	
	@Override
	public void removeBean(QinMaFarmRule qinMaFarmRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+qinMaFarmRule);
		}
		findById(qinMaFarmRule.getId());
        template.delete(qinMaFarmRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        QinMaFarmRule qinMaFarmRule = findById(id);
        template.delete(qinMaFarmRule);
	}

	@Override
	public QinMaFarmRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		QinMaFarmRule qinMaFarmRule = (QinMaFarmRule)template.get(QinMaFarmRule.class, id);;
		if (null==qinMaFarmRule)
			throw new NotFoundBeanException();
		return qinMaFarmRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QinMaFarmRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <QinMaFarmRule> list = (List<QinMaFarmRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QinMaFarmRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <QinMaFarmRule> list = (List<QinMaFarmRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}