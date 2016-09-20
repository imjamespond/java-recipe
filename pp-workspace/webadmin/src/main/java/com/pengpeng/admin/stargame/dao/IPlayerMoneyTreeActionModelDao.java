package com.pengpeng.admin.stargame.dao;

import com.pengpeng.admin.stargame.model.PlayerMoneyTreeActionModel;
import com.tongyi.dao.IBaseDao;

import java.util.Map;


/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IPlayerMoneyTreeActionModelDao extends IBaseDao<PlayerMoneyTreeActionModel>{
    int count(final String nameQuery, final Map<String, Object> params);
}