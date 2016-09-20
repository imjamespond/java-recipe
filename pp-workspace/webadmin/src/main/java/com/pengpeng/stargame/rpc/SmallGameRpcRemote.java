package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SmallGameRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取排行
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO getRank (Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("get.rank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*扣减次数
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO deduct (Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("small.game.deduct",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*更新排行
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO updateRank (Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("update.rank",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*获取小游戏列表
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameListVO getlist (Session maxKey, com.pengpeng.stargame.vo.smallgame.SmallGameReq weekKey ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameListVO  rValue=  proxy.execute("get.list",maxKey, proxy.gson.toJson(weekKey),com.pengpeng.stargame.vo.smallgame.SmallGameListVO.class);
      return rValue;
}
/**
*达人币购买次数
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO goldBuy (Session session, com.pengpeng.stargame.vo.smallgame.SmallGameReq req ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("gold.buy",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}
/**
*产生每天奖励
*/
public com.pengpeng.stargame.vo.smallgame.SmallGameRankVO dayReward (Session typedTuple, com.pengpeng.stargame.vo.smallgame.SmallGameReq pid ) throws GameException{
      com.pengpeng.stargame.vo.smallgame.SmallGameRankVO  rValue=  proxy.execute("day.reward",typedTuple, proxy.gson.toJson(pid),com.pengpeng.stargame.vo.smallgame.SmallGameRankVO.class);
      return rValue;
}

}