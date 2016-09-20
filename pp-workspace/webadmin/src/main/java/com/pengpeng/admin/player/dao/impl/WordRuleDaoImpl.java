package com.pengpeng.admin.player.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.player.dao.IWordRuleDao;
import com.pengpeng.stargame.player.rule.WordRule;
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
@Repository(value = "wordRuleDao")
public class WordRuleDaoImpl implements IWordRuleDao{
	private static final Log log = LogFactory.getLog(WordRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(WordRule wordRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+wordRule);
		}
		try{
			findById(wordRule.getId());
		}catch(NotFoundBeanException e){
            template.save(wordRule);
		}		
	}
	
	@Override
	public void updateBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+wordRule);
		}
		findById(wordRule.getId());
        template.update(wordRule);
	}
	
	@Override
	public void removeBean(WordRule wordRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+wordRule);
		}
		findById(wordRule.getId());
        template.delete(wordRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        WordRule wordRule = findById(id);
        template.delete(wordRule);
	}

	@Override
	public WordRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		WordRule wordRule = (WordRule)template.get(WordRule.class, id);;
		if (null==wordRule)
			throw new NotFoundBeanException();
		return wordRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WordRule> findPages(String nameQuery,Map<String,Object> params,int begin,int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <WordRule> list = (List<WordRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WordRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <WordRule> list = (List<WordRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}
	
}