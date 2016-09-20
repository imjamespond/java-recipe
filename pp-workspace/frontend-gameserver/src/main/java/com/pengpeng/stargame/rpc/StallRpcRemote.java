package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class StallRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*小摊启用
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO enable (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.enable",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*小摊信息
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO getInfo (com.pengpeng.stargame.rpc.Session myStall, com.pengpeng.stargame.vo.stall.StallReq playerStall ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.info",myStall, proxy.gson.toJson(playerStall),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*购买货品
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO buy (com.pengpeng.stargame.rpc.Session farmPackage, com.pengpeng.stargame.vo.stall.StallReq mysessions ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.buy",farmPackage, proxy.gson.toJson(mysessions),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*获取收入
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO getMoney (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.getMoney",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*达人币货架开启
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO goldShelf (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.gold.shelf",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*好友货架开启
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO friendShelf (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.friend.shelf",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*获取助手资讯
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO getAssistant (com.pengpeng.stargame.rpc.Session entry, com.pengpeng.stargame.vo.stall.StallAssistantReq vo ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.assistant.info",entry, proxy.gson.toJson(vo),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*货品上架
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO hitShelf (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.hit.shelf",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*货品下架
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO offShelf (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.off.shelf",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*登广告
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO advertise (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.advertise",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*获取广告
*/
public com.pengpeng.stargame.vo.stall.StallAdvertisementVO getAdvertisement (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallAdvReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallAdvertisementVO  rValue=  proxy.execute("stall.get.advertisement",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallAdvertisementVO.class);
      return rValue;
}
/**
*开启助手
*/
public void enableAssistant (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallAssistantReq req ) throws GameException{
     proxy.execute("stall.assistant.enable",session, proxy.gson.toJson(req),void.class);
    
}
/**
*助手购买
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO buyAssistant (com.pengpeng.stargame.rpc.Session farmPackage, com.pengpeng.stargame.vo.stall.StallReq mysessions ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.assistant.buy",farmPackage, proxy.gson.toJson(mysessions),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}
/**
*助手物品列表
*/
public com.pengpeng.stargame.vo.farm.FarmItemVO[] browseAssistant (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallAssistantReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmItemVO[]  rValue=  proxy.execute("stall.assistant.items",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmItemVO[].class);
      return rValue;
}
/**
*助手是否已开启
*/
public java.lang.Boolean checkAssistant (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallAssistantReq req ) throws GameException{
      java.lang.Boolean  rValue=  proxy.execute("stall.assistant.check",session, proxy.gson.toJson(req),java.lang.Boolean.class);
      return rValue;
}
/**
*助手冷却时间
*/
public com.pengpeng.stargame.vo.stall.StallInfoVO cdAssistant (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallAssistantReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallInfoVO  rValue=  proxy.execute("stall.assistant.cd",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallInfoVO.class);
      return rValue;
}

}