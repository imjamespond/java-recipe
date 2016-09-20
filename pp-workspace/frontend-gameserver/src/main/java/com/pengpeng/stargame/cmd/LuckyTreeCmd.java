package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.LuckyTreeRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SuccessiveRpcRemote;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeCallVO;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessiveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author james
 * @since 13-6-13 下午3:53
 */
@Component
public class LuckyTreeCmd extends AbstractHandler {

	@Autowired
	private LuckyTreeRpcRemote luckyTreeRpcRemote;

	@CmdAnnotation(cmd="lucky.tree.info",name="招财树信息",vo= LuckyTreeVO.class,req=LuckyTreeReq.class)
	public Response info(Session session,LuckyTreeReq req) throws GameException {
		return Response.newObject(luckyTreeRpcRemote.getInfo(session, req));
	}

    @CmdAnnotation(cmd="lucky.tree.call",name="招财树招财",vo= LuckyTreeCallVO.class,req=LuckyTreeReq.class)
    public Response call(Session session,LuckyTreeReq req) throws GameException {
        return Response.newObject(luckyTreeRpcRemote.call(session, req));
    }

    @CmdAnnotation(cmd="lucky.tree.water",name="招财树浇水",vo= LuckyTreeVO.class,req=LuckyTreeReq.class)
    public Response water(Session session,LuckyTreeReq req) throws GameException {
        return Response.newObject(luckyTreeRpcRemote.water(session, req));
    }

    @CmdAnnotation(cmd="lucky.tree.add",name="增加达人次数币",vo= LuckyTreeVO.class,req=LuckyTreeReq.class)
    public Response add(Session session,LuckyTreeReq req) throws GameException {
        return Response.newObject(luckyTreeRpcRemote.add(session, req));
    }

}
