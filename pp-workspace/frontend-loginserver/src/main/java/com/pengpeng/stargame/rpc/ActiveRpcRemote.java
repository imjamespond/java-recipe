package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class ActiveRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得活跃度信息
*/
public com.pengpeng.stargame.vo.role.ActiveVO getActiveList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.ActiveReq req ) throws GameException{
      com.pengpeng.stargame.vo.role.ActiveVO  rValue=  proxy.execute("active.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.role.ActiveVO.class);
      return rValue;
}
/**
*领取积分
*/
public int reward (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.ActiveReq req ) throws GameException{
      int  rValue=  proxy.execute("active.reward",session, proxy.gson.toJson(req),int.class);
      return rValue;
}

}