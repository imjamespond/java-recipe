package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.IMapDao;
import com.pengpeng.stargame.model.player.EventLog;

import java.util.List;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:08
 */
public interface IEventLogDao extends IMapDao<String,EventLog> {

	/**
	 * 获取事件日志
	 * @param eventType 事件日志类型
	 * @param pid 玩家id
	 * @return List<EventLog>
	 */
	public List<EventLog> getEventLog(Integer eventType,String pid);

}
