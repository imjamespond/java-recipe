package com.pengpeng.stargame.integral.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.integral.dao.IPlayerIntegralDao;
import com.pengpeng.stargame.model.integral.IntegralAction;
import com.pengpeng.stargame.model.integral.PlayerIntegralShow;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.integral.IntegralActionVO;
import com.pengpeng.stargame.vo.integral.IntegralReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午4:52
 */
@Component
public class IntegralRpc extends RpcHandler {
    @Autowired
    private IPlayerIntegralDao playerIntegralDao;
    @RpcAnnotation(cmd = "player.integralActions", lock = true, req = IntegralReq.class, name = "积分获取动态信息")
    public IntegralActionVO[] integralActions(Session session, IntegralReq req) throws GameException {
        PlayerIntegralShow playerIntegralShow=playerIntegralDao.getPlayerIntegralShow(session.getPid());
        List<IntegralActionVO> integralActionVOList=new ArrayList<IntegralActionVO>();
        for(IntegralAction integralAction:playerIntegralShow.getIntegralActionList()){
            IntegralActionVO integralActionVO=new IntegralActionVO();
            integralActionVO.setType(integralAction.getType());
            integralActionVO.setNum(integralAction.getNum());
            integralActionVOList.add(integralActionVO);
        }
        return integralActionVOList.toArray(new IntegralActionVO[0]);
    }
}
