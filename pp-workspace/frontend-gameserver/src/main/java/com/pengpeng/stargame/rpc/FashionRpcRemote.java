package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FashionRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*换装
*/
public void change (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
     proxy.execute("fashion.change",session, proxy.gson.toJson(req),void.class);
    
}
/**
*脱掉服装
*/
public void takeOff (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
     proxy.execute("fashion.takeOff",session, proxy.gson.toJson(req),void.class);
    
}
/**
*卸下所有服装
*/
public void takeOffAll (com.pengpeng.stargame.rpc.Session fashionPkg, com.pengpeng.stargame.vo.fashion.FashionIdReq vo ) throws GameException{
     proxy.execute("fashion.takeOffAll",fashionPkg, proxy.gson.toJson(vo),void.class);
    
}
/**
*从 背包里面随机搭配 穿戴
*/
public void randomFromPkg (com.pengpeng.stargame.rpc.Session fashionPkg, com.pengpeng.stargame.vo.fashion.FashionIdReq vo ) throws GameException{
     proxy.execute("fashion.randomFromPkg",fashionPkg, proxy.gson.toJson(vo),void.class);
    
}
/**
*获取身上穿的服装
*/
public com.pengpeng.stargame.vo.fashion.PlayerFashionVO getFashion (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.fashion.FashionIdReq req ) throws GameException{
      com.pengpeng.stargame.vo.fashion.PlayerFashionVO  rValue=  proxy.execute("fashion.get",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.fashion.PlayerFashionVO.class);
      return rValue;
}

}