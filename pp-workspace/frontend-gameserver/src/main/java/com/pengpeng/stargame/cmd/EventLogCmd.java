package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.container.ISessionContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.rpc.EventLogRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.EventLogReq;
import com.pengpeng.stargame.vo.EventLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午11:27
 */
@Component
public class EventLogCmd extends AbstractHandler {
	@Autowired
	private PlayerRpcRemote playerService;

	@Autowired
	private StatusRemote statusService;

	@Autowired
	private ISessionContainer container;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private EventLogRpcRemote eventLogRpcRemote;

	@CmdAnnotation(cmd="eventLog.get.all",name="取得玩家事件日志",vo=EventLogVO[].class,req=EventLogReq.class)
	public Response getEventLogAll(Session session,EventLogReq req) throws GameException {
		return Response.newObject(eventLogRpcRemote.getEventLogAll(session,req));
	}

}
