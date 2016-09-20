package com.pengpeng.stargame.rank.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.rank.container.IRankRuleContainer;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.rank.RankReq;
import com.pengpeng.stargame.vo.rank.RankTypeVO;
import com.pengpeng.stargame.vo.rank.RankVO;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午6:01
 */
@Component()
public class RankRpc extends RpcHandler {
    @Autowired
    private IRankRuleContainer rankRuleContainer;
    @RpcAnnotation(cmd = "rank.rankInfo", req = RankReq.class, name = "排行榜信息",vo=RankVO[].class)
    public RankTypeVO rankInfo(Session session, RankReq req) throws AlertException {
        RankTypeVO rankTypeVO=new RankTypeVO();
        rankTypeVO.setRankVOs(rankRuleContainer.getRankVO(req.getType()));
        rankTypeVO.setType(req.getType());
        return rankTypeVO;

    }

}
