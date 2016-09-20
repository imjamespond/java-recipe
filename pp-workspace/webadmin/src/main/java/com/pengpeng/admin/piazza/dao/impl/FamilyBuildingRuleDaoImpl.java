package com.pengpeng.admin.piazza.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.piazza.dao.IFamilyBuildingRuleDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
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
@Repository(value = "familyBuildingRuleDao")
public class FamilyBuildingRuleDaoImpl implements IFamilyBuildingRuleDao{
	private static final Log log = LogFactory.getLog(FamilyBuildingRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(FamilyBuildingRule familyBuildingRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+familyBuildingRule);
		}
		try{
			findById(familyBuildingRule.getId());
		}catch(NotFoundBeanException e){
            template.save(familyBuildingRule);
		}		
	}
	
	@Override
	public void updateBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+familyBuildingRule);
		}
		findById(familyBuildingRule.getId());
        template.update(familyBuildingRule);
	}
	
	@Override
	public void removeBean(FamilyBuildingRule familyBuildingRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+familyBuildingRule);
		}
		findById(familyBuildingRule.getId());
        template.delete(familyBuildingRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        FamilyBuildingRule familyBuildingRule = findById(id);
        template.delete(familyBuildingRule);
	}

	@Override
	public FamilyBuildingRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		FamilyBuildingRule familyBuildingRule = (FamilyBuildingRule)template.get(FamilyBuildingRule.class, id);;
		if (null==familyBuildingRule)
			throw new NotFoundBeanException();
		return familyBuildingRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyBuildingRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FamilyBuildingRule> list = (List<FamilyBuildingRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FamilyBuildingRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <FamilyBuildingRule> list = (List<FamilyBuildingRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}