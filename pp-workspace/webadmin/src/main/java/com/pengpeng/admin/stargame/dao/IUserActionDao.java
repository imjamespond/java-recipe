package com.pengpeng.admin.stargame.dao;

import com.pengpeng.admin.stargame.model.UserActionModel;
import com.tongyi.dao.IBaseDao;

import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:08
 */
public interface IUserActionDao extends IBaseDao<UserActionModel> {
    int count(final String nameQuery, final Map<String , Object> params);
}
