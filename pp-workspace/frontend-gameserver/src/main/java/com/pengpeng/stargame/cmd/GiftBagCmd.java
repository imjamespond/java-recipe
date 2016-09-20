package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.GiftBagRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.giftBag.GiftBagReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-12
 * Time: 下午4:49
 */
@Component
public class GiftBagCmd  extends AbstractHandler {
    @Autowired
    private GiftBagRpcRemote rpcRemote;
    @CmdAnnotation(cmd = "giftbag.open", vo = RewardVO.class, req = GiftBagReq.class, name = "打开礼包")
    public Response getGiftBag(Session session, GiftBagReq req) throws GameException {
        return Response.newObject(rpcRemote.getGiftBag(session,req));
    }
}
