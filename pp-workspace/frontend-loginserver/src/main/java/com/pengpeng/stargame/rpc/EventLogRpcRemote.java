package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class EventLogRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得玩家事件日志
*/
public com.pengpeng.stargame.vo.EventLogVO[] getEventLogAll (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.EventLogReq req ) throws GameException{
      com.pengpeng.stargame.vo.EventLogVO[]  rValue=  proxy.execute("eventLog.get.all",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.EventLogVO[].class);
      return rValue;
}

}