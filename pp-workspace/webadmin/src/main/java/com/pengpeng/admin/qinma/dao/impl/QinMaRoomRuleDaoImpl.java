package com.pengpeng.admin.qinma.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.qinma.dao.IQinMaRoomRuleDao;
import com.pengpeng.stargame.qinma.rule.QinMaRoomRule;
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
@Repository(value = "qinMaRoomRuleDao")
public class QinMaRoomRuleDaoImpl implements IQinMaRoomRuleDao{
	private static final Log log = LogFactory.getLog(QinMaRoomRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(QinMaRoomRule qinMaRoomRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+qinMaRoomRule);
		}
		try{
			findById(qinMaRoomRule.getId());
		}catch(NotFoundBeanException e){
            template.save(qinMaRoomRule);
		}		
	}
	
	@Override
	public void updateBean(QinMaRoomRule qinMaRoomRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+qinMaRoomRule);
		}
		findById(qinMaRoomRule.getId());
        template.update(qinMaRoomRule);
	}
	
	@Override
	public void removeBean(QinMaRoomRule qinMaRoomRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+qinMaRoomRule);
		}
		findById(qinMaRoomRule.getId());
        template.delete(qinMaRoomRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        QinMaRoomRule qinMaRoomRule = findById(id);
        template.delete(qinMaRoomRule);
	}

	@Override
	public QinMaRoomRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		QinMaRoomRule qinMaRoomRule = (QinMaRoomRule)template.get(QinMaRoomRule.class, id);;
		if (null==qinMaRoomRule)
			throw new NotFoundBeanException();
		return qinMaRoomRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QinMaRoomRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <QinMaRoomRule> list = (List<QinMaRoomRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QinMaRoomRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <QinMaRoomRule> list = (List<QinMaRoomRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}