package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FashionRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.PlayerFashionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-30
 * Time: 下午12:01
 */
@Component
public class FashionCmd extends AbstractHandler {
   @Autowired
    private FashionRpcRemote fashionRpcRemote;

    @CmdAnnotation(cmd = "fashion.get", req = FashionIdReq.class, name = "获取玩家身上的服装信息",vo=PlayerFashionVO.class)
    public Response  getFashion(Session session, FashionIdReq req) throws GameException {
        return Response.newObject(fashionRpcRemote.getFashion(session,req));
    }

    @CmdAnnotation(cmd = "fashion.change", req = FashionIdReq.class, name = "换装",vo=void.class)
    public void  change(Session session, FashionIdReq req) throws GameException {
        fashionRpcRemote.change(session,req);

    }

    @CmdAnnotation(cmd = "fashion.takeOff", req = FashionIdReq.class, name = "脱掉服装",vo=void.class)
    public void takeOff(Session session,FashionIdReq req) throws GameException{
        fashionRpcRemote.takeOff(session,req);
    }


    /**
     * 卸下 所有 。。
     */
    @CmdAnnotation(cmd = "fashion.takeOffAll", req = FashionIdReq.class, name = "卸下所有服装",vo=void.class)
    public void takeOffAll(Session session,FashionIdReq req) throws GameException{
        fashionRpcRemote.takeOffAll(session,req);
    }

    /**
     * 随机  搭配
     */
    @CmdAnnotation(cmd = "fashion.randomFromPkg", req = FashionIdReq.class, name = "从 背包里面随机搭配 穿戴",vo=void.class)
    public void  randomFromPkg(Session session,FashionIdReq req)   throws GameException{
        fashionRpcRemote.randomFromPkg(session,req);
    }
}
