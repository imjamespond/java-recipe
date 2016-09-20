package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class LotteryRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*抽奖
*/
public com.pengpeng.stargame.vo.lottery.LotteryVO lotteryDraw (com.pengpeng.stargame.rpc.Session sessions, com.pengpeng.stargame.vo.lottery.LotteryReq content ) throws GameException{
      com.pengpeng.stargame.vo.lottery.LotteryVO  rValue=  proxy.execute("lotter.draw",sessions, proxy.gson.toJson(content),com.pengpeng.stargame.vo.lottery.LotteryVO.class);
      return rValue;
}
/**
*抽奖信息
*/
public com.pengpeng.stargame.vo.lottery.LotteryInfoVO lotteryInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.lottery.LotteryReq req ) throws GameException{
      com.pengpeng.stargame.vo.lottery.LotteryInfoVO  rValue=  proxy.execute("lotter.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.lottery.LotteryInfoVO.class);
      return rValue;
}

}