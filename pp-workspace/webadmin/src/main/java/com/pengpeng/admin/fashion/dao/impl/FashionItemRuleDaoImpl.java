package com.pengpeng.admin.fashion.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.fashion.dao.IFashionItemRuleDao;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
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
@Repository(value = "fashionItemRuleDao")
public class FashionItemRuleDaoImpl implements IFashionItemRuleDao{
	private static final Log log = LogFactory.getLog(FashionItemRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FashionItemRule fashionItemRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+fashionItemRule);
		}
		try{
			findById(fashionItemRule.getId());
		}catch(NotFoundBeanException e){
            template.save(fashionItemRule);
		}		
	}
	
	@Override
	public void updateBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+fashionItemRule);
		}
		findById(fashionItemRule.getId());
        template.update(fashionItemRule);
	}
	
	@Override
	public void removeBean(FashionItemRule fashionItemRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+fashionItemRule);
		}
		findById(fashionItemRule.getId());
        template.delete(fashionItemRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FashionItemRule fashionItemRule = findById(id);
        template.delete(fashionItemRule);
	}

	@Override
	public FashionItemRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FashionItemRule fashionItemRule = (FashionItemRule)template.get(FashionItemRule.class, id);;
		if (null==fashionItemRule)
			throw new NotFoundBeanException();
		return fashionItemRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FashionItemRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FashionItemRule> list = (List<FashionItemRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FashionItemRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FashionItemRule> list = (List<FashionItemRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}