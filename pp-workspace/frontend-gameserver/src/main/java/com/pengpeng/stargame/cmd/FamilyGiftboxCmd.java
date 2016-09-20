package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.FamilyGiftBoxRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族礼物盒
 * User: mql
 * Date: 13-6-27
 * Time: 下午2:48
 */
@Component
public class FamilyGiftboxCmd extends AbstractHandler {
    @Autowired
    private FamilyGiftBoxRpcRemote familyGiftBoxRpcRemote;
    @CmdAnnotation(cmd = "family.gift.info", req = StarGiftReq.class, name = "礼物盒界面",vo=  StarGiftPageVO.class)
    public Response  getInfo(Session session, StarGiftReq req) throws GameException {

        return Response.newObject(familyGiftBoxRpcRemote.getInfo(session,req));
    }

    @CmdAnnotation(cmd = "family.gift.giftList", req = StarGiftReq.class, name = "送礼时候 礼物列表",vo=  StarSendPageVO.class)
    public Response  giftList(Session session, StarGiftReq req) throws GameException {


        return Response.newObject(familyGiftBoxRpcRemote.giftList(session,req));
    }

    @CmdAnnotation(cmd = "family.gift.sendGift", req = StarGiftReq.class, name = "送礼",vo=  void.class)
    public Response  sendGift(Session session, StarGiftReq req) throws GameException {
        familyGiftBoxRpcRemote.sendGift(session,req);
        return Response.newOK();

    }

}
