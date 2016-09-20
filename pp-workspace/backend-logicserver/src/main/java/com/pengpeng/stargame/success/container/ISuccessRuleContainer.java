package com.pengpeng.stargame.success.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.success.OneSuccess;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.success.rule.SuccessRule;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:37
 */
public interface ISuccessRuleContainer extends IMapContainer<String,SuccessRule> {
    /**
     * 初始化玩家成就信息
     * @param playerSuccessInfo
     * @return
     */
    public boolean init(PlayerSuccessInfo playerSuccessInfo);

    /**
     * 修改玩家成就信息
     * @param pid
     * @param type
     * @param num
     */
    public void updateSuccessNum(String pid,int type,int num,String id);

    /**
     * 检测
     * @param playerSuccessInfo
     */
    public boolean checkSuccess(PlayerSuccessInfo playerSuccessInfo);

    /**
     * 发放奖励
     * @param pid
     * @param oneSeccess
     */
    public void addReward(String pid,OneSuccess oneSeccess) throws RuleException;
}
