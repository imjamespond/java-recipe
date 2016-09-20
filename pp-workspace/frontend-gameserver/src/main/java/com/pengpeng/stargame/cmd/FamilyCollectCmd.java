package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyCollectRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeOverride;

/**
 * User: mql
 * Date: 14-3-26
 * Time: 上午10:46
 */
@Component
public class FamilyCollectCmd extends AbstractHandler {
    @Autowired
    private FamilyCollectRpcRemote familyCollectRpcRemote;
    @CmdAnnotation(cmd = "family.collect.info", req = FamilyCollectReq.class, name = "收集面板信息",vo=FamilyCollectInfoVO.class)
    public Response collectInfo(Session session, FamilyCollectReq req) throws GameException {
        FamilyCollectInfoVO familyCollectInfoVO=  familyCollectRpcRemote.collectInfo(session,req);
        return Response.newObject(familyCollectInfoVO);
    }

    @CmdAnnotation(cmd = "family.collect.donate", req = FamilyCollectReq.class, name = "捐献物品",vo=FamilyCollectInfoVO.class)
    public Response donate(Session session, FamilyCollectReq req) throws GameException {
        FamilyCollectInfoVO familyCollectInfoVO=  familyCollectRpcRemote.donate(session,req);
        return Response.newObject(familyCollectInfoVO);
    }

    @CmdAnnotation(cmd = "family.collect.memberrank", req = FamilyCollectReq.class, name = "本族排行",vo=MemberColletPageVO.class)
    public Response  memberrank(Session session, FamilyCollectReq req) throws GameException {
        MemberColletPageVO memberColletPageVO=  familyCollectRpcRemote.memberrank(session,req);
        return Response.newObject(memberColletPageVO);
    }
    @CmdAnnotation(cmd = "family.collect.familyrank", req = FamilyCollectReq.class, name = "家族排行",vo=FamilyCollectRankVO[].class)
    public Response familyrank(Session session, FamilyCollectReq req) throws GameException {
        FamilyCollectRankVO [] familyCollectRankVOs=   familyCollectRpcRemote.familyrank(session,req);
        return Response.newObject(familyCollectRankVOs);
    }
}
