package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyActivityRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*家族活动列表
*/
public com.pengpeng.stargame.vo.piazza.ActivityVO[] getEvents (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.ActivityVO[]  rValue=  proxy.execute("familyinfo.getEvents",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.ActivityVO[].class);
      return rValue;
}

}