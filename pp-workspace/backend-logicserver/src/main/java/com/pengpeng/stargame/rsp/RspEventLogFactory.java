package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.player.EventLog;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.EventLogVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事件日志
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:20
 */
@Component()
public class RspEventLogFactory extends RspFactory {

	public EventLogVO[] buildEventLogVO(List<EventLog> list) {
		if(list == null || list.isEmpty()){
			return new EventLogVO[0];
		}
		List<EventLogVO> logVOs = new ArrayList<EventLogVO>();

		for(EventLog eventLog : list){
			Date date = eventLog.getCreateTime();

			EventLogVO vo = new EventLogVO();
//			vo.setPid(eventLog.getPid());
			vo.setEventType(eventLog.getEventType());
			vo.setContent(eventLog.getContent());
			if(date !=null){
				vo.setCreateTime(DateUtil.parseDateString(date,"yyyy年MM月dd日 HH:mm:ss"));
			}

			logVOs.add(vo);
		}
		return logVOs.toArray(new EventLogVO[0]);
	}
}
