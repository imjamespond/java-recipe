package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.MoneyTreeRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.MoneyPickInfoVO;
import com.pengpeng.stargame.vo.piazza.MoneyTreeReq;
import com.pengpeng.stargame.vo.piazza.MoneyTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 下午4:02
 */
@Component
public class MoneyTreeCmd extends AbstractHandler {
    @Autowired
    private MoneyTreeRpcRemote moneyTreeRpcRemote;
    @CmdAnnotation(cmd = "moneytree.getinfo", req = MoneyTreeReq.class, name = "获取摇钱树信息",vo= MoneyTreeVO.class)
    public Response getinfo(Session session, MoneyTreeReq req) throws GameException {
        return  Response.newObject(moneyTreeRpcRemote.getinfo(session,req));
    }

    @CmdAnnotation(cmd = "moneytree.pickMoney", req = MoneyTreeReq.class, name = "捡取地面上的钱",vo= MoneyPickInfoVO.class)
    public Response pickMoney(Session session, MoneyTreeReq req) throws GameException {
       return Response.newObject(moneyTreeRpcRemote.pickMoney(session,req));
    }

    @CmdAnnotation(cmd = "moneytree.blessing", req = MoneyTreeReq.class, name = "祝福",vo= void.class)
    public Response blessing(Session session, MoneyTreeReq req) throws GameException {
        moneyTreeRpcRemote.blessing(session,req);
        return  Response.newOK();
    }

    @CmdAnnotation(cmd = "moneytree.rock", req = MoneyTreeReq.class, name = "摇钱",vo= void.class)
    public Response rock(Session session, MoneyTreeReq req) throws GameException {
        moneyTreeRpcRemote.rock(session,req);
        return  Response.newOK();
    }
}
