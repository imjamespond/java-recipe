package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class GiftRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*各个模块下未收取礼物数
*/
public com.pengpeng.stargame.vo.gift.GiftTipVO[] untreated (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.gift.GiftTipVO[]  rValue=  proxy.execute("gift.shop.untreated",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.gift.GiftTipVO[].class);
      return rValue;
}
/**
*取得指定类型下的礼物列表
*/
public com.pengpeng.stargame.vo.gift.ShopGiftVO[] listGiftShop (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.gift.ShopGiftReq req ) throws GameException{
      com.pengpeng.stargame.vo.gift.ShopGiftVO[]  rValue=  proxy.execute("gift.shop.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.gift.ShopGiftVO[].class);
      return rValue;
}
/**
*音乐榜期间,领取礼包
*/
public com.pengpeng.stargame.vo.RewardVO onlineGive (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("gift.online.give",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}

}