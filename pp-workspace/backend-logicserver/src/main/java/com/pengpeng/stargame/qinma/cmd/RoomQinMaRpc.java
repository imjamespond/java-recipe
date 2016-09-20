package com.pengpeng.stargame.qinma.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.qinma.dao.IQinMaDao;
import com.pengpeng.stargame.qinma.container.IQinMaRoomRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomEvaluateDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午4:44
 */
@Component
public class RoomQinMaRpc extends RpcHandler {
     @Autowired
    private IQinMaRoomRuleContainer qinMaRoomRuleContainer;
    @Autowired
    private IQinMaDao qinMaDao;
    @Autowired
    private IRoomEvaluateDao roomEvaluateDao;

    @RpcAnnotation(cmd = "room.qinma.info", req = RoomIdReq.class, name = "取得 亲妈 房间信息")
    public RoomVO getRoomInfo(Session session, RoomIdReq req) throws AlertException {
       return qinMaRoomRuleContainer.getQinMaRoomVO();
    }

    @RpcAnnotation(cmd = "room.qinma.evaluation", req = RoomIdReq.class, name = "评价 亲妈 房间",vo=RoomVO.class)
    public RoomVO evaluation(Session session, RoomIdReq req) throws AlertException {

        RoomEvaluate roomEvaluate= roomEvaluateDao.getRoomEvaluate(session.getPid());
        QinMa qinMa=qinMaDao.getQinMa(Constant.QINMA_ID);
        qinMaRoomRuleContainer.checkEvaluate(roomEvaluate,Constant.QINMA_ID);

        qinMaRoomRuleContainer.evaluate(roomEvaluate,qinMa);

        roomEvaluateDao.saveBean(roomEvaluate);
        qinMaDao.saveBean(qinMa);


        return qinMaRoomRuleContainer.getQinMaRoomVO();

    }
}
