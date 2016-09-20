package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.rpc.FarmDecorateRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.farm.FarmItemReq;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午3:03
 */
@Component
public class FarmDecorateCmd extends AbstractHandler{
    @Autowired
    private FarmDecorateRpcRemote farmDecorateRpcRemote;
    @CmdAnnotation(cmd = "farm.decorate.info", req = FarmDecorateReq.class, name = "农场装饰信息",vo =FarmDecorateVO.class)
    public Response getFarmDecorateInfo(Session session,  FarmDecorateReq farmDecorateReq) throws GameException {

        return Response.newObject(farmDecorateRpcRemote.getFarmDecorateInfo(session,farmDecorateReq));
    }
    @CmdAnnotation(cmd = "farm.decorate.add", req = FarmDecorateReq.class, name = "添加装饰信息", vo = FarmDecorateVO.class)
    public Response add(Session session, FarmDecorateReq req) throws GameException {

        return Response.newObject(farmDecorateRpcRemote.add(session,req));
    }
    @CmdAnnotation(cmd = "farm.decorate.pkg", vo = FarmDecoratePkgVO.class, req = FarmDecorateReq.class, name = "取得农场装饰 背包")
    public Response getItemAll(Session session, FarmDecorateReq req) throws GameException {
        return Response.newObject(farmDecorateRpcRemote.getItemAll(session,req));
    }
    @CmdAnnotation(cmd = "farm.decorate.save", req = FarmDecorateReq.class, name = "编辑农场 保存", vo = FarmDecorateVO.class)
    public Response save(Session session, FarmDecorateReq req) throws AlertException, GameException {
        return Response.newObject(farmDecorateRpcRemote.save(session, req));
    }
    @CmdAnnotation(cmd = "farm.decorate.tip", vo = FarmDecorateShopItemVO.class, req = FarmDecorateReq.class, name = "取得物品详细信息 Tip编辑的时候 用")
    public Response getItemTip(Session session, FarmDecorateReq req) throws GameException {
        return Response.newObject(farmDecorateRpcRemote.getItemTip(session, req));
    }

    @CmdAnnotation(cmd = "farm.decorate.list", req = FarmDecorateReq.class, name = "取得农场内的装饰品 列表")
    public Response getShopList(Session session, FarmDecorateReq req) throws GameException {
        return Response.newObject(farmDecorateRpcRemote.getShopList(session,req));
    }

    @CmdAnnotation(cmd = "farm.animal.operation", vo = FarmDecorateVO.class, req = FarmDecorateReq.class, name = "操作  鼹鼠（地鼠）")
    public Response animalOperation(Session session, FarmDecorateReq req) throws GameException {
        return Response.newObject(farmDecorateRpcRemote.animalOperation(session,req));
    }
    @CmdAnnotation(cmd = "farm.decorate.clear", req = FarmDecorateReq.class, name = "清除农场东西", vo = FarmDecorateVO.class)
    public Response clear(Session session, FarmDecorateReq req) throws AlertException, GameException {
        farmDecorateRpcRemote.clear(session,req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd = "farm.decorate.itemNum", req = FarmDecorateReq.class, name = "取得农场内的装饰品装饰品的数量",vo= GoodsVO.class)
    public Response getDecorateNum(Session session, FarmDecorateReq req) throws GameException {
        return Response.newObject(farmDecorateRpcRemote.getDecorateNum(session,req));
    }

}
