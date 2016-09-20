package com.pengpeng.admin.stargame.dao;

import com.pengpeng.admin.stargame.model.PlayerItemModel;
import com.pengpeng.admin.stargame.model.PlayerSaleActionModel;
import com.tongyi.dao.IBaseDao;

import java.util.Map;


/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IPlayerItemModelDao extends IBaseDao<PlayerItemModel>{
    int count(final String nameQuery, final Map<String, Object> params);
}