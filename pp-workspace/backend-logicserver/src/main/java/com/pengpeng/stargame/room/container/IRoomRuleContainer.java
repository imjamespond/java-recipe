package com.pengpeng.stargame.room.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.room.rule.RoomRule;

/**
 * 个人房间规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午4:02
 */
public interface IRoomRuleContainer extends IMapContainer<String,RoomRule> {


    /**
     * 可否评价 好友的农场
     * @param roomEvaluate
     * @param friendId
     * @throws AlertException
     */
    public void checkEvaluate(RoomEvaluate roomEvaluate,String friendId) throws AlertException;

    /**
     * 评价好友农场
     * @param roomEvaluate
     * @param friendRoomEvaluate
     */
    public void evaluate(RoomEvaluate roomEvaluate,RoomEvaluate friendRoomEvaluate);
}
