package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.RankRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.rank.RankReq;
import com.pengpeng.stargame.vo.rank.RankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-15
 * Time: 下午4:30
 */
@Component
public class RankCmd extends AbstractHandler {
    @Autowired
    private RankRpcRemote rankRpcRemote;
    @CmdAnnotation(cmd = "rank.rankInfo", req = RankReq.class, name = "排行榜信息",vo=RankVO[].class)
    public Response rankInfo(Session session, RankReq req) throws GameException {
        return Response.newObject(rankRpcRemote.rankInfo(session,req)) ;
    }
}
