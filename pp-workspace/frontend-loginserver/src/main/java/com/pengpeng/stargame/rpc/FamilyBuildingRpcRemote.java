package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyBuildingRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取建筑列表
*/
public com.pengpeng.stargame.vo.piazza.BuildVO[] getBuilds (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.BuildVO[]  rValue=  proxy.execute("family.list.build",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.BuildVO[].class);
      return rValue;
}
/**
*升级建筑
*/
public com.pengpeng.stargame.vo.piazza.BuildVO[] upgradeBuild (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.FamilyReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.BuildVO[]  rValue=  proxy.execute("family.up.level",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.BuildVO[].class);
      return rValue;
}

}