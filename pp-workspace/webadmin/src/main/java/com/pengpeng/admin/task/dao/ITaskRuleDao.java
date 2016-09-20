package com.pengpeng.admin.task.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.task.rule.TaskRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import com.tongyi.dao.IBaseDao;

/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface ITaskRuleDao extends IBaseDao<TaskRule>{
    public int count(String nameQuery);
}