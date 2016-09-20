package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.Broadcast;
import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.role.PlayerReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.pengpeng.stargame.vo.role.RoleReq;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-23下午2:48
 */
@Component
public class PlayerCmd extends AbstractHandler {

	@Autowired
	private PlayerRpcRemote playerRpcRemote;

    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private ISessionContainer container;
    @Autowired
    private Broadcast broadcast;


    @CmdAnnotation(cmd="p.enter",vo=PlayerVO.class,req=PlayerReq.class,channel=true,name="进入游戏")
    public Response enterGame(ChannelHandlerContext ctx,PlayerReq req) throws GameException {

        PlayerVO player = playerRpcRemote.enterGame(new Session("" ,""), req);
        String sceneid = player.getSceneId();
        Session session = container.createSession(player.getId(),sceneid);
        SessionUtil.setParam(session,SessionUtil.KEY_CHANNEL_FAMILY,player.getFamilyId());
        ctx.setAttachment(session);
        //登录的时候自动加入聊天频道,家族频道,场景频道等,在status服务器实现
        statusRemote.login(session, null);
        broadcast.addConnection(ctx, session);
        return Response.newObject(player);
    }
    @CmdAnnotation(cmd="p.enter.after",vo=void.class,req=CommonReq.class,channel=false,name="进入游戏之后必须调用的指令")
    public Response afterEnter(Session session,CommonReq req) throws GameException {
        playerRpcRemote.afterEnter(session, req);
        return Response.newOK();
    }
    @CmdAnnotation(cmd="p.rename",vo=String.class,req=PlayerReq.class,name="修改角色名称")
    public Response rename(Session session,RoleReq req) throws GameException {
        playerRpcRemote.rename(session, req);
        return Response.newOK();
    }

    @CmdAnnotation(cmd="p.get.info",vo=PlayerVO.class,req=IdReq.class,name="取得角色信息")
    public Response getPlayer(Session session,IdReq req) throws GameException {
        PlayerVO vo = playerRpcRemote.getPlayerInfo(session,req.getId());
        return Response.newObject(vo);
    }

//    @CmdAnnotation(cmd="p.get.friendinfo",vo=PlayerVO.class,req=PlayerReq.class,name="取得好友的信息")
//    public Response getPlayerInfo(Session session,PlayerReq req){
//        return null;
//    }

    @CmdAnnotation(cmd="p.get.randomname",vo=String.class,req=CommonReq.class,name="取得随机名称")
    public Response getRandomName(Session session,CommonReq commonReq){
        return null;
    }


    @CmdAnnotation(cmd="p.claim",name="公测期间每日领取达人币",vo=RewardVO.class,req=CommonReq.class)
    public Response listActive(Session session,CommonReq req) throws GameException {
        RewardVO vo = playerRpcRemote.claim(session,req);
        return Response.newObject(vo);
    }
}
