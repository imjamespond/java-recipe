package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.EventLog;
import com.pengpeng.stargame.player.dao.IEventLogDao;
import com.pengpeng.stargame.player.dao.impl.PlayerDaoImpl;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspEventLogFactory;
import com.pengpeng.stargame.vo.EventLogReq;
import com.pengpeng.stargame.vo.EventLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:27
 */
@Component()
public class EventLogRpc extends RpcHandler {
	@Autowired
	private PlayerDaoImpl playerDao;

	@Autowired
	private IEventLogDao eventLogDao;

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Autowired
	private RspEventLogFactory factory;

	@RpcAnnotation(cmd="eventLog.get.all",vo=EventLogVO[].class,req=EventLogReq.class,name="取得玩家事件日志")
	public EventLogVO [] getEventLogAll(Session session,EventLogReq req) throws GameException{
		List<EventLog> list =  eventLogDao.getEventLog(req.getEventType(), session.getPid());
		if(list == null || list.isEmpty()){
			return new EventLogVO[0];
		}

		return factory.buildEventLogVO(list);
	}
}
