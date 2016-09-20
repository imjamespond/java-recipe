package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.FarmPkgRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.BaseShopItemVO;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.farm.*;
import com.pengpeng.stargame.vo.role.PlayerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 包裹，仓库，物品 - 农场
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-6 下午12:19
 */
@Component
public class FarmPkgCmd extends AbstractHandler {
    @Autowired
    private PlayerRpcRemote playerService;

    @Autowired
    private StatusRemote statusService;

    @Autowired
    private ISessionContainer container;

    @Autowired
    private FarmPkgRpcRemote farmPkgRpcRemote;
    @CmdAnnotation(cmd = "farm.goodsNum", vo =GoodsVO.class, req = FarmIdReq.class, name = "查询物品的数量")
    public Response goodsNum(Session session,FarmIdReq req) throws GameException {
        return Response.newObject(farmPkgRpcRemote.goodsNum(session,req));
    }
    @CmdAnnotation(cmd="farm.get.warehouse",name="取得商店列表",vo=FarmShopItemVO[].class,req=CommonReq.class)
    public Response getShopInfo(Session session,CommonReq req) throws GameException {
        FarmShopItemVO[] vo =farmPkgRpcRemote.getPackageInfo(session, null);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.buy",name="购买物品/种子",vo=FarmPkgVO.class,req=FarmItemReq.class)
    public Response bug(Session session,FarmItemReq req) throws GameException {
        FarmPkgVO vo = farmPkgRpcRemote.addItem(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.remove",name="卖出物品/种子",vo=FarmPkgVO.class,req=FarmItemReq.class)
    public Response sale(Session session,FarmItemReq req) throws GameException {
        FarmPkgVO vo = farmPkgRpcRemote.saleItem(session, req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="farmpkg.removeSome",name="卖出物品/种子 多个",vo=FarmPkgVO.class,req=FarmItemReq.class)
    public Response saleSome(Session session,FarmItemReq req) throws GameException {
        FarmPkgVO vo = farmPkgRpcRemote.saleItemsome(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.get",name="取得玩家一个物品",vo=FarmItemVO.class,req=FarmItemReq.class)
    public Response getItem(Session session,FarmItemReq req) throws GameException {
        FarmItemVO vo = farmPkgRpcRemote.getItem(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.get.all",name="取得玩家仓库信息",vo=FarmPkgVO.class,req=FarmItemReq.class)
    public Response getAll(Session session,FarmItemReq req) throws GameException {
        FarmPkgVO vo = farmPkgRpcRemote.getItemAll(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.get.fortype",name="取得玩家指定的仓库物品集合",vo=FarmItemVO.class,req=FarmItemReq.class)
    public Response getForType(Session session,FarmItemReq req) throws GameException {
        FarmItemVO [] vo = farmPkgRpcRemote.getForType(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.up.level",name="升级仓库",vo=void.class,req=FarmIdReq.class)
    public Response levelFarm(Session session,FarmIdReq req) throws GameException {
        FarmPkgVO vo = farmPkgRpcRemote.levelUp(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd="farmpkg.item.tip",name="取得物品详细信息",vo=FarmShopItemVO.class,req=FarmItemReq.class)
    public Response getFarmItemTip(Session session,FarmItemReq req) throws GameException {
        FarmShopItemVO vo = farmPkgRpcRemote.getFarmItemTip(session,req);
        return Response.newObject(vo);
    }
    @CmdAnnotation(cmd="goods.item.tip",name="取得物品详细信息 包括所有物品",vo=BaseShopItemVO.class,req=IdReq.class)
    public Response getGoodsInfo(Session session,IdReq req) throws GameException {
        BaseShopItemVO vo = farmPkgRpcRemote.getGoodsTip(session,req);
        return Response.newObject(vo);
    }



    @CmdAnnotation(cmd="farmpkg.level.item",name="升级仓库下一级物品",vo=FarmPkgLevelVO[].class,req=FarmItemReq.class)
    public Response levelItem(Session session,FarmItemReq req) throws GameException {
        FarmPkgLevelVO [] vo = farmPkgRpcRemote.levelItem(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "farmpkg.clearup", vo = FarmPkgVO.class, req = FarmItemReq.class, name = "整理背包")
    public Response clearup(Session session, FarmItemReq req) throws GameException {
        FarmPkgVO vo= farmPkgRpcRemote.clearup(session,req);
        return  Response.newObject(vo);
    }

}
