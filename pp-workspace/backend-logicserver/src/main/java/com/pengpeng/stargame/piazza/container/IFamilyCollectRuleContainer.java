package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;
import com.pengpeng.stargame.piazza.rule.FamilyCollectRule;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:06
 */
public interface IFamilyCollectRuleContainer  extends IMapContainer<String,FamilyCollectRule> {
    /**
     * GM每分钟调用 刷新 所有家族的收集活动数据
     */
    public void refresh();

    /**
     * 获取需要收集的物品 规则id
     * @param fid
     * @return
     */
    public String getFamilyRuleId(String fid);

    /**
     * 检测
     * @param pid
     * @param familyCollect
     */
    public void chechDonate(String pid,FamilyCollect familyCollect,int num) throws GameException;

    /**
     * 捐献
     * @param pid
     * @param familyCollect
     */
    public void donate(String pid,FamilyCollect familyCollect,int num);

    /**
     * 如果 家族收集完成 那一刻 ，邮件控制
     * @param familyCollect
     * @param fid
     */
    public void mailControl( FamilyCollect familyCollect,String fid,String pid) throws AlertException;

    /**
     * 获取玩家贡献排行
     * @param familyCollect
     * @param page
     * @return
     */
    public MemberColletPageVO getMemberCollectPageVo(FamilyCollect familyCollect,int page);

    /**
     * 获取所有家族信息
     * @return
     */
    public  FamilyCollectRankVO[] getFamilyCollectRankVO();

    /**
     * 玩家登录的时候 检测是否有 要发的 收集积分奖励邮件
     * @param pid
     */
    public void checkRewardMail(String pid) throws AlertException;

    /**
     * 添加奖励
     * @param pid
     * @param ruleId
     * @param num
     */
    public void addReward(String pid,String ruleId,int num);

}
