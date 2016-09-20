package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmDecorateRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*添加装饰信息
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO add (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO  rValue=  proxy.execute("farm.decorate.add",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO.class);
      return rValue;
}
/**
*清除农场物件
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO clear (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO  rValue=  proxy.execute("farm.decorate.clear",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO.class);
      return rValue;
}
/**
*编辑农场 保存
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO save (com.pengpeng.stargame.rpc.Session decorateVO, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq arr$ ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO  rValue=  proxy.execute("farm.decorate.save",decorateVO, proxy.gson.toJson(arr$),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO.class);
      return rValue;
}
/**
*取得农场装饰 背包
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO getItemAll (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO  rValue=  proxy.execute("farm.decorate.pkg",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO.class);
      return rValue;
}
/**
*取得物品详细信息 Tip编辑的时候 用
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO getItemTip (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO  rValue=  proxy.execute("farm.decorate.tip",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO.class);
      return rValue;
}
/**
*取得农场内的装饰品 列表
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO[] getShopList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO[]  rValue=  proxy.execute("farm.decorate.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO[].class);
      return rValue;
}
/**
*农场装饰信息
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO getFarmDecorateInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO  rValue=  proxy.execute("farm.decorate.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO.class);
      return rValue;
}
/**
*操作 打 鼹鼠（地鼠）
*/
public com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO animalOperation (com.pengpeng.stargame.rpc.Session sessionF, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq session ) throws GameException{
      com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO  rValue=  proxy.execute("farm.animal.operation",sessionF, proxy.gson.toJson(session),com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO.class);
      return rValue;
}
/**
*取得农场内的装饰品装饰品的数量
*/
public com.pengpeng.stargame.vo.farm.GoodsVO getDecorateNum (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.decorate.FarmDecorateReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.GoodsVO  rValue=  proxy.execute("farm.decorate.itemNum",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.GoodsVO.class);
      return rValue;
}

}