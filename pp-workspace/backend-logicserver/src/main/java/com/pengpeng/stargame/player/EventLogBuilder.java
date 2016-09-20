package com.pengpeng.stargame.player;

import com.pengpeng.stargame.model.player.EventLog;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-1上午11:01
 */

@Component
public class EventLogBuilder {

    /**
     * 好友事件
     * @param pid
     * @param content
     * @return
     */
    public EventLog newFriendEvent(String pid,String content){
        EventLog event = new EventLog(pid,1,content);
        return event;
    }

    /**
     * 礼物事件
     * @param pid
     * @param content
     * @return
     */
    public EventLog newGiftEvent(String pid,String content){
        EventLog event = new EventLog(pid,2,content);
        return event;
    }

    /**
     * 货币消耗事件
     * @param pid
     * @param content
     * @return
     */
    public EventLog newGoldEvent(String pid,String content){
        EventLog event = new EventLog(pid,3,content);
        return event;
    }

    /**
     * 其它
     * @param pid
     * @param content
     * @return
     */
    public EventLog newOtherEvent(String pid,String content){
        EventLog event = new EventLog(pid,0,content);
        return event;
    }
}
