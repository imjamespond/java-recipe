package com.pengpeng.stargame.qinma.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.qinma.rule.QinMaRoomRule;
import com.pengpeng.stargame.vo.room.RoomVO;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-13下午5:15
 */
public interface IQinMaRoomRuleContainer extends IMapContainer<String,QinMaRoomRule> {

    public RoomVO getQinMaRoomVO();


    /**
     * 可否评价 好友的农场
     * @param roomEvaluate
     * @param friendId
     * @throws com.pengpeng.stargame.exception.AlertException
     */
    public void checkEvaluate(RoomEvaluate roomEvaluate,String friendId) throws AlertException;

    /**
     * 评价好友农场
     * @param roomEvaluate
     * @param qinMa
     */
    public void evaluate(RoomEvaluate roomEvaluate,QinMa qinMa
    );
}
