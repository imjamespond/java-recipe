package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmQinMaRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得亲妈农场信息
*/
public com.pengpeng.stargame.vo.farm.FarmVO getFarmInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmVO  rValue=  proxy.execute("farm.qinma.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmVO.class);
      return rValue;
}
/**
*评价亲妈农场
*/
public void evaluation (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq req ) throws GameException{
     proxy.execute("farm.qinma.evaluation",session, proxy.gson.toJson(req),void.class);
    
}

}