package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmProcessRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmProcessDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.farm.process.OneQueue;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.room.dao.impl.RoomPlayerDaoImpl;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO;
import com.pengpeng.stargame.vo.farm.process.FarmProcessReq;
import com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午5:38
 */
@Component()
public class FarmProcessRpc extends RpcHandler {
    @Autowired
    private RspFarmFactory rspFarmFactory;
    @Autowired
    private IFarmProcessDao farmProcessDao;
    @Autowired
    private IFarmProcessRuleContainer farmProcessRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @RpcAnnotation(cmd = "farm.process.list", name = "获取加工物品列表", vo = FarmProcessItemVO[].class, req = FarmProcessReq.class)
    public FarmProcessItemVO[] getprocesslist(Session session, FarmProcessReq req) throws GameException {
        return rspFarmFactory.getFramProcessItemVOS(session.getPid(), req.getType());
    }

    @RpcAnnotation(cmd = "farm.process.start", name = "开始生产", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO processstart(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        FarmPackage farmPackage=farmPackageDao.getBean(session.getPid());
        if (req.getProcessId() == null || req.getProcessId().length == 0) {
            return null;
        }
        farmProcessRuleContainer.checkStart(farmProcessQueue, req.getProcessId());
        farmProcessRuleContainer.start(farmProcessQueue, req.getProcessId(),farmPackage);
        farmProcessDao.saveBean(farmProcessQueue);
        farmPackageDao.saveBean(farmPackage);
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.cancel", name = "取消队列中的一个队列", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO processcancel(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        farmProcessRuleContainer.checkCancel(farmProcessQueue, req.getId());
        farmProcessRuleContainer.cancel(farmProcessQueue, req.getId());
        farmProcessDao.saveBean(farmProcessQueue);
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.finishone", name = "完成一个正在进行的队列", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO finishOne(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        farmProcessRuleContainer.finishOne(farmProcessQueue, req.getId(),player);

        playerDao.saveBean(player);
        farmProcessDao.saveBean(farmProcessQueue);

        frontendService.broadcast(session, roleFactory.newPlayerVO(player));

        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.needgold", name = "返回 直接完成 需要的达人币数量", vo = Integer.class, req = FarmProcessReq.class)
    public int needgold(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        int needGold=0;
        if(req.getGoldType()==1){
            needGold= farmProcessRuleContainer.getNeedgold(farmProcessQueue);
        } else if(req.getGoldType()==2){
            needGold= farmProcessRuleContainer.getOneNeedgold(farmProcessQueue,req.getId());
        }else {
            exceptionFactory.throwAlertException("参数类型不对！");
        }
        return  needGold;
    }

    @RpcAnnotation(cmd = "farm.process.finish", name = "完成正在进行的队列", vo = Integer.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO finish(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        Player player=playerDao.getBean(session.getPid());

        farmProcessRuleContainer.finish(farmProcessQueue,player);
        playerDao.saveBean(player);
        farmProcessDao.saveBean(farmProcessQueue);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.open", name = "开通格子", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO open(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        farmProcessRuleContainer.openGrid(farmProcessQueue,player);
        farmProcessDao.saveBean(farmProcessQueue);
        playerDao.saveBean(player);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));

        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.getProcessItem", name = "领取生成物品", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO getProcessItem(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        FarmPackage farmPackage=farmPackageDao.getBean(session.getPid());
        RoomPackege roomPackege=roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        farmProcessRuleContainer.getProecessGoods(farmProcessQueue,farmPackage,roomPackege,fashionCupboard,farmDecoratePkg);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        farmProcessDao.saveBean(farmProcessQueue);
        farmPackageDao.saveBean(farmPackage);
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.getProcessInfo", name = "获取玩家队列信息", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO getProcessInfo(Session session, FarmProcessReq req) throws GameException {
        String pid=req.getPid();
        if(null==pid){
            pid=session.getPid();
        }
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(pid);
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

    @RpcAnnotation(cmd = "farm.process.getFinished", name = "进入农场场景的时候 获取可以领取的 物品", vo = GoodsVO[].class, req = FarmProcessReq.class)
    public GoodsVO[] getFinished(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());

        return farmProcessRuleContainer.getFinish(farmProcessQueue).toArray(new GoodsVO[0]);
    }

    @RpcAnnotation(cmd = "farm.process.speedAll", name = "添加加速", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO speedAll(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        Player player=playerDao.getBean(session.getPid());
        farmProcessRuleContainer.speedAll(player,farmProcessQueue);
        farmProcessDao.saveBean(farmProcessQueue);
        playerDao.saveBean(player);

        frontendService.broadcast(session, roleFactory.newPlayerVO(player));
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }


    @RpcAnnotation(cmd = "farm.process.speedAllEnd", name = "加速倒计时完成", vo = FarmQueueInfoVO.class, req = FarmProcessReq.class)
    public FarmQueueInfoVO speedAllEnd(Session session, FarmProcessReq req) throws GameException {
        FarmProcessQueue farmProcessQueue = farmProcessDao.getFarmProcessQueue(session.getPid());
        farmProcessQueue.speedAllEnd();
        farmProcessDao.saveBean(farmProcessQueue);
        return rspFarmFactory.getFarmQueueInfoVO(session.getPid(), farmProcessQueue);
    }

}
