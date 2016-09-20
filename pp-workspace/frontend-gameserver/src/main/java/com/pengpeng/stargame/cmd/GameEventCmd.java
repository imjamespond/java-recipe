package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.EventRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.VideoEventRpcRemote;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.event.EventDropInfoVO;
import com.pengpeng.stargame.vo.event.EventReq;
import com.pengpeng.stargame.vo.event.VideoPanelVO;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 下午2:07
 */
@Component
public class GameEventCmd extends AbstractHandler {
    @Autowired
    private EventRpcRemote eventRpcRemote;
    @Autowired
    private VideoEventRpcRemote videoEventRpcRemote;

    @CmdAnnotation(cmd = "gameevent.dropgift.list", req = EventReq.class, vo = EventDropInfoVO.class, name = "取得场景内掉落")
    public Response getDropGift(Session session, EventReq req) throws GameException {
        return Response.newObject(eventRpcRemote.getDropGift(session, req));
    }

    @CmdAnnotation(cmd = "gameevent.pick.gift", req = EventReq.class, vo = RewardVO.class, name = "捡取礼物")
    public Response pickGift(Session session, EventReq req) throws GameException {
        return Response.newObject(eventRpcRemote.pickGift(session, req));
    }

    @CmdAnnotation(cmd = "gameevent.familyBank.info", req = EventReq.class, vo = FamilyBankVO.class, name = "家族银行活动信息")
    public Response familyBankInfo(Session session, EventReq req) throws GameException {
        return Response.newObject(eventRpcRemote.getFamilyBankEventInfo(session, req));
    }

    @CmdAnnotation(cmd = "gameevent.familyBank.get", req = EventReq.class, vo = FamilyBankVO.class, name = "领取家族银行活动奖励")
    public Response familyBankGet(Session session, EventReq req) throws GameException {
        return Response.newObject(eventRpcRemote.getFamilyBankEventReward(session, req));
    }


    @CmdAnnotation(cmd = "gameevent.springevent.info", req = EventReq.class, name = "获取春节活动信息")
    public Response springeventInfo(Session session, EventReq req) throws GameException {
      return Response.newObject(eventRpcRemote.springeventInfo(session,req));
    }

    @CmdAnnotation(cmd = "gameevent.springevent.start", req = EventReq.class, name = "放鞭炮")
    public Response springeventStart(Session session, EventReq req) throws GameException {
        return Response.newObject(eventRpcRemote.springeventStart(session,req));

    }

    @CmdAnnotation(cmd = "gameevent.video.list", req = EventReq.class, name = "取得视频列表 正在直播的 和过往的",vo = VideoPanelVO.class)
    public Response getVideoList(Session session, EventReq req) throws GameException {
        return Response.newObject(videoEventRpcRemote.getVideoList(session,req));
    }

    @CmdAnnotation(cmd = "gameevent.video.exchange", req = EventReq.class, name = "兑换",vo=VideoPanelVO.class)
    public Response videoExchange(Session session, EventReq req) throws GameException {
        return Response.newObject(videoEventRpcRemote.exchange(session,req));
    }



}
