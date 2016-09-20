package com.pengpeng.admin.qinma.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.qinma.dao.IQinMaRoomRuleDao;
import com.pengpeng.admin.qinma.manager.IQinMaRoomRuleManager;
import com.pengpeng.stargame.qinma.rule.QinMaRoomRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "qinMaRoomRuleManager")
public class QinMaRoomRuleManagerImpl implements IQinMaRoomRuleManager{
	private static final Log log = LogFactory.getLog(QinMaRoomRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="qinMaRoomRuleDao")
	private IQinMaRoomRuleDao qinMaRoomRuleDao;

	@Override	
	public QinMaRoomRule findById(Serializable id) throws NotFoundBeanException{
		return qinMaRoomRuleDao.findById(id);
	}

	public QinMaRoomRule createBean(QinMaRoomRule qinMaRoomRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+qinMaRoomRule);
		}		
		qinMaRoomRuleDao.createBean(qinMaRoomRule);
		return qinMaRoomRule;
	}

	@Override	
	public void updateBean(QinMaRoomRule qinMaRoomRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+qinMaRoomRule);
		}
		qinMaRoomRuleDao.updateBean(qinMaRoomRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		qinMaRoomRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(QinMaRoomRule qinMaRoomRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+qinMaRoomRule);
		}
		qinMaRoomRuleDao.removeBean(qinMaRoomRule);
	}
	
	@Override	
	public Page<QinMaRoomRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<QinMaRoomRule> list = qinMaRoomRuleDao.findPages("from QinMaRoomRule a",map,start,start+pageSize);

		Page<QinMaRoomRule> page = new Page<QinMaRoomRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}