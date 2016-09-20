package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisMapDao;
import com.pengpeng.stargame.model.player.EventLog;
import com.pengpeng.stargame.player.dao.IEventLogDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-6-19 上午10:21
 */
@Component()
@DaoAnnotation(prefix = "eventLog.")
public class EventLogDaoImpl extends RedisMapDao<String,EventLog> implements IEventLogDao{

	@Override
	public Class<EventLog> getClassType() {
		return EventLog.class;
	}

	@Override
	public List<EventLog> getEventLog(Integer eventType, String pid) {
		HashMap<String, EventLog> map =  super.findAll(pid);
		List<EventLog> list = new ArrayList<EventLog>();
		if(map == null || map.isEmpty()){
			return list;
		}

		for(Map.Entry<String, EventLog> entry : map.entrySet()){
			EventLog e = entry.getValue();
			if(e == null){
				continue;
			}

			if(eventType != null){
				if(e.getEventType() == eventType.intValue()){
					list.add(e);
				}
			}else{
				list.add(e);
			}
		}
		return list;
	}

}
