package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmBoxRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*刷新宝箱，进入农场调用
*/
public com.pengpeng.stargame.vo.farm.box.FarmBoxVO refresh (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.box.FarmBoxReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.box.FarmBoxVO  rValue=  proxy.execute("farm.box.refresh",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.box.FarmBoxVO.class);
      return rValue;
}
/**
*打开宝箱
*/
public com.pengpeng.stargame.vo.RewardVO openbox (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.box.FarmBoxReq req ) throws GameException{
      com.pengpeng.stargame.vo.RewardVO  rValue=  proxy.execute("farm.box.open",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.RewardVO.class);
      return rValue;
}
/**
*取消打开宝箱
*/
public com.pengpeng.stargame.vo.farm.box.FarmBoxVO cancel (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.box.FarmBoxReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.box.FarmBoxVO  rValue=  proxy.execute("farm.box.cancel",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.box.FarmBoxVO.class);
      return rValue;
}

}