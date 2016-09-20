package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class AntiAddictionRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*实名认证
*/
public com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO certificate (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.antiaddiction.AntiAddictionReq req ) throws GameException{
      com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO  rValue=  proxy.execute("antiaddiction.certificate",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO.class);
      return rValue;
}
/**
*是否已实名认证
*/
public com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO check (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.antiaddiction.AntiAddictionReq req ) throws GameException{
      com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO  rValue=  proxy.execute("antiaddiction.check",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO.class);
      return rValue;
}

}