package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyActivityRpcRemote;
import com.pengpeng.stargame.rpc.FamilyInfoRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族信息,加入家族
 * User: mql
 * Date: 13-6-27
 * Time: 下午2:48
 */
@Component
public class FamilyCmd extends AbstractHandler {

    @Autowired
    private FamilyInfoRpcRemote familyInfoRpcRemote;

    @Autowired
    private FamilyActivityRpcRemote familyActivityRpcRemote;

//    @CmdAnnotation(cmd = "family.info.my", req = FamilyReq.class, name = "取得自己所在家族信息",vo= FamilyInfoVO.class)
//    public Response getMyInfo(Session session, FamilyReq req) throws GameException {
//        FamilyInfoVO vo = familyInfoRpcRemote.getFamilyInfo(session,req);
//        return  Response.newObject(vo);
//    }

    @CmdAnnotation(cmd = "family.get", req = FamilyReq.class, name = "取得家族资料",vo= FamilyInfoVO.class)
    public Response getFamily(Session session, FamilyReq req) throws GameException {
        FamilyInfoVO vo = familyInfoRpcRemote.getFamilyById(session,req);
        return  Response.newObject(vo);
    }


    @CmdAnnotation(cmd = "family.get.panel", req = FamilyReq.class, name = "获取家族面板信息",vo= FamilyPanelVO.class)
    public Response getInfo(Session session, FamilyReq req) throws GameException {
        FamilyPanelVO vo = familyInfoRpcRemote.getPanel(session,req);
        return  Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "familyinfo.alter", req = FamilyReq.class, name = "修改家族信息",vo= FamilyPanelVO.class)
    public Response alter(Session session, FamilyReq req) throws GameException {
        FamilyPanelVO vo = familyInfoRpcRemote.alter(session,req);
        return  Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "familyinfo.getFamilys", req = FamilyReq.class, name = "家族列表",vo= FamilyPageVO.class)
    public Response getFamilys(Session session, FamilyReq req) throws GameException {
        FamilyPageVO vo = familyInfoRpcRemote.getFamilyList(session,req);
        return  Response.newObject(vo);
    }


    @CmdAnnotation(cmd = "family.list.active", req = FamilyReq.class, name = "家族活动列表",vo= void.class)
    public Response listActive(Session session, FamilyReq req) throws GameException {
        ActivityVO[] vos = familyActivityRpcRemote.getEvents(session,req);
        return  Response.newObject(vos);
    }

}
