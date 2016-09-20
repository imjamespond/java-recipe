package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmPkgRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*是否可增加物品
*/
public boolean isAddItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      boolean  rValue=  proxy.execute("farmpkg.isAddItem",session, proxy.gson.toJson(req),boolean.class);
      return rValue;
}
/**
*卖出物品
*/
public com.pengpeng.stargame.vo.farm.FarmPkgVO saleItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmPkgVO  rValue=  proxy.execute("farmpkg.remove",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmPkgVO.class);
      return rValue;
}
/**
*取得玩家仓库信息
*/
public com.pengpeng.stargame.vo.farm.FarmPkgVO getItemAll (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmPkgVO  rValue=  proxy.execute("farmpkg.get.all",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmPkgVO.class);
      return rValue;
}
/**
*取得玩家指定的仓库物品集合
*/
public com.pengpeng.stargame.vo.farm.FarmItemVO[] getForType (com.pengpeng.stargame.rpc.Session i$, com.pengpeng.stargame.vo.farm.FarmItemReq session ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmItemVO[]  rValue=  proxy.execute("farmpkg.get.fortype",i$, proxy.gson.toJson(session),com.pengpeng.stargame.vo.farm.FarmItemVO[].class);
      return rValue;
}
/**
*升级仓库
*/
public com.pengpeng.stargame.vo.farm.FarmPkgVO levelUp (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmPkgVO  rValue=  proxy.execute("farmpkg.up.level",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmPkgVO.class);
      return rValue;
}
/**
*取得物品详细信息
*/
public com.pengpeng.stargame.vo.farm.FarmShopItemVO getFarmItemTip (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmShopItemVO  rValue=  proxy.execute("farmpkg.item.tip",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmShopItemVO.class);
      return rValue;
}
/**
*升级仓库下一级物品
*/
public com.pengpeng.stargame.vo.farm.FarmPkgLevelVO[] levelItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmPkgLevelVO[]  rValue=  proxy.execute("farmpkg.level.item",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmPkgLevelVO[].class);
      return rValue;
}
/**
*取得物品详细信息
*/
public com.pengpeng.stargame.vo.BaseShopItemVO getGoodsTip (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
      com.pengpeng.stargame.vo.BaseShopItemVO  rValue=  proxy.execute("goods.item.tip",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.BaseShopItemVO.class);
      return rValue;
}
/**
*取得玩家一个物品
*/
public com.pengpeng.stargame.vo.farm.FarmItemVO getItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmItemVO  rValue=  proxy.execute("farmpkg.get",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmItemVO.class);
      return rValue;
}
/**
*购买物品
*/
public com.pengpeng.stargame.vo.farm.FarmPkgVO addItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmItemReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmPkgVO  rValue=  proxy.execute("farmpkg.buy",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmPkgVO.class);
      return rValue;
}
/**
*取得商店列表
*/
public com.pengpeng.stargame.vo.farm.FarmShopItemVO[] getPackageInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmShopItemVO[]  rValue=  proxy.execute("farm.get.warehouse",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmShopItemVO[].class);
      return rValue;
}

}