package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class SceneRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*进入指定场景
*/
public com.pengpeng.stargame.vo.map.ScenceVO enterScene (com.pengpeng.stargame.rpc.Session xx, com.pengpeng.stargame.vo.map.MapReq yy ) throws GameException{
      com.pengpeng.stargame.vo.map.ScenceVO  rValue=  proxy.execute("scene.enter",xx, proxy.gson.toJson(yy),com.pengpeng.stargame.vo.map.ScenceVO.class);
      return rValue;
}
/**
*退出地图
*/
public void outerScene (com.pengpeng.stargame.rpc.Session session, String channelId ) throws GameException{
     proxy.execute("scene.outer",session, proxy.gson.toJson(channelId),void.class);
    
}
/**
*进入指定场景和指定坐标
*/
public void enterTo (com.pengpeng.stargame.rpc.Session map, com.pengpeng.stargame.vo.map.MapReq sessions ) throws GameException{
     proxy.execute("scene.to",map, proxy.gson.toJson(sessions),void.class);
    
}
/**
*取得地图信息
*/
public com.pengpeng.stargame.vo.map.ScenceVO getScene (com.pengpeng.stargame.rpc.Session session, String sceneId ) throws GameException{
      com.pengpeng.stargame.vo.map.ScenceVO  rValue=  proxy.execute("scene.get.info",session, proxy.gson.toJson(sceneId),com.pengpeng.stargame.vo.map.ScenceVO.class);
      return rValue;
}
/**
*移动
*/
public com.pengpeng.stargame.vo.map.MoveVO move (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.map.MoveReq req ) throws GameException{
      com.pengpeng.stargame.vo.map.MoveVO  rValue=  proxy.execute("scene.move",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.map.MoveVO.class);
      return rValue;
}

}