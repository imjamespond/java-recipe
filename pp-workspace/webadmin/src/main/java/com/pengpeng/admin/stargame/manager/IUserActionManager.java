package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.UserActionModel;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;

import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:10
 */
public interface IUserActionManager {

    public UserActionModel createBean(UserActionModel userActionModel) throws BeanAreadyException;
    public Page<UserActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize);
}
