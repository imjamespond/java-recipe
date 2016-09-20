package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FarmActionRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.farm.FarmActionInfoVO;
import com.pengpeng.stargame.vo.farm.FarmActionReq;
import com.pengpeng.stargame.vo.farm.FarmMessageInfoVO;
import com.pengpeng.stargame.vo.farm.FarmMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 下午12:08
 */
@Component
public class FarmActionCmd extends AbstractHandler {
    @Autowired
    private FarmActionRpcRemote farmActionRpcRemote;

    @CmdAnnotation(cmd = "farm.action.newnum", req = FarmActionReq.class, name = "进入农场时候获取最新的动态数量",vo=Integer.class)
    public Response getNewActionNum(Session session,FarmActionReq req) throws GameException {
        int a= farmActionRpcRemote.getNewActionNum(session,req);
        return Response.newObject(a);
    }
    @CmdAnnotation(cmd = "farm.action.info", req = FarmActionReq.class, name = "取得玩家农场动态信息",vo=FarmActionInfoVO.class)
    public Response getFarmActionInfoVO(Session session,FarmActionReq req) throws GameException {
        FarmActionInfoVO farmActionInfoVO=farmActionRpcRemote.getFarmActionInfoVO(session,req);
        return Response.newObject(farmActionInfoVO);
    }
    @CmdAnnotation(cmd = "farm.message.list",req = FarmActionReq.class, name = "取得玩家留言信息列表",vo=FarmMessageInfoVO.class)
    public Response getMessageList(Session session,FarmActionReq req) throws GameException {
        FarmMessageInfoVO farmMessageVOs=farmActionRpcRemote.getMessageList(session,req);
        return Response.newObject(farmMessageVOs);
    }
    @CmdAnnotation(cmd = "farm.message.add",  req = FarmActionReq.class, name = "添加留言",vo=FarmMessageInfoVO.class)
    public Response addMessage(Session session,FarmActionReq req) throws GameException {
        FarmMessageInfoVO farmMessageVOs= farmActionRpcRemote.addMessage(session,req);
        return Response.newObject(farmMessageVOs);
    }
}
