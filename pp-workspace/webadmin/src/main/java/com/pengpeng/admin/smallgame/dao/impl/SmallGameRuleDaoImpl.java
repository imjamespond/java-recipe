package com.pengpeng.admin.smallgame.dao.impl;

import com.pengpeng.admin.smallgame.dao.ISmallGameRuleDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "smallGameRuleDao")
public class SmallGameRuleDaoImpl implements ISmallGameRuleDao{
	private static final Log log = LogFactory.getLog(SmallGameRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(SmallGameRule SmallGameRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+SmallGameRule);
		}
		try{
			findById(SmallGameRule.getId());
		}catch(NotFoundBeanException e){
            template.save(SmallGameRule);
		}		
	}
	
	@Override
	public void updateBean(SmallGameRule SmallGameRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+SmallGameRule);
		}
		findById(SmallGameRule.getId());
        template.update(SmallGameRule);
	}
	
	@Override
	public void removeBean(SmallGameRule SmallGameRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+SmallGameRule);
		}
		findById(SmallGameRule.getId());
        template.delete(SmallGameRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        SmallGameRule SmallGameRule = findById(id);
        template.delete(SmallGameRule);
	}

	@Override
	public SmallGameRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		SmallGameRule SmallGameRule = (SmallGameRule)template.get(SmallGameRule.class, id);
		if (null==SmallGameRule)
			throw new NotFoundBeanException();
		return SmallGameRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SmallGameRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <SmallGameRule> list = (List<SmallGameRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SmallGameRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <SmallGameRule> list = (List<SmallGameRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}

    @Override
    public List<SmallGameRule> findAll() {
        List<SmallGameRule> list = template.find("from SmallGameRule a ");
        //List<FamilyRule> list1 = template.find("from FamilyRule a ");
        return list;
    }
}