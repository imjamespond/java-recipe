package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class WharfRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*码头启用
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO enable (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.enable",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*码头信息
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO getInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.getInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*码头排行
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO getRank (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.rank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*提交任务
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO submit (com.pengpeng.stargame.rpc.Session mysessions, com.pengpeng.stargame.vo.wharf.WharfReq farmPlayer ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.submit",mysessions, proxy.gson.toJson(farmPlayer),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*货船启航
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO shipSail (com.pengpeng.stargame.rpc.Session weekKey, com.pengpeng.stargame.vo.wharf.WharfReq e ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.ship.sail",weekKey, proxy.gson.toJson(e),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*请求好友帮助
*/
public com.pengpeng.stargame.vo.wharf.WharfInfoVO needHelp (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfInfoVO  rValue=  proxy.execute("wharf.need.help",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfInfoVO.class);
      return rValue;
}
/**
*码头货船到来
*/
public com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO arrive (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO  rValue=  proxy.execute("wharf.ship.arrive",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO.class);
      return rValue;
}
/**
*码头货船到来所需达人币
*/
public com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO arriveGold (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO  rValue=  proxy.execute("wharf.arrive.gold",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO.class);
      return rValue;
}
/**
*全部提交
*/
public com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO submitAll (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.wharf.WharfReq req ) throws GameException{
      com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO  rValue=  proxy.execute("wharf.ship.submit.all",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.wharf.WharfGoldCoinVO.class);
      return rValue;
}

}