package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyManagerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族成员管理
 * User: mql
 * Date: 13-6-27
 * Time: 下午2:48
 */
@Component
public class FamilyMemberCmd extends AbstractHandler {

    @Autowired
    private FamilyManagerRpcRemote familyManagerRpcRemote;

    @CmdAnnotation(cmd = "family.get.member", req = FamilyReq.class, name = "获取家族成员信息",vo=  FamilyMemberPageVO.class)
    public Response  getInfo(Session session, FamilyReq req) throws GameException {
        FamilyMemberVO vos = familyManagerRpcRemote.getMembers(session,req);
        return  Response.newObject(vos);
    }

    @CmdAnnotation(cmd = "family.list.member", req = FamilyReq.class, name = "获取成员列表",vo=  FamilyMemberPageVO.class)
    public Response  getMembers(Session session, FamilyReq req) throws GameException {
        FamilyMemberPageVO vos = familyManagerRpcRemote.listMembers(session,req);
        return  Response.newObject(vos);
    }

    @CmdAnnotation(cmd = "family.del.member", req = FamilyReq.class, name = "删除家族成员",vo=  void.class)
    public Response  removeMembers(Session session, FamilyReq req) throws GameException {
        familyManagerRpcRemote.removeMember(session,req);
        return  Response.newOK();
    }
    @CmdAnnotation(cmd = "familyinfo.join", req = FamilyReq.class, name = "加入家族",vo= FamilyInfoVO.class)
    public Response join(Session session, FamilyReq req) throws GameException {
        FamilyInfoVO vo = familyManagerRpcRemote.join(session,req);
        return  Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "familyinfo.welcome", req = FamilyReq.class, name = "欢迎加入家族",vo= void.class)
    public Response welcome(Session session, FamilyReq req) throws GameException {
        familyManagerRpcRemote.welcome(session,req);
        return  Response.newOK();
    }

    @CmdAnnotation(cmd = "family.change", req = FamilyReq.class, name = "变更家族",vo= void.class)
    public Response change(Session session, FamilyReq req) throws GameException {
        familyManagerRpcRemote.changeFamily(session,req);
        return  Response.newOK();
    }

    @CmdAnnotation(cmd = "family.endow", req = FamilyReq.class, name = "家族捐献",vo= RewardVO.class)
    public Response endow(Session session, FamilyReq req) throws GameException {
        RewardVO vo = familyManagerRpcRemote.endow(session,req);
        return  Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "family.get.devote", req = FamilyReq.class, name = "取得家族捐献比例",vo= FamilyDevoteVO.class)
    public Response getDevote(Session session, FamilyReq req) throws GameException {
        FamilyDevoteVO vo = familyManagerRpcRemote.getDevoteInfo(session,req);
        return  Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "family.receive.boon",req = FamilyReq.class, name = "领取家族福利",vo= Integer.class)
    public Response getBoon(Session session, FamilyReq req) throws GameException {
        Integer val = familyManagerRpcRemote.getBoon(session,req);
        return Response.newObject(val);
    }
}
