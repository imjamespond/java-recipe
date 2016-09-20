package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmActionRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*进入农场时候获取最新的动态数量
*/
public int getNewActionNum (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmActionReq req ) throws GameException{
      int  rValue=  proxy.execute("farm.action.newnum",session, proxy.gson.toJson(req),int.class);
      return rValue;
}
/**
*取得玩家农场动态信息
*/
public com.pengpeng.stargame.vo.farm.FarmActionInfoVO getFarmActionInfoVO (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmActionReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmActionInfoVO  rValue=  proxy.execute("farm.action.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmActionInfoVO.class);
      return rValue;
}
/**
*取得玩家留言信息列表
*/
public com.pengpeng.stargame.vo.farm.FarmMessageInfoVO getMessageList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmActionReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmMessageInfoVO  rValue=  proxy.execute("farm.message.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmMessageInfoVO.class);
      return rValue;
}
/**
*添加留言
*/
public com.pengpeng.stargame.vo.farm.FarmMessageInfoVO addMessage (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmActionReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmMessageInfoVO  rValue=  proxy.execute("farm.message.add",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.FarmMessageInfoVO.class);
      return rValue;
}

}