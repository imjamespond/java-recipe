package com.pengpeng.admin.player.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.player.dao.ITransferRuleDao;
import com.pengpeng.admin.player.manager.ITransferRuleManager;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "transferRuleManager")
public class TransferRuleManagerImpl implements ITransferRuleManager{
	private static final Log log = LogFactory.getLog(TransferRuleManagerImpl.class);
	
	@Autowired
	@Qualifier(value="transferRuleDao")
	private ITransferRuleDao transferRuleDao;

	@Override	
	public TransferRule findById(Serializable id) throws NotFoundBeanException{
		return transferRuleDao.findById(id);
	}

	public TransferRule createBean(TransferRule transferRule) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+transferRule);
		}		
		transferRuleDao.createBean(transferRule);
		return transferRule;
	}

	@Override	
	public void updateBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+transferRule);
		}
		transferRuleDao.updateBean(transferRule);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		transferRuleDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(TransferRule transferRule) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+transferRule);
		}
		transferRuleDao.removeBean(transferRule);
	}
	
	@Override	
	public Page<TransferRule> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        List<TransferRule> list = transferRuleDao.findPages("from TransferRule a",map,start,start+pageSize);

		Page<TransferRule> page = new Page<TransferRule>();
		page.setPage(pageNo);
		page.setRows(list);
		return page;
	}
	
}