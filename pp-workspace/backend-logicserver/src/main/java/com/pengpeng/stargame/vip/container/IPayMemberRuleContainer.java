package com.pengpeng.stargame.vip.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.vip.rule.PayMemberRule;

import java.util.List;

/**
 * User: mql
 * Date: 13-11-19
 * Time: 上午11:57
 */
public interface IPayMemberRuleContainer extends IMapContainer<String,PayMemberRule> {

    /**
     * 添加VIP时间
      * @param pid
     * @param hour
     */
    public  void addVipTime(String pid,int hour);

    public List<PayMemberRule> getListByLevel(int level);

    public PayMemberRule getPayMemberRuleByTypeAndLevel(int type,int level);
    /**
     * 返回玩家是否是 VIP
     * @param pid
     * @return
     */
    public boolean isPayMember(String  pid);

    /**
     * 获取超级粉丝 每日 订单的最大数量
     * @param pid
     * @return
     */
    public int getOrderMaxNum(String pid);

    /**
     * 每日协助好友采摘次数
     * @param pid
     * @return
     */
    public int getHelpMaxNum(String pid);

    /**
     * 千里传音 免费次数
     * @param pid
     * @return
     */
    public int getSonorusMaxNum(String pid);

    /**
     * 每天向好友赠送礼物次数
     * @param pid
     * @return
     */
    public int getSendGiftMaxNum(String pid);

    /**
     *获取家族任务 完美领取 增加的倍数
     * @param pid
     * @return
     */
    public int getFamilyTaskAdd(String pid);

    /**
     *获取家族任务 完美领取 增加的倍数
     * @param pid
     * @return
     */
    public int getFreeZanNum(String pid);
}
