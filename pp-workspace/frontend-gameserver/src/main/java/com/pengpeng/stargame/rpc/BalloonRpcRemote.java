package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class BalloonRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得放气球面板信息
*/
public com.pengpeng.stargame.vo.event.BalloonPanelInfoVO getBalloonInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.BalloonPanelInfoVO  rValue=  proxy.execute("gameevent.balloon.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.BalloonPanelInfoVO.class);
      return rValue;
}
/**
*放气球
*/
public void putBalloon (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
     proxy.execute("gameevent.balloon.put",session, proxy.gson.toJson(req),void.class);
    
}

}