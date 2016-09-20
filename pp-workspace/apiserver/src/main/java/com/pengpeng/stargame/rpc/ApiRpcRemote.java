package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.vo.role.ChargeReq;
import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class ApiRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取玩家信息
*/
public com.pengpeng.stargame.vo.api.PlayerResult getPlayerInfo (Session session, com.pengpeng.stargame.vo.api.ApiReq req ) throws GameException{
      com.pengpeng.stargame.vo.api.PlayerResult  rValue=  proxy.execute("getPlayerInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.api.PlayerResult.class);
      return rValue;
}
/**
*明星粉丝值排行
*/
public com.pengpeng.stargame.vo.api.FamilyRankVO[] familyTop (Session familyRule, com.pengpeng.stargame.vo.piazza.StarGiftReq familyRankVO ) throws GameException{
      com.pengpeng.stargame.vo.api.FamilyRankVO[]  rValue=  proxy.execute("family.fansValue.top",familyRule, proxy.gson.toJson(familyRankVO),com.pengpeng.stargame.vo.api.FamilyRankVO[].class);
      return rValue;
}
/**
*获取房间豪华度排行
*/
public com.pengpeng.stargame.vo.api.RoomRankVO[] roomtop (Session roomRankVO, com.pengpeng.stargame.vo.room.RoomIdReq memberInfo ) throws GameException{
      com.pengpeng.stargame.vo.api.RoomRankVO[]  rValue=  proxy.execute("room.top",roomRankVO, proxy.gson.toJson(memberInfo),com.pengpeng.stargame.vo.api.RoomRankVO[].class);
      return rValue;
}
/**
*获取农场等级排行
*/
public com.pengpeng.stargame.vo.api.FarmRankVO[] farmtop (Session farmRankVO, com.pengpeng.stargame.vo.room.RoomIdReq memberInfo ) throws GameException{
      com.pengpeng.stargame.vo.api.FarmRankVO[]  rValue=  proxy.execute("farm.top",farmRankVO, proxy.gson.toJson(memberInfo),com.pengpeng.stargame.vo.api.FarmRankVO[].class);
      return rValue;
}
/**
*充值,加达人币
*/
public void charge (Session session, com.pengpeng.stargame.vo.role.ChargeReq req ) throws GameException{
     proxy.execute("p.charge",session, proxy.gson.toJson(req),void.class);
    
}
/**
*充值VIP
*/
public void chargeVip (Session session, ChargeReq req ) throws GameException{
     proxy.execute("p.chargeVip",session, proxy.gson.toJson(req),void.class);
    
}

}