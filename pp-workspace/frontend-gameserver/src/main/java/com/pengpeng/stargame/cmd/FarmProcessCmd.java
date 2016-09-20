package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FarmProcessRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.farm.FarmShopItemVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO;
import com.pengpeng.stargame.vo.farm.process.FarmProcessReq;
import com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午3:36
 */
@Component
public class FarmProcessCmd extends AbstractHandler {
    @Autowired
    private FarmProcessRpcRemote farmProcessRpcRemote;
    @CmdAnnotation(cmd="farm.process.list",name="获取加工物品列表",vo=FarmProcessItemVO[].class,req=FarmProcessReq.class)
    public Response getprocesslist(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.getprocesslist(session,req));
    }

    @CmdAnnotation(cmd="farm.process.start",name="开始生产",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response processstart(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.processstart(session,req));
    }
    @CmdAnnotation(cmd="farm.process.cancel",name="取消队列中的一个队列",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response processcancel(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.processcancel(session,req));
    }
    @CmdAnnotation(cmd="farm.process.finishone",name="完成队列中的一个队列",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response finishOne(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.finishOne(session,req));
    }
    @CmdAnnotation(cmd="farm.process.needgold",name="返回 直接完成 需要的达人币数量",vo=Integer.class,req=FarmProcessReq.class)
    public Response needgold(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.needgold(session,req));
    }
    @CmdAnnotation(cmd="farm.process.finish",name="完成正在进行的队列",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response finish(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.finish(session,req));
    }

    @CmdAnnotation(cmd="farm.process.open",name="开通格子",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
      public Response open(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.open(session,req));
    }

    @CmdAnnotation(cmd="farm.process.getProcessItem",name="领取生成物品",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response getProcessItem(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.getProcessItem(session,req));
    }
    @CmdAnnotation(cmd="farm.process.getProcessInfo",name="获取玩家队列信息",vo=FarmQueueInfoVO.class,req=FarmProcessReq.class)
    public Response getProcessInfo(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.getProcessInfo(session,req));
    }
    @CmdAnnotation(cmd="farm.process.getFinished",name="进入农场场景的时候 获取可以领取的 物品",vo=GoodsVO[].class,req=FarmProcessReq.class)
    public Response getFinished(Session session,FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.getFinished(session,req));
    }

    @CmdAnnotation(cmd = "farm.process.speedAll", name = "添加加速", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public Response speedAll(Session session, FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.speedAll(session,req));
    }
    @CmdAnnotation(cmd = "farm.process.speedAllEnd", name = "加速倒计时完成", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public Response speedAllEnd(Session session, FarmProcessReq req) throws GameException {
        return Response.newObject(farmProcessRpcRemote.speedAllEnd(session,req));
    }
}
