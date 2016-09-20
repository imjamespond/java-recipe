package com.pengpeng.stargame.container.role;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rule.role.PlayerRule;

/**
 * 存放用户规则的容器
 * @author Administrator
 *
 */
public interface IPlayerRuleContainer extends IMapContainer<String, PlayerRule> {

    /**
     * 取得对应的等级规则
     * @param level 玩家等级
     * @param type 角色类型
     * @return
     */
    public PlayerRule getRule(int level, int type);
}
