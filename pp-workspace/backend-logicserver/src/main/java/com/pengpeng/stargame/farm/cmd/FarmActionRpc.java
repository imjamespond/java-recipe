package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.farm.dao.IFarmActionDao;
import com.pengpeng.stargame.farm.dao.IFarmMessageDao;
import com.pengpeng.stargame.model.farm.FarmActionInfo;
import com.pengpeng.stargame.model.farm.FarmMessage;
import com.pengpeng.stargame.model.farm.FarmMessageInfo;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFarmActionFactory;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.farm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:35
 */
@Component()
public class FarmActionRpc extends RpcHandler{

    @Autowired
     private IFarmActionDao farmActionDao;
    @Autowired
    private IFarmMessageDao farmMessageDao;
    @Autowired
    private RspFarmActionFactory farmActionFactory;

    @RpcAnnotation(cmd = "farm.action.newnum", lock = false, req = FarmActionReq.class, name = "进入农场时候获取最新的动态数量")
    public int getNewActionNum(Session session,FarmActionReq req){
        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(session.getPid());
        return farmActionInfo.getNewNum();
    }
    @RpcAnnotation(cmd = "farm.action.info", lock = false, req = FarmActionReq.class, name = "取得玩家农场动态信息")
    public FarmActionInfoVO getFarmActionInfoVO(Session session,FarmActionReq req){
        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(session.getPid());
        farmActionInfo.setLastNumTime(new Date());
        farmActionDao.saveBean(farmActionInfo);
        return farmActionFactory.getFarmActionInfoVO(farmActionInfo);
    }
    @RpcAnnotation(cmd = "farm.message.list", lock = false, req = FarmActionReq.class, name = "取得玩家留言信息列表")
    public FarmMessageInfoVO getMessageList(Session session,FarmActionReq req){

        FarmMessageInfo farmMessageInfo=farmMessageDao.getFarmMessageInfo(req.getFid());
        if(session.getPid().equals(req.getFid())){
            farmMessageInfo.setLastNumTime(new Date());
            farmMessageDao.saveBean(farmMessageInfo);
        }
        return farmActionFactory.getFarmMessageInfoVO(farmMessageInfo);
    }
    @RpcAnnotation(cmd = "farm.message.add", lock = false, req = FarmActionReq.class, name = "添加留言")
    public FarmMessageInfoVO addMessage(Session session,FarmActionReq req){
        FarmMessageInfo farmMessageInfo=farmMessageDao.getFarmMessageInfo(req.getFid());
        FarmMessage farmMessage=new FarmMessage(Uid.uuid(),session.getPid(),req.getContent());
        farmMessageInfo.addMessage(farmMessage);
        farmMessageDao.saveBean(farmMessageInfo);
        return farmActionFactory.getFarmMessageInfoVO(farmMessageInfo);
    }
}
