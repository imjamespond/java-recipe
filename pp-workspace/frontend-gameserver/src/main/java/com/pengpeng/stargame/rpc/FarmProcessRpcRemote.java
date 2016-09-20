package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmProcessRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*开通格子
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO open (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.open",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*完成正在进行的队列
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO finish (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.finish",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*进入农场场景的时候 获取可以领取的 物品
*/
public com.pengpeng.stargame.vo.farm.GoodsVO[] getFinished (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.GoodsVO[]  rValue=  proxy.execute("farm.process.getFinished",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.GoodsVO[].class);
      return rValue;
}
/**
*获取加工物品列表
*/
public com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO[] getprocesslist (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO[]  rValue=  proxy.execute("farm.process.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO[].class);
      return rValue;
}
/**
*开始生产
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO processstart (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.start",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*取消队列中的一个队列
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO processcancel (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.cancel",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*完成一个正在进行的队列
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO finishOne (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.finishone",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*返回 直接完成 需要的达人币数量
*/
public int needgold (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      int  rValue=  proxy.execute("farm.process.needgold",session, proxy.gson.toJson(req),int.class);
      return rValue;
}
/**
*领取生成物品
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO getProcessItem (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.getProcessItem",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*获取玩家队列信息
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO getProcessInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.getProcessInfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*添加加速
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO speedAll (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.speedAll",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}
/**
*加速倒计时完成
*/
public com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO speedAllEnd (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.process.FarmProcessReq req ) throws GameException{
      com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO  rValue=  proxy.execute("farm.process.speedAllEnd",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO.class);
      return rValue;
}

}