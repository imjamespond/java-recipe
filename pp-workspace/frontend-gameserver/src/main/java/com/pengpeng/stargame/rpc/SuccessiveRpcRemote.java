package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class SuccessiveRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*领取奖励
*/
public com.pengpeng.stargame.vo.successive.SuccessiveInfoVO getPrize (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.successive.SuccessiveReq req ) throws GameException{
      com.pengpeng.stargame.vo.successive.SuccessiveInfoVO  rValue=  proxy.execute("successive.getPrize",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.successive.SuccessiveInfoVO.class);
      return rValue;
}
/**
*连续登陆信息
*/
public com.pengpeng.stargame.vo.successive.SuccessiveInfoVO SuccessiveInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.successive.SuccessiveReq req ) throws GameException{
      com.pengpeng.stargame.vo.successive.SuccessiveInfoVO  rValue=  proxy.execute("successive.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.successive.SuccessiveInfoVO.class);
      return rValue;
}

}