package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class LuckyTreeRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*增加达人币次数
*/
public com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO add (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO  rValue=  proxy.execute("lucky.tree.add",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO.class);
      return rValue;
}
/**
*招财树信息
*/
public com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO getInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO  rValue=  proxy.execute("lucky.tree.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO.class);
      return rValue;
}
/**
*招财树招财
*/
public com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO call (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO  rValue=  proxy.execute("lucky.tree.call",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO.class);
      return rValue;
}
/**
*招财树浇水
*/
public com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO water (com.pengpeng.stargame.rpc.Session playerLuckyTree, com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq session ) throws GameException{
      com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO  rValue=  proxy.execute("lucky.tree.water",playerLuckyTree, proxy.gson.toJson(session),com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO.class);
      return rValue;
}

}