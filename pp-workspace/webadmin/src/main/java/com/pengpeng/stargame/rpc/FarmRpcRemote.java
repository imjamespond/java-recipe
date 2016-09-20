package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*升级农场（农场等级）
*/
public void levelUp (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.up.level",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得农场信息
*/
public com.pengpeng.stargame.vo.farm.FarmVO getFarmInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmVO  rValue=  proxy.execute("farm.get.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmVO.class);
      return rValue;
}
/**
*评价好友农场（每日一评）
*/
public void evaluation (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.evaluation",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得好友农场信息
*/
public com.pengpeng.stargame.vo.farm.FarmVO getFrindInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmVO  rValue=  proxy.execute("farm.get.friend",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmVO.class);
      return rValue;
}
/**
*种植作物
*/
public void plant (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.plant",session, proxy.gson.toJson(req),void.class);
    
}
/**
*收获作物
*/
public void harvest (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.harvest",session, proxy.gson.toJson(req),void.class);
    
}
/**
*帮助好友收获
*/
public void harvestFriend (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.harvest.friend",session, proxy.gson.toJson(req),void.class);
    
}
/**
*进入好友农场
*/
public void enterFriendFarm (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.enter.friend",session, proxy.gson.toJson(req),void.class);
    
}
/**
*取得农场状态
*/
public com.pengpeng.stargame.vo.farm.FarmStateVO[] friendState (com.pengpeng.stargame.rpc.Session fp, com.pengpeng.stargame.vo.IdReq canHelp ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmStateVO[]  rValue=  proxy.execute("farm.state.friends",fp, proxy.gson.toJson(canHelp),com.pengpeng.stargame.vo.farm.FarmStateVO[].class);
      return rValue;
}

}