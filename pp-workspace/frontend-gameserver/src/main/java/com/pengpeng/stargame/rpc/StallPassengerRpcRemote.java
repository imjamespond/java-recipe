package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class StallPassengerRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*路人信息
*/
public com.pengpeng.stargame.vo.stall.StallPassengerInfoVO getInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallPassengerReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallPassengerInfoVO  rValue=  proxy.execute("stall.passenger.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallPassengerInfoVO.class);
      return rValue;
}
/**
*拒绝出售
*/
public com.pengpeng.stargame.vo.stall.StallPassengerInfoVO reject (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.stall.StallPassengerReq req ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallPassengerInfoVO  rValue=  proxy.execute("stall.passenger.reject",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.stall.StallPassengerInfoVO.class);
      return rValue;
}
/**
*向路人出售
*/
public com.pengpeng.stargame.vo.stall.StallPassengerInfoVO sell (com.pengpeng.stargame.rpc.Session ca, com.pengpeng.stargame.vo.stall.StallPassengerReq session ) throws GameException{
      com.pengpeng.stargame.vo.stall.StallPassengerInfoVO  rValue=  proxy.execute("stall.passenger.sell",ca, proxy.gson.toJson(session),com.pengpeng.stargame.vo.stall.StallPassengerInfoVO.class);
      return rValue;
}

}