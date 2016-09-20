package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;

import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:10
 */
public interface IPlayerActionManager {

    public PlayerActionModel createBean(PlayerActionModel pam) throws BeanAreadyException;
    public Page<PlayerActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize);
}
