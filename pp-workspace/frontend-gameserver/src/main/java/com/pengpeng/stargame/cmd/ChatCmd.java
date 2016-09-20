package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.ChatRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.chat.ChatReq;
import com.pengpeng.stargame.vo.chat.ShoutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-20下午12:23
 */
@Component
public class ChatCmd extends AbstractHandler {

    @Autowired
    private ChatRpcRemote chatRemote;

    @CmdAnnotation(cmd="chat.talk",name="发消息",vo=void.class,req=ChatReq.class)
    public Response talk(Session session,ChatReq req) throws GameException {
        chatRemote.talk(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="chat.shout",name="千里传音",vo=void.class,req=ChatReq.class)
    public Response shout(Session session,ChatReq req) throws GameException {
        chatRemote.shout(session,req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="chat.shout.info",name="千里传音信息",vo=ShoutVO.class,req=ChatReq.class)
    public Response shoutInfo(Session session,ChatReq req) throws GameException {

        return Response.newObject(chatRemote.shoutInfo(session,req));
    }


}
