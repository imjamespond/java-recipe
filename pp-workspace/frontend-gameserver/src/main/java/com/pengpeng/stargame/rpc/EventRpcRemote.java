package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class EventRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得场景内掉落
*/
public com.pengpeng.stargame.vo.event.EventDropInfoVO getDropGift (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.EventDropInfoVO  rValue=  proxy.execute("gameevent.dropgift.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.EventDropInfoVO.class);
      return rValue;
}
/**
*捡取礼物
*/
public com.pengpeng.stargame.vo.RewardVO pickGift (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("gameevent.pick.gift",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}
/**
*获取家族银行活动信息
*/
public com.pengpeng.stargame.vo.event.FamilyBankEventVO getFamilyBankEventInfo (com.pengpeng.stargame.rpc.Session reward, com.pengpeng.stargame.vo.event.EventReq arr$ ) throws GameException{
      com.pengpeng.stargame.vo.event.FamilyBankEventVO  rValue=  proxy.execute("gameevent.familyBank.info",reward, proxy.gson.toJson(arr$),com.pengpeng.stargame.vo.event.FamilyBankEventVO.class);
      return rValue;
}
/**
*领取存款奖励
*/
public com.pengpeng.stargame.vo.event.FamilyBankEventVO getFamilyBankEventReward (com.pengpeng.stargame.rpc.Session reward, com.pengpeng.stargame.vo.event.EventReq arr$ ) throws GameException{
      com.pengpeng.stargame.vo.event.FamilyBankEventVO  rValue=  proxy.execute("gameevent.familyBank.get",reward, proxy.gson.toJson(arr$),com.pengpeng.stargame.vo.event.FamilyBankEventVO.class);
      return rValue;
}
/**
*获取春节活动信息
*/
public com.pengpeng.stargame.vo.event.SpringEventVO springeventInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.SpringEventVO  rValue=  proxy.execute("gameevent.springevent.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.SpringEventVO.class);
      return rValue;
}
/**
*放鞭炮
*/
public com.pengpeng.stargame.vo.event.SpringEventVO springeventStart (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.SpringEventVO  rValue=  proxy.execute("gameevent.springevent.start",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.SpringEventVO.class);
      return rValue;
}

}