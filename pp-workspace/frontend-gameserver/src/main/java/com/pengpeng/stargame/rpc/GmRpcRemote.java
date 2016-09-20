package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class GmRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*封号
*/
public void freeze (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.TimeReq req ) throws GameException{
     proxy.execute("gm.p.freeze",session, proxy.gson.toJson(req),void.class);
    
}
/**
*禁言
*/
public void speak (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.role.TimeReq req ) throws GameException{
     proxy.execute("gm.p.speak",session, proxy.gson.toJson(req),void.class);
    
}
/**
*增加游戏币达人币
*/
public void coin (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.gm.CoinReq req ) throws GameException{
     proxy.execute("gm.p.coin",session, proxy.gson.toJson(req),void.class);
    
}
/**
*刷新规则数据
*/
public void ruleRefresh (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.gm.ItemReq req ) throws GameException{
     proxy.execute("gm.rule.refresh",session, proxy.gson.toJson(req),void.class);
    
}
/**
*完成玩家当前的新手任务
*/
public void finishNew (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
     proxy.execute("gm.p.finishNew",session, proxy.gson.toJson(req),void.class);
    
}
/**
*发送消息
*/
public void message (com.pengpeng.stargame.rpc.Session svo, com.pengpeng.stargame.vo.gm.MsgReq sessions ) throws GameException{
     proxy.execute("gm.p.message",svo, proxy.gson.toJson(sessions),void.class);
    
}
/**
*添加道具
*/
public void item (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.gm.ItemReq req ) throws GameException{
     proxy.execute("gm.p.item",session, proxy.gson.toJson(req),void.class);
    
}

}