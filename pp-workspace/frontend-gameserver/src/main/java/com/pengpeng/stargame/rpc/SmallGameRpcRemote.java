package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class SmallGameRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取排行
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO getRank (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("get.rank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*扣减次数
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO deduct (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("small.game.deduct",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*更新排行
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO updateRank (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("update.rank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*获取小游戏列表
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameListVO getlist (com.pengpeng.stargame.rpc.Session maxKey, com.pengpeng.stargame.vo.smallgame.SmallGameReq weekKey ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameListVO  rValue=  proxy.execute("get.list",maxKey, proxy.gson.toJson(weekKey),com.pengpeng.stargame.vo.smallgame.SmallGameListVO.class);
      return rValue;
}
/**
*达人币购买次数
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameListVO goldBuy (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameListVO  rValue=  proxy.execute("gold.buy",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameListVO.class);
      return rValue;
}
/**
*产生每天奖励
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO dayReward (com.pengpeng.stargame.rpc.Session title, com.pengpeng.stargame.vo.smallgame.SmallGameReq content ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("day.reward",title, proxy.gson.toJson(content),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}

}