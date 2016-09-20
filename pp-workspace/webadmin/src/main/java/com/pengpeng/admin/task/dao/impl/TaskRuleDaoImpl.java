package com.pengpeng.admin.task.dao.impl;

import com.pengpeng.admin.task.dao.ITaskRuleDao;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "taskRuleDao")
public class TaskRuleDaoImpl implements ITaskRuleDao{
	private static final Log log = LogFactory.getLog(TaskRuleDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(TaskRule taskRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+taskRule);
		}
		try{
			findById(taskRule.getId());
		}catch(NotFoundBeanException e){
            template.save(taskRule);
		}		
	}
	
	@Override
	public void updateBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+taskRule);
		}
		findById(taskRule.getId());
        template.update(taskRule);
	}
	
	@Override
	public void removeBean(TaskRule taskRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+taskRule);
		}
		findById(taskRule.getId());
        template.delete(taskRule);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        TaskRule taskRule = findById(id);
        template.delete(taskRule);
	}

	@Override
	public TaskRule findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		TaskRule taskRule = (TaskRule)template.get(TaskRule.class, id);;
		if (null==taskRule)
			throw new NotFoundBeanException();
		return taskRule;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskRule> findPages(final String nameQuery,Map<String,Object> params,final int begin,final int offset){
		if (log.isDebugEnabled()){
			log.debug("findPages:[begin="+begin+",offset="+offset+",nameQuery="+nameQuery+"]");
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        //List <TaskRule> list = (List<TaskRule>)template.find.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        List<TaskRule> list = (List<TaskRule>)template.execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery(nameQuery).setFirstResult(begin).setMaxResults(offset).list();
            }
        });
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskRule> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <TaskRule> list = (List<TaskRule>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}

    public int count(String nameQuery){
        List<Long> count;
        String hql = "select count(*) "+nameQuery;
        count = template.find(hql);
        return count.get(0).intValue();
    }
}