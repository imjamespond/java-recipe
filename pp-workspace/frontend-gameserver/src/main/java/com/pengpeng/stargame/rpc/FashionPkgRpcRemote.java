package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FashionPkgRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*卖出物品
*/
public void saleItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
     proxy.execute("fashionpkg.remove",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得物品详细信息
*/
public com.pengpeng.stargame.vo.fashion.FashionShopItemVO getItemTip (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.fashion.FashionShopItemVO  rValue=  proxy.execute("fashionpkg.item.tip",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.fashion.FashionShopItemVO.class);
      return rValue;
}
/**
*取得商店列表
*/
public com.pengpeng.stargame.vo.fashion.FashionShopItemVO[] getFashionShopList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.CommonReq req ) throws GameException{
      com.pengpeng.stargame.vo.fashion.FashionShopItemVO[]  rValue=  proxy.execute("fashion.get.warehouse",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.fashion.FashionShopItemVO[].class);
      return rValue;
}
/**
*取得玩家指定类型的仓库
*/
public com.pengpeng.stargame.vo.fashion.FashionPkgVO getFashionPkg (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.fashion.FashionPkgVO  rValue=  proxy.execute("fashionpkg.get.fortype",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.fashion.FashionPkgVO.class);
      return rValue;
}
/**
*从背包中随机一件物品
*/
public com.pengpeng.stargame.vo.fashion.FashionItemVO randomItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.fashion.FashionItemVO  rValue=  proxy.execute("fashionpkg.item.random",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.fashion.FashionItemVO.class);
      return rValue;
}
/**
*购买物品
*/
public void addItem (com.pengpeng.stargame.rpc.Session itemId, com.pengpeng.stargame.vo.fashion.FashionIdReq arr$ ) throws GameException{
     proxy.execute("fashionpkg.buy",itemId, proxy.gson.toJson(arr$),void.class);
    
}

}