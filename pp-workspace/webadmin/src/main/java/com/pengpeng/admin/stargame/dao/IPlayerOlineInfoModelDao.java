package com.pengpeng.admin.stargame.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import com.tongyi.dao.IBaseDao;
import java.util.Map;


/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IPlayerOlineInfoModelDao extends IBaseDao<PlayerOlineInfoModel>{
    int count(final String nameQuery, final Map<String, Object> params);
}