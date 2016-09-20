package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IFamilyRuleDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
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
@Repository(value = "familyRuleDao")
public class FamilyRuleDaoImpl implements IFamilyRuleDao{
	private static final Log log = LogFactory.getLog(FamilyRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FamilyRule familyRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+familyRule);
		}
		try{
			findById(familyRule.getId());
		}catch(NotFoundBeanException e){
            template.save(familyRule);
		}		
	}
	
	@Override
	public void updateBean(FamilyRule familyRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+familyRule);
		}
		findById(familyRule.getId());
        template.update(familyRule);
	}
	
	@Override
	public void removeBean(FamilyRule familyRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+familyRule);
		}
		findById(familyRule.getId());
        template.delete(familyRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FamilyRule familyRule = findById(id);
        template.delete(familyRule);
	}

	@Override
	public FamilyRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FamilyRule familyRule = (FamilyRule)template.get(FamilyRule.class, id);;
		if (null==familyRule)
			throw new NotFoundBeanException();
		return familyRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FamilyRule> list = (List<FamilyRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FamilyRule> list = (List<FamilyRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}

    @Override
    public List<FamilyRule> findAll() {
        return template.find("from FamilyRule a ");
    }
}