package com.pengpeng.stargame.farm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmOrderRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmOrderDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmOrder;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFarmOrderFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmOrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单接口
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-26下午3:39
 */
@Component
public class FarmOrderRpc extends RpcHandler {

    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFarmOrderDao farmOrderDao;
    @Autowired
    private RspFarmOrderFactory rspFarmOrderFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmOrderRuleContainer farmOrderRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private FarmPkgRpc farmPkgRpc;
    @Autowired
    private FarmRpc farmRpc;
    @Autowired
    private PlayerRpc playerRpc;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;




    private FarmOrderInfoVO getFarmOrderInfoVO(String pId){
        FarmOrder farmOrder=farmOrderDao.getFarmOrder(pId) ;
        FarmPackage farmPackage=farmPackageDao.getBean(pId);
        return rspFarmOrderFactory.transitionVO(farmOrder,farmPackage);
    }

    @RpcAnnotation(cmd="farm.get.orderlist",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="获取当前订单列表",lock = false)
    public FarmOrderInfoVO getOrderList(Session session,FarmIdReq farmIdReq){
       return this.getFarmOrderInfoVO(session.getPid());
    }

    @RpcAnnotation(cmd="farm.refresh.order",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="刷新订单,取得订单信息",lock = true)
    public FarmOrderInfoVO refreshOrder(Session session,FarmIdReq farmIdReq) throws AlertException {

        Player player=playerDao.getBean(session.getPid());
        FarmOrder farmOrder=farmOrderDao.getFarmOrder(session.getPid());

        if(farmOrder.getFinishNum()> farmOrderRuleContainer.getMaxOrderNum(session.getPid())) {
            exceptionFactory.throwAlertException("farm.order.finishAll");
        }

        farmOrderRuleContainer.checkFresh(player,farmPlayerDao.getFarmLevel(farmIdReq.getPid()));
        farmOrderRuleContainer.refresh(player,farmOrder,farmPlayerDao.getFarmLevel(farmIdReq.getPid()));

        playerDao.saveBean(player);
        farmOrderDao.saveBean(farmOrder);

        Session[] mysessions={session};
        frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));

        return this.getFarmOrderInfoVO(session.getPid());
    }



    @RpcAnnotation(cmd="farm.finish.order",vo=FarmOrderInfoVO.class,req=FarmIdReq.class,name="完成订单",lock = true)
    public FarmOrderInfoVO finishOrder(Session session,FarmIdReq farmIdReq) throws AlertException{
        Player player=playerDao.getBean(session.getPid());
        FarmOrder farmOrder=farmOrderDao.getFarmOrder(session.getPid());
        FarmPackage farmPackage=farmPackageDao.getBean(session.getPid());
        FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
        if(farmOrder.getOneOrder(farmIdReq.getOid())==null){
            exceptionFactory.throwAlertException("farm.order.notfound");
        }
        if(farmOrder.getFinishNum()>=farmOrderRuleContainer.getMaxOrderNum(session.getPid())) {
            exceptionFactory.throwAlertException("farm.order.finishAll");
        }
        if(farmIdReq.getType()!=1&&farmIdReq.getType()!=2){
            return null;
        }
        farmOrderRuleContainer.checkFinishOrder(player,farmPackage,farmOrder.getOneOrder(farmIdReq.getOid()).getOrderId(),farmIdReq.getType());

        farmOrderRuleContainer.finishOrder(farmPlayer, player,farmPackage, farmOrder, farmIdReq.getOid(),farmIdReq.getType());

        farmPackageDao.saveBean(farmPackage);
        farmOrderDao.saveBean(farmOrder);
        playerDao.saveBean(player);
        farmPlayerDao.saveBean(farmPlayer);



        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_10,"",1);
        //成就
        successRuleContainer.updateSuccessNum(session.getPid(),4,1,"");

        Session[] mysessions={session};
        frontendService.broadcast(mysessions,farmPkgRpc.getItemByPid(session.getPid()));
        frontendService.broadcast(mysessions,farmRpc.getFarmVoByPlayerId(session.getPid()));
        frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));




        return this.getFarmOrderInfoVO(session.getPid());
    }

}
