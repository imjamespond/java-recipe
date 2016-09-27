package com.pengpeng.admin.qinma.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.qinma.dao.IQinMaFarmRuleDao;
import com.pengpeng.admin.qinma.manager.IQinMaFarmRuleManager;
import com.pengpeng.stargame.qinma.rule.QinMaFarmRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "qinMaFarmRuleManager")
public class QinMaFarmRuleManagerImpl implements IQinMaFarmRuleManager{
	private static final Log log = LogFactory.getLog(QinMaFarmRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="qinMaFarmRuleDao")
	private IQinMaFarmRuleDao qinMaFarmRuleDao;

	@Override	
	public QinMaFarmRule findById(Serializable id) throws NotFoundBeanException{
		return qinMaFarmRuleDao.findById(id);
	}

	public QinMaFarmRule createBean(QinMaFarmRule qinMaFarmRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+qinMaFarmRule);
		}		
		qinMaFarmRuleDao.createBean(qinMaFarmRule);
		return qinMaFarmRule;
	}

	@Override	
	public void updateBean(QinMaFarmRule qinMaFarmRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+qinMaFarmRule);
		}
		qinMaFarmRuleDao.updateBean(qinMaFarmRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		qinMaFarmRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(QinMaFarmRule qinMaFarmRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+qinMaFarmRule);
		}
		qinMaFarmRuleDao.removeBean(qinMaFarmRule);
	}
	
	@Override	
	public Page<QinMaFarmRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<QinMaFarmRule> list = qinMaFarmRuleDao.findPages("from QinMaFarmRule a",map,start,start+pageSize);

		Page<QinMaFarmRule> page = new Page<QinMaFarmRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}