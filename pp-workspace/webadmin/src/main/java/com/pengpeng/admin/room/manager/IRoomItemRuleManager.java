package com.pengpeng.admin.room.manager;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import com.tongyi.action.Page;
/**
 * 生成模版 manager.vm
 * @author fangyaoxia
 */

public interface IRoomItemRuleManager{
	
	public RoomItemRule createBean(RoomItemRule roomItemRule) throws BeanAreadyException;
	
	public void updateBean(RoomItemRule roomItemRule) throws NotFoundBeanException;
	
	public void removeBean(Serializable id) throws NotFoundBeanException;
	public void removeBean(RoomItemRule roomItemRule) throws NotFoundBeanException;
	
	public RoomItemRule findById(Serializable id) throws NotFoundBeanException;
	
	public Page<RoomItemRule> findPages(Map<String,Object> params,int pageNo,int pageSize);

}