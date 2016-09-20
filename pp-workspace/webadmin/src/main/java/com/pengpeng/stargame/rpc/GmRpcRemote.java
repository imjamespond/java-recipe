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
*发送消息
*/
public void message (Session svo, com.pengpeng.stargame.vo.gm.MsgReq sessions ) throws GameException{
     proxy.execute("gm.p.message",svo, proxy.gson.toJson(sessions),void.class);
    
}
/**
*添加道具
*/
public void item (Session session, com.pengpeng.stargame.vo.gm.ItemReq req ) throws GameException{
     proxy.execute("gm.p.item",session, proxy.gson.toJson(req),void.class);
    
}
/**
*封号
*/
public void freeze (Session session, com.pengpeng.stargame.vo.role.TimeReq req ) throws GameException{
     proxy.execute("gm.p.freeze",session, proxy.gson.toJson(req),void.class);
    
}
/**
*禁言
*/
public void speak (Session session, com.pengpeng.stargame.vo.role.TimeReq req ) throws GameException{
     proxy.execute("gm.p.speak",session, proxy.gson.toJson(req),void.class);
    
}
/**
*增加游戏币达人币
*/
public void coin (Session session, com.pengpeng.stargame.vo.gm.CoinReq req ) throws GameException{
     proxy.execute("gm.p.coin",session, proxy.gson.toJson(req),void.class);
    
}
/**
*刷新规则数据
*/
public void ruleRefresh (Session session, com.pengpeng.stargame.vo.gm.ItemReq req ) throws GameException{
     proxy.execute("gm.rule.refresh",session, proxy.gson.toJson(req),void.class);
    
}
/**
*完成玩家当前的新手任务
*/
public void finishNew (Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
     proxy.execute("gm.p.finishNew",session, proxy.gson.toJson(req),void.class);
    
}
/**
*增加游戏币达人币
*/
public void farmExpAdd (Session session, com.pengpeng.stargame.vo.gm.CoinReq req ) throws GameException{
     proxy.execute("gm.p.farmExpAdd",session, proxy.gson.toJson(req),void.class);
    
}
/**
*跳过所有新手任务
*/
public void finishAllNew (Session session, com.pengpeng.stargame.vo.IdReq req ) throws GameException{
     proxy.execute("gm.p.finishAllNew",session, proxy.gson.toJson(req),void.class);
    
}
/**
*获取每天添加达人币的玩家Id
*/
public com.pengpeng.stargame.vo.gm.AddGoldVO getAddGoldInfo (Session session, com.pengpeng.stargame.vo.gm.AddGoldReq req ) throws GameException{
      com.pengpeng.stargame.vo.gm.AddGoldVO  rValue=  proxy.execute("gm.p.addGoldInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.gm.AddGoldVO.class);
      return rValue;
}
/**
*保存每天添加达人币的玩家Id
*/
public com.pengpeng.stargame.vo.gm.AddGoldVO saveAddGold (Session arr$, com.pengpeng.stargame.vo.gm.AddGoldReq len$ ) throws GameException{
      com.pengpeng.stargame.vo.gm.AddGoldVO  rValue=  proxy.execute("gm.p.saveAddGold",arr$, proxy.gson.toJson(len$),com.pengpeng.stargame.vo.gm.AddGoldVO.class);
      return rValue;
}
/**
*充值查询
*/
public com.pengpeng.stargame.model.player.RechargeLog[] chargeInfo (Session dateKey, com.pengpeng.stargame.vo.role.ChargeReq i ) throws GameException{
      com.pengpeng.stargame.model.player.RechargeLog[]  rValue=  proxy.execute("gm.p.chargeInfo",dateKey, proxy.gson.toJson(i),com.pengpeng.stargame.model.player.RechargeLog[].class);
      return rValue;
}
/**
*Gm添加VIP
*/
public void addVip (Session session, com.pengpeng.stargame.vo.role.ChargeReq req ) throws GameException{
     proxy.execute("gm.addVip",session, proxy.gson.toJson(req),void.class);
    
}
/**
*Gm调用
*/
public void executeGame (Session session, com.pengpeng.stargame.req.BaseReq req ) throws GameException{
     proxy.execute("gm.executeGame",session, proxy.gson.toJson(req),void.class);
    
}
/**
*查询以家族为单位的 道具信息
*/
public com.pengpeng.stargame.vo.gm.VideoFamilyInfoVO videoFamilyNum (Session player, com.pengpeng.stargame.vo.gm.VideoReq one ) throws GameException{
      com.pengpeng.stargame.vo.gm.VideoFamilyInfoVO  rValue=  proxy.execute("gm.videoFamilyInfo",player, proxy.gson.toJson(one),com.pengpeng.stargame.vo.gm.VideoFamilyInfoVO.class);
      return rValue;
}

}