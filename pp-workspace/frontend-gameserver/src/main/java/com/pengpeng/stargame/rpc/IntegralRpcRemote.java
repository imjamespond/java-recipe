package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class IntegralRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*积分获取动态信息
*/
public com.pengpeng.stargame.vo.integral.IntegralActionVO[] integralActions (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.integral.IntegralReq req ) throws GameException{
      com.pengpeng.stargame.vo.integral.IntegralActionVO[]  rValue=  proxy.execute("player.integralActions",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.integral.IntegralActionVO[].class);
      return rValue;
}

}