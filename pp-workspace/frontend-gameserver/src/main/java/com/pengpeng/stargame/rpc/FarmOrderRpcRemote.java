package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FarmOrderRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取当前订单列表
*/
public com.pengpeng.stargame.vo.farm.FarmOrderInfoVO getOrderList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq farmIdReq ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmOrderInfoVO  rValue=  proxy.execute("farm.get.orderlist",session, proxy.gson.toJson(farmIdReq),com.pengpeng.stargame.vo.farm.FarmOrderInfoVO.class);
      return rValue;
}
/**
*刷新订单,取得订单信息
*/
public com.pengpeng.stargame.vo.farm.FarmOrderInfoVO refreshOrder (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq farmIdReq ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmOrderInfoVO  rValue=  proxy.execute("farm.refresh.order",session, proxy.gson.toJson(farmIdReq),com.pengpeng.stargame.vo.farm.FarmOrderInfoVO.class);
      return rValue;
}
/**
*完成订单
*/
public com.pengpeng.stargame.vo.farm.FarmOrderInfoVO finishOrder (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.farm.FarmIdReq farmIdReq ) throws GameException{
      com.pengpeng.stargame.vo.farm.FarmOrderInfoVO  rValue=  proxy.execute("farm.finish.order",session, proxy.gson.toJson(farmIdReq),com.pengpeng.stargame.vo.farm.FarmOrderInfoVO.class);
      return rValue;
}

}