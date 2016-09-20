package com.pengpeng.stargame.integral.container.impl;

import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.dao.IPlayerIntegralDao;
import com.pengpeng.stargame.integral.rule.IntegralRule;
import com.pengpeng.stargame.lottery.rule.LotteryRule;
import com.pengpeng.stargame.model.integral.IntegralAction;
import com.pengpeng.stargame.model.integral.PlayerIntegralShow;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.MsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:55
 */
@Component
public class IIntegralRuleContainerImpl extends HashMapContainer<String, IntegralRule> implements IIntegralRuleContainer {
    /**
     * 1、小游戏排行
     * 2、明星收集活动
     * 3、货船
     * 4、路人
     * 5、活跃度
     * 6、神秘宝箱
     * 7、招财树
     * 8、任务奖励
     * 9、五一活动
     * 10、成就奖励
     */
    public static final int INTEGRAL_ACTION_1 = 1;
    public static final int INTEGRAL_ACTION_2 = 2;
    public static final int INTEGRAL_ACTION_3 = 3;
    public static final int INTEGRAL_ACTION_4 = 4;
    public static final int INTEGRAL_ACTION_5 = 5;
    public static final int INTEGRAL_ACTION_6 = 6;
    public static final int INTEGRAL_ACTION_7 = 7;
    public static final int INTEGRAL_ACTION_8 = 8;
    public static final int INTEGRAL_ACTION_9 = 9;
    public static final int INTEGRAL_ACTION_10 = 10;
    @Autowired
    private IPlayerIntegralDao playerIntegralDao;

    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private IStatistics statistics;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;

    @Override
    public void addIntegralAction(String pid, int type, int num) throws Exception {
        /**
         * 游戏内的 动态
         */
        PlayerIntegralShow playerIntegralShow = playerIntegralDao.getPlayerIntegralShow(pid);
        IntegralAction integralAction = new IntegralAction();
        integralAction.setType(type);
        integralAction.setNum(num);
        integralAction.setTime(new Date());
        playerIntegralShow.addAction(integralAction);
        playerIntegralDao.saveBean(playerIntegralShow);
        BroadcastHolder.add(new MsgVO(EventConstant.EVENT_NEN_I, 1));

        Player player = playerDao.getBean(pid);
        /**
         * 成就   累计获得积分
         */
        successRuleContainer.updateSuccessNum(pid,7,num,"");
        /**
         * 任务的
         */
        taskRuleContainer.updateTaskNum(pid, TaskConstants.CONDI_TYPE_20,"",num);
        /**
         * 记录一次获取积分
         */
        statistics.recordAddIntegral(pid,player,type,num);
        /**
         * 网站添加积分
         */
        String des = "《粉丝达人》游戏获取积分:";
        IntegralRule integralRule = getElement(String.valueOf(type));

        if(integralRule!=null){
            if (integralRule.getDes() != null && !"".equals(integralRule.getDes())) {
                des = integralRule.getDes();
            }
        }
        String wzType="STAR_GAME_100";
        if(integralRule!=null){
            wzType=integralRule.getType();
        }
        siteDao.addFansDoyenPoint(player.getUserId(), num,wzType, des+String.valueOf(num));
    }
}
