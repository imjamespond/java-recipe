package com.pengpeng.stargame.qinma.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.qinma.container.IQinMaRoomRuleContainer;
import com.pengpeng.stargame.qinma.dao.IQinMaDao;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.qinma.rule.QinMaRoomRule;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.pengpeng.stargame.vo.room.DecorateVO;
import com.pengpeng.stargame.vo.room.RoomVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-13下午5:16
 */
@Component
public class QinMaRoomRuleContainerImpl extends HashMapContainer<String,QinMaRoomRule> implements IQinMaRoomRuleContainer {
    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;
    @Autowired
    private IQinMaDao qinMaDao;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Override
    public RoomVO getQinMaRoomVO() {
        QinMaRoomRule qinMaRoomRule=getElement(Constant.QINMA_ID);
        RoomVO roomVO=new RoomVO(qinMaRoomRule.getId());
        roomVO.setName(qinMaRoomRule.getName());
        roomVO.setGoodReputation(qinMaDao.getQinMa(Constant.QINMA_ID).getRoomEvaluation());
        int luxuryDegree=0;
        List<DecorateVO> decorateVOList=new ArrayList<DecorateVO>();
        for(DecoratePosition decoratePosition : qinMaRoomRule.getItems().values()){
            RoomItemRule roomItemRule=roomItemRuleContainer.getElement(decoratePosition.getItemId());
            luxuryDegree+=roomItemRule.getLuxuryDegree();
            DecorateVO decorateVO=new DecorateVO();
            BeanUtils.copyProperties(decoratePosition, decorateVO);
            decorateVOList.add(decorateVO);

            decorateVO.setType(roomItemRule.getItemtype());
            decorateVO.setImage(roomItemRule.getImage());
        }
        roomVO.setDecorateVOs(decorateVOList.toArray(new DecorateVO [0]));
        roomVO.setX(qinMaRoomRule.getX());
        roomVO.setY(qinMaRoomRule.getY());
        roomVO.setGlamour(luxuryDegree);
        roomVO.setName(qinMaRoomRule.getName());

        return   roomVO;
    }

    @Override
    public void checkEvaluate(RoomEvaluate roomEvaluate,String friendId) throws AlertException {
        boolean bool = roomEvaluate.isEvaluate(friendId);
        if (bool){
            exceptionFactory.throwAlertException("evaluate");
        }
    }

    @Override
    public void evaluate(RoomEvaluate roomEvaluate, QinMa qinMa) {
        roomEvaluate.evaluate(qinMa.getId());
        qinMa.incRoomEvaluation(1);
    }
}
