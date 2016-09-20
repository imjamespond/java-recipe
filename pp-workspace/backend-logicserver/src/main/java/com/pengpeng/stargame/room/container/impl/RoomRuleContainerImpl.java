package com.pengpeng.stargame.room.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.room.container.IRoomRuleContainer;
import com.pengpeng.stargame.room.rule.RoomRule;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午5:19
 */
@Component
public class RoomRuleContainerImpl extends HashMapContainer<String,RoomRule> implements IRoomRuleContainer {

    @Override
    public void checkEvaluate(RoomEvaluate roomEvaluate,String friendId) throws AlertException {
        boolean bool = roomEvaluate.isEvaluate(friendId);
        if (bool){
            throw new AlertException("evaluate");
        }
    }

    @Override
    public void evaluate(RoomEvaluate roomEvaluate, RoomEvaluate friendRoomEvaluate) {
        roomEvaluate.evaluate(friendRoomEvaluate.getpId());
        friendRoomEvaluate.incNum(1);
    }


}
