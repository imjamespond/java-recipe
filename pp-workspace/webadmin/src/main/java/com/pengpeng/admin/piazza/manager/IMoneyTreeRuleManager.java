package com.pengpeng.admin.piazza.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IMoneyTreeRuleManager{
	
	public MoneyTreeRule createBean(MoneyTreeRule moneyTreeRule) throws BeanAreadyException;
	
	public void updateBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(MoneyTreeRule moneyTreeRule) throws NotFoundBeanException;
	
	public MoneyTreeRule findById(Serializable id) throws NotFoundBeanException;
	
	public Page<MoneyTreeRule> findPages(Map<String,Object> params,int pageNo,int pageSize);

}