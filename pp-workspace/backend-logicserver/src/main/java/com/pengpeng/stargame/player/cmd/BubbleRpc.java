package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.cmd.FarmRpc;
import com.pengpeng.stargame.model.player.Bubble;
import com.pengpeng.stargame.model.player.BubbleConstant;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IBubbleRuleContainer;
import com.pengpeng.stargame.player.dao.IBubbleDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspMailFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.map.BubbleReq;
import com.pengpeng.stargame.vo.map.BubbleVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther james
 * @since: 13-5-28下午4:53
 */
@Component()
public class BubbleRpc extends RpcHandler {

    @Autowired
    private RspMailFactory rspFactory;
    @Autowired
    private IBubbleRuleContainer bubbleRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IBubbleDao bubbleDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private FarmRpc farmRpc;

    private static final Logger logger = Logger.getLogger(BubbleRpc.class);

    @RpcAnnotation(cmd = "get.info.bubble", lock = false, req = BubbleReq.class, name = "取得自己信息", vo = BubbleVO.class)
    public BubbleVO getInfo(Session session, BubbleReq req) {
        String pid = session.getPid();
        Bubble bubble = bubbleDao.getBubble(pid);

        Date now = new Date();
        if(DateUtil.getDayOfYear(bubble.getAccDate())!=DateUtil.getDayOfYear(now) ){
            bubble.setTimes(0);
        }
        //登陆清除泡泡
        bubble.setStatus(0);
        bubbleDao.saveBean(bubble);

        BubbleVO vo = new BubbleVO(pid);
        vo.setRestTime(BubbleConstant.ACCEPT_NUM - bubble.getTimes());
        return vo;
    }

    @RpcAnnotation(cmd = "get.bubble", lock = false, req = BubbleReq.class, name = "取得泡泡", vo = BubbleVO.class)
    public BubbleVO get(Session session, BubbleReq req) {
        String pid = req.getPid();
        Bubble bubble = bubbleDao.getBubble(pid);
        if(bubble.getStatus()>0){
            return bubbleRuleContainer.getBubble(pid,bubble.getType());
        }
        return new BubbleVO(pid);
    }

    @RpcAnnotation(cmd = "activate.bubble", lock = false, req = BubbleReq.class, name = "激活泡泡", vo = BubbleVO.class)
    public BubbleVO activate(Session session, BubbleReq req) {
        String pid = session.getPid();
        Bubble bubble = bubbleDao.getBubble(pid);
        BubbleVO vo = bubbleRuleContainer.getRandomAttach(bubble);
        bubble.setStatus(1);
        bubbleDao.saveBean(bubble);

        String channelId = SessionUtil.getChannelScene(session);
        //取得这个频道的所有用户
        Session[] sessions = statusRemote.getMember(session, channelId);
        //广播给所有人
        frontendService.broadcast(sessions, vo);
        return null;
    }

    @RpcAnnotation(cmd = "deactivate.bubble", lock = false, req = BubbleReq.class, name = "不激活泡泡", vo = BubbleVO.class)
    public BubbleVO deactivate(Session session, BubbleReq req) {
        String pid = session.getPid();
        Bubble bubble = bubbleDao.getBubble(pid);
        bubble.setStatus(0);
        bubbleDao.saveBean(bubble);
        return null;
    }

    @RpcAnnotation(cmd = "accept.bubble", lock = false, req = BubbleReq.class, name = "领取泡泡", vo = BubbleVO.class)
    public BubbleVO accept(Session session, BubbleReq req) throws AlertException{
        String pid = req.getPid();
        String mid = session.getPid();

        Player player = playerDao.getBean(mid);
        Bubble bubble = bubbleDao.getBubble(pid);
        Bubble myBubble = bubbleDao.getBubble(mid);

        //次数判断
        if(myBubble.getTimes()> BubbleConstant.ACCEPT_NUM){
            exceptionFactory.throwAlertException("bubble.accept.limit");
        }
        myBubble.setTimes(myBubble.getTimes()+1);

        BubbleVO vo = new BubbleVO(pid);
        if(bubble.getStatus()>0){
            //get
            int notify = bubbleRuleContainer.accept( player , bubble);
            bubble.setStatus(0);
            myBubble.setAccDate(new Date());
            bubbleDao.saveBean(bubble);
            bubbleDao.saveBean(myBubble);

            //推送
            Session[] mysessions = {session};
            if ((notify & BaseRewardConstant.NOTIFY_GAMECOIN) > 0) {
                //财富通知
                frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
            }
            if ((notify & BaseRewardConstant.NOTIFY_FARMEXP) > 0) {
                //经验通知
                frontendService.broadcast(mysessions, farmRpc.getFarmVoByPlayerId(mid));
            }

            //广播
            String channelId = SessionUtil.getChannelScene(session);
            //取得这个频道的所有用户
            Session[] sessions = statusRemote.getMember(session, channelId);
            //广播给所有人
            frontendService.broadcast(sessions, vo);
            return vo;
        }


        vo.setRestTime(-1);//exceptionFactory.throwAlertException("bubble.accepted");
        return vo;
    }

    }
