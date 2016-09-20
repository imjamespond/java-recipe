package com.pengpeng.stargame.success.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.success.OneSuccess;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspSuccessFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO;
import com.pengpeng.stargame.vo.success.SuccessReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午3:06
 */
@Component
public class SuccessRpc extends RpcHandler {
    @Autowired
    private IPlayerSuccessDao playerSuccessDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspSuccessFactory rspSuccessFactory;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @RpcAnnotation(cmd="success.getSuccessInfo",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="获取成就信息")
    public PlayerSuccessInfoVO getSuccessInfo(Session session,SuccessReq req) throws GameException {
        PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(session.getPid());
       return rspSuccessFactory.getSVO(playerSuccessInfo);
    }

    @RpcAnnotation(cmd="success.gerReward",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="获取成就奖励信息")
    public PlayerSuccessInfoVO gerReward(Session session,SuccessReq req) throws GameException {
        PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(session.getPid());
        OneSuccess oneSeccess=playerSuccessInfo.getOneSuccess(req.getsId()) ;
        int star=oneSeccess.getStarNum();
        if(star==0){
            exceptionFactory.throwAlertException("成就未到达！");
        }
        if(oneSeccess.getRewardStar().contains(star)){
            exceptionFactory.throwAlertException("奖励已经领取过了！");
        }
        oneSeccess.addReward(oneSeccess.getStarNum());
        playerSuccessDao.saveBean(playerSuccessInfo);
        /**
         * 发放奖励
         */
        successRuleContainer.addReward(session.getPid(),oneSeccess);


        PlayerSuccessInfo playerSuccessInfo1=playerSuccessDao.getPlayerSuccessInfo(session.getPid());
        return rspSuccessFactory.getSVO(playerSuccessInfo1);
    }
    @RpcAnnotation(cmd="success.changeTitle",vo=PlayerSuccessInfoVO.class,req=SuccessReq.class,name="改变称号")
    public PlayerSuccessInfoVO changeTitle(Session session,SuccessReq req) throws GameException {
        PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(session.getPid());
        OneSuccess oneSeccess=playerSuccessInfo.getOneSuccess(req.getsId()) ;
        if(oneSeccess==null){
            playerSuccessInfo.setUseId("");
            playerSuccessDao.saveBean(playerSuccessInfo);
        }
        if(oneSeccess!=null&&oneSeccess.getStarNum()==3){
            playerSuccessInfo.setUseId(oneSeccess.getId());
            playerSuccessDao.saveBean(playerSuccessInfo);
        }
        return rspSuccessFactory.getSVO(playerSuccessInfo);
    }

}
