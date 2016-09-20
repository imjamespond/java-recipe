package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.vo.success.SuccessReq;
import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class SuccessRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取成就信息
*/
public com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO getSuccessInfo (com.pengpeng.stargame.rpc.Session session, SuccessReq req ) throws GameException{
      com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO  rValue=  proxy.execute("success.getSuccessInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO.class);
      return rValue;
}
/**
*获取成就奖励信息
*/
public com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO gerReward (com.pengpeng.stargame.rpc.Session session, SuccessReq req ) throws GameException{
      com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO  rValue=  proxy.execute("success.gerReward",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO.class);
      return rValue;
}
/**
*改变称号
*/
public com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO changeTitle (com.pengpeng.stargame.rpc.Session session, SuccessReq req) throws GameException{
      com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO  rValue=  proxy.execute("success.changeTitle",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.success.PlayerSuccessInfoVO.class);
      return rValue;
}

}