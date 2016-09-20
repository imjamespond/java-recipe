package com.pengpeng.stargame.statistics;

import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;

/**
 * User: mql
 * Date: 14-2-19
 * Time: 下午5:48
 */
public interface IStatistics {
    /**
     * 以家族为单位统计 物品数量
     * @param pid
     * @param itemId
     * @param num
     */
    void addItemNumByFamily(String pid,String itemId,int num);

    /**
     * 记录玩家一次登录
     * @param pid
     */
    void recordLogin(String pid,Player player,OtherPlayer otherPlayer);

    /**
     * 记录玩家注册
     * @param pid
     */
    void recordRegister(String pid,Player player);

    /**
     * 记录一次 加积分
     * @param pid
     * @param player
     * @param type
     * @param num
     */
    void recordAddIntegral(String pid,Player player,int type,int num);

    /**
     * 记录一次 换家族 ，算新家族的注册
     * @param pid
     * @param player
     */
    void recordChangeFamily(String pid,Player player);

    /**
     * 记录一次 消耗 达人币
     * @param pid
     * @param player
     * @param type
     * @param num
     */
    void recordDeductGold(String pid,Player player,int type,int num);

    /**
     * 记录一次 充值
     * @param pid
     * @param player
     * @param num
     */
    void recordRecharge(String pid,Player player,int num);

    /**
     * 记录 完成新手任务
     * @param pid
     * @param player
     * @param taskId
     */
    void recordFinishNewTask(String pid,Player player,String taskId);
}
