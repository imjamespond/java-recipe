package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class QinmaSceneRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*进入亲妈农场
*/
public com.pengpeng.stargame.vo.map.ScenceVO enterFarm (com.pengpeng.stargame.rpc.Session xx, com.pengpeng.stargame.vo.map.MapReq yy ) throws GameException{
      com.pengpeng.stargame.vo.map.ScenceVO  rValue=  proxy.execute("qinma.scene.enter",xx, proxy.gson.toJson(yy),com.pengpeng.stargame.vo.map.ScenceVO.class);
      return rValue;
}

}