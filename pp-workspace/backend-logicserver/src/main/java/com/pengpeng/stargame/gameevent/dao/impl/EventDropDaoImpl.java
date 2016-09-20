package com.pengpeng.stargame.gameevent.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午11:30
 */
@Component
@DaoAnnotation(prefix = "gameevent.drop.")
public class EventDropDaoImpl extends RedisDao<EventDrop> implements IEventDropDao{
    @Override
    public Class<EventDrop> getClassType() {
        return EventDrop.class;
    }

    @Override
    public EventDrop getEventDrop(String sceneId) {
        EventDrop eventDrop=getBean(sceneId);
        if(eventDrop==null){
            eventDrop=new EventDrop(sceneId) ;
            saveBean(eventDrop);
            return eventDrop;
        }
        if(eventDrop.clear()){
            saveBean(eventDrop);
        }
        return eventDrop;
    }
}
