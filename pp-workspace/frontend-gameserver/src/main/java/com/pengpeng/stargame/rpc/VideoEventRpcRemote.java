package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class VideoEventRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得视频列表
*/
public com.pengpeng.stargame.vo.event.VideoPanelVO getVideoList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.VideoPanelVO  rValue=  proxy.execute("gameevent.video.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.VideoPanelVO.class);
      return rValue;
}
/**
*兑换
*/
public com.pengpeng.stargame.vo.event.VideoPanelVO exchange (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.event.EventReq req ) throws GameException{
      com.pengpeng.stargame.vo.event.VideoPanelVO  rValue=  proxy.execute("gameevent.video.exchange",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.event.VideoPanelVO.class);
      return rValue;
}

}