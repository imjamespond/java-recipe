package com.pengpeng.admin.stargame.dao;

import com.pengpeng.admin.stargame.model.PlayerActionModel;
import com.tongyi.action.Page;
import com.tongyi.dao.IBaseDao;

import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:08
 */
public interface IPlayerActionDao extends IBaseDao<PlayerActionModel> {
    void findNcount(final String nameQuery, final Map<String, Object> params, final int begin, final int pagesize, Page<PlayerActionModel> page);
}
