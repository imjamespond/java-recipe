package com.pengpeng.admin.stargame.dao;

import com.pengpeng.admin.stargame.model.PlayerGivingActionModel;
import com.tongyi.dao.IBaseDao;

import java.util.Map;


/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IPlayerGivingActionModelDao extends IBaseDao<PlayerGivingActionModel>{
    int count(final String nameQuery, final Map<String, Object> params);
}