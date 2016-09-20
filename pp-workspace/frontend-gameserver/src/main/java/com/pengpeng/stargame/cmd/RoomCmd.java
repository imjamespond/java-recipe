package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.RoomRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.room.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-23
 * Time: 上午10:25
 */
@Component
public class RoomCmd extends AbstractHandler{

    @Autowired
    private RoomRpcRemote roomRpcRemote;

    @CmdAnnotation(cmd = "room.enterFriendRoom", req = RoomIdReq.class, name = "进入好友房间消息",vo= RoomVO.class)
    public Response enterFriendRoom(Session session,RoomIdReq req) throws GameException {
        RoomVO roomVO=roomRpcRemote.enterFriendRoom(session,req);
        return  Response.newObject(roomVO);
    }

    @CmdAnnotation(cmd = "room.get.info", req = RoomIdReq.class, name = "取得房间信息",vo= RoomVO.class)
    public Response getRoomInfo(Session session,RoomIdReq req) throws GameException {
        RoomVO roomVO=roomRpcRemote.getRoomInfo(session, req);
        return  Response.newObject(roomVO);
    }

    @CmdAnnotation(cmd = "room.shopList", req = RoomIdReq.class, name = "取得房间内商品 列表",vo= RoomShopItemVO[].class)
    public Response getRoomshop(Session session,RoomIdReq req) throws GameException {
        RoomShopItemVO [] roomShopItemVOs=roomRpcRemote.getRoomshop(session,req);
        return  Response.newObject(roomShopItemVOs);
    }

    @CmdAnnotation(cmd = "room.myList", req = RoomIdReq.class, name = "取得房间内闲置的物品列表",vo= RoomPkgVO.class)
    public Response getRoomItemList(Session session,RoomIdReq req) throws GameException {
       RoomPkgVO roomPkgVO=roomRpcRemote.getRoomItemList(session,req) ;
        return  Response.newObject(roomPkgVO);
    }

    @CmdAnnotation(cmd = "room.item.tip", req = RoomIdReq.class, name = "取得物品信息",vo= RoomShopItemVO.class)
    public Response getItemTip(Session session,RoomIdReq req) throws GameException {
         return  Response.newObject(roomRpcRemote.getItemTip(session,req));
    }

    @CmdAnnotation(cmd = "room.save", req = RoomIdReq.class, name = "编辑房间 保存",vo=RoomVO.class)
    public Response save(Session session,RoomIdReq req) throws GameException {
        RoomVO roomVO=roomRpcRemote.save(session,req);
        return  Response.newObject(roomVO);
    }

    @CmdAnnotation(cmd = "room.buy", req = RoomIdReq.class, name = "购买单个装饰品",vo=void.class)
    public Response buy(Session session,RoomIdReq req) throws GameException{
        roomRpcRemote.buy(session,req);
        return  Response.newOK();
    }
    @CmdAnnotation(cmd = "room.sale", req = RoomIdReq.class, name = "出售装饰品",vo=void.class)
    public Response sale(Session session,RoomIdReq req) throws GameException{
        roomRpcRemote.sale(session,req);
        return  Response.newOK();
    }

    @CmdAnnotation(cmd="room.comment",name="评价好友房间",vo=RoomVO.class,req=RoomIdReq.class)
    public Response commentFarm(Session session,RoomIdReq req) throws GameException {
       RoomVO roomVO=roomRpcRemote.evaluation(session,req);
        return Response.newObject(roomVO);
    }




    @CmdAnnotation(cmd = "room.extension.before", req = RoomIdReq.class, name = "扩建之前请求", vo = ExtensionVO.class)
    public Response extensionBefore(Session session, RoomIdReq req) throws GameException {
        ExtensionVO extensionVO=roomRpcRemote.extensionBefore(session,req);
        return Response.newObject(extensionVO);
    }
    @CmdAnnotation(cmd = "room.extension.start", req = RoomIdReq.class, name = "扩建", vo = RoomVO.class)
    public Response extension(Session session, RoomIdReq req) throws GameException {
        RoomVO roomVO=roomRpcRemote.extension(session,req);
        return Response.newObject(roomVO);
    }
    @CmdAnnotation(cmd = "room.extension.finish", req = RoomIdReq.class, name = "直接完成扩建", vo = RoomVO.class)
    public Response extensionFinish(Session session, RoomIdReq req) throws GameException {
        RoomVO roomVO=roomRpcRemote.extensionFinish(session,req);
        return Response.newObject(roomVO);
    }
    @CmdAnnotation(cmd = "room.extension.finish.needGold", req = RoomIdReq.class, name = "直接完成扩建需要的达人币", vo = Integer.class)
    public Response extensionFinishNeedGold(Session session, RoomIdReq req) throws GameException {
        return Response.newObject(roomRpcRemote.extensionFinishNeedGold(session,req));
    }


}
