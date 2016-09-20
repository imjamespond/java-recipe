package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.rule.PlayerRule;

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

    /**
     * 玩家 添加 达人币
     * @param player
     * @param value
     */
    public void  incGoldCoin(Player player,int value,int type);

    /**
     * 玩家 扣减 达人币
     * @param player
     * @param value
     */
    public void  decGoldCoin(Player player,int value,int type);

    /**
     * 玩家 添加游戏币
     * @param player
     * @param value
     */
    public void  incGameCoin(Player player,int value);

    /**
     * 玩家扣减 游戏币
     * @param player
     * @param value
     */
    public void  decGameCoin(Player player,int value);
}
