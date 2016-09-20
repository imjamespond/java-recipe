package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class PayMemberRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*刷新VIP
*/
public com.pengpeng.stargame.vo.vip.VipInfoVO refresh (Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
      com.pengpeng.stargame.vo.vip.VipInfoVO  rValue=  proxy.execute("vip.refresh",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.vip.VipInfoVO.class);
      return rValue;
}
/**
*获取VIp信息
*/
public com.pengpeng.stargame.vo.vip.VipInfoVO getinfo (Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
      com.pengpeng.stargame.vo.vip.VipInfoVO  rValue=  proxy.execute("vip.getinfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.vip.VipInfoVO.class);
      return rValue;
}

}