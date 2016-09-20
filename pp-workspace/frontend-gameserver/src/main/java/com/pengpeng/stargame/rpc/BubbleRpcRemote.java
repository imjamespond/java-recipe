package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class BubbleRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得泡泡
*/
public com.pengpeng.stargame.vo.map.BubbleVO get (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.map.BubbleReq req ) throws GameException{
      com.pengpeng.stargame.vo.map.BubbleVO  rValue=  proxy.execute("get.bubble",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.map.BubbleVO.class);
      return rValue;
}
/**
*领取泡泡
*/
public com.pengpeng.stargame.vo.map.BubbleVO accept (com.pengpeng.stargame.rpc.Session mysessions, com.pengpeng.stargame.vo.map.BubbleReq session ) throws GameException{
      com.pengpeng.stargame.vo.map.BubbleVO  rValue=  proxy.execute("accept.bubble",mysessions, proxy.gson.toJson(session),com.pengpeng.stargame.vo.map.BubbleVO.class);
      return rValue;
}
/**
*激活泡泡
*/
public com.pengpeng.stargame.vo.map.BubbleVO activate (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.map.BubbleReq req ) throws GameException{
      com.pengpeng.stargame.vo.map.BubbleVO  rValue=  proxy.execute("activate.bubble",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.map.BubbleVO.class);
      return rValue;
}
/**
*取得自己信息
*/
public com.pengpeng.stargame.vo.map.BubbleVO getInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.map.BubbleReq req ) throws GameException{
      com.pengpeng.stargame.vo.map.BubbleVO  rValue=  proxy.execute("get.info.bubble",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.map.BubbleVO.class);
      return rValue;
}
/**
*不激活泡泡
*/
public com.pengpeng.stargame.vo.map.BubbleVO deactivate (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.map.BubbleReq req ) throws GameException{
      com.pengpeng.stargame.vo.map.BubbleVO  rValue=  proxy.execute("deactivate.bubble",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.map.BubbleVO.class);
      return rValue;
}

}