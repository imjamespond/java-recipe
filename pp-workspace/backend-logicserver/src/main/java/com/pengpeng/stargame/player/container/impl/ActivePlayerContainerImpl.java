package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.container.IActiveRewardRuleContainer;
import com.pengpeng.stargame.player.container.IActiveRuleContainer;
import com.pengpeng.stargame.player.dao.IActivePlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.player.rule.ActiveRewardRule;
import com.pengpeng.stargame.player.rule.ActiveRule;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.MsgVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-13下午3:07
 */
@Component
public class ActivePlayerContainerImpl implements IActivePlayerContainer {
    private static final Logger logger = Logger.getLogger(ActivePlayerContainerImpl.class);
    @Autowired
    private IActiveRuleContainer activeRuleContainer;

    @Autowired
    private IActiveRewardRuleContainer activeRewardRuleContainer;

    @Autowired
    private IActivePlayerDao activePlayerDao;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;

    @Autowired
    private FrontendServiceProxy frontendServiceProxy;
//    @Override
//    public void finished(String pid,int type){
//        ActivePlayer ap = activePlayerDao.getBean(pid);
//        if (null==ap){
//            return ;
//        }
//        ActiveRule rule = activeRuleContainer.getElement(type);
//        if (rule==null){
//
//        }
//        int max = rule.getFinishMax();
//        if (!ap.isMax(type,max)){
//            return ;
//        }
//        ap.finished(type);
//    }
    @Override
    public void finish(Session session,String pid,int type, int num) {
        ActivePlayer ap = activePlayerDao.getBean(pid);
        if (null==ap){
            return ;
        }
        ActiveRule rule = activeRuleContainer.getElement(type);
        if (null==rule){
            return ;
        }
        frontendServiceProxy.broadcast(session, new MsgVO(EventConstant.EVENT_DAY_ACTIVE,0));
        boolean isMax = ap.isMax(type,rule.getFinishMax());
        if (isMax){
            return ;
        }
        ap.finish(type, num);
        activePlayerDao.saveBean(ap);
    }

    @Override
    public int reward(ActivePlayer ap, int active) throws RuleException {
        //检查是否已领取
        //调用网站接口
        //设置已领取
        if (ap.isReward(active)){
            exceptionFactory.throwRuleException("active.isreward");
        }
        ActiveRewardRule rule = activeRewardRuleContainer.getElement(active);
        if(null==rule){
            exceptionFactory.throwRuleException("active.notfound");
        }
        /**
         * 先做保存
         */
        ap.reward(active);
        activePlayerDao.saveBean(ap);
        int score = rule.getScore();
        Player player = playerDao.getBean(ap.getId());
        if (player==null){
            exceptionFactory.throwRuleException("p.notfound");
        }
        try {
            /**
             * 记录积分获取动作
             */
            iIntegralRuleContainer.addIntegralAction(ap.getPid(), IIntegralRuleContainerImpl.INTEGRAL_ACTION_5,score);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            exceptionFactory.throwRuleException("active.gamecoin");
        }
        return score;
    }

    @Override
    public void finish(Session session, String pid, String taskId) {
        ActiveRule activeRule=activeRuleContainer.getByTaskId(taskId);
        if(activeRule!=null){
            finish(session,pid,activeRule.getType(),1);
        }
    }


}
