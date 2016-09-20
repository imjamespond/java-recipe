package com.pengpeng.admin.room.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import com.tongyi.dao.IBaseDao;

/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IRoomItemRuleDao extends IBaseDao<RoomItemRule>{

}