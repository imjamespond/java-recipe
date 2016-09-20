package com.pengpeng.admin.task.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface ITaskRuleManager{
	
	public TaskRule createBean(TaskRule taskRule) throws BeanAreadyException;
	
	public void updateBean(TaskRule taskRule) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(TaskRule taskRule) throws NotFoundBeanException;
	
	public TaskRule findById(Serializable id) throws NotFoundBeanException;
	
	public Page<TaskRule> findPages(Map<String,Object> params,int pageNo,int pageSize);

}