package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.rpc.FamilyBankRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.piazza.MoneyTreeReq;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午10:45
 */
@Component
public class FamilyBankCmd extends AbstractHandler {
    @Autowired
    private FamilyBankRpcRemote familyBankRpcRemote;
    @CmdAnnotation(cmd = "family.bank.info", req = FamilyBankReq.class, name = "家族银行面板信息", vo = FamilyBankVO.class)
    public Response rankInfo(Session session, FamilyBankReq req) throws GameException {

        return Response.newObject(familyBankRpcRemote.rankInfo(session, req));
    }
    @CmdAnnotation(cmd = "family.bank.get", req = FamilyBankReq.class, name = "取款", vo = FamilyBankVO.class)
    public Response rankGet(Session session, FamilyBankReq req) throws GameException {

        return Response.newObject(familyBankRpcRemote.rankGet(session, req));
    }

    @CmdAnnotation(cmd = "family.bank.save", req = FamilyBankReq.class, name = "存款", vo = FamilyBankVO.class)
    public Response rankSave(Session session, FamilyBankReq req) throws GameException {


        return Response.newObject(familyBankRpcRemote.rankSave(session, req));
    }

    @CmdAnnotation(cmd = "family.bank.playerMoney", req = FamilyBankReq.class, name = "获取玩家存款数量", vo = Integer.class)
    public Response getPlayerMoney(Session session, FamilyBankReq req) throws GameException {
        return Response.newObject(familyBankRpcRemote.getPlayerMoney(session,req));
    }

    }
