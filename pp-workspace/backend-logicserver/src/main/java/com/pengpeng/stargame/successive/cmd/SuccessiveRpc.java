package com.pengpeng.stargame.successive.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFarmItemFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspSuccessiveFactory;
import com.pengpeng.stargame.successive.container.ISuccessiveContainer;
import com.pengpeng.stargame.successive.dao.IPlayerSuccessiveDao;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessiveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */
@Component
public class SuccessiveRpc extends RpcHandler {
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IPlayerSuccessiveDao playerSuccessiveDao;
    @Autowired
    private ISuccessiveContainer successiveContainer;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private RspSuccessiveFactory rspSuccessiveFactory;

    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private RspFarmItemFactory rspFarmItemFactory;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @RpcAnnotation(cmd="successive.getPrize",vo=SuccessiveInfoVO.class,req=SuccessiveReq.class,name="领取奖励",lock = true)
    public SuccessiveInfoVO getPrize(Session session,SuccessiveReq req) throws GameException {
        String pid = session.getPid();

        PlayerSuccessive playerSuccessive=playerSuccessiveDao.getPlayerSuccessive(pid);
        Player player = playerDao.getBean(pid);

        FarmPackage farmPackage=farmPackageDao.getBean(session.getPid());
        RoomPackege roomPackege=roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        successiveContainer.getPrize(req.getDay(),playerSuccessive,player,farmPackage, roomPackege, fashionCupboard,farmDecoratePkg);
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        playerDao.saveBean(player);
        playerSuccessiveDao.saveBean(playerSuccessive);

        //财富通知
        Session[] mysessions={session};
        frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));

        SuccessiveInfoVO vo = rspSuccessiveFactory.SuccessiveInfoVO(playerSuccessive.getDay(),playerSuccessive.getGetPrize(),null);

        //数字图标推送
        int num = successiveContainer.getNum(playerSuccessive.getDay(),playerSuccessive.getGetPrize());
        frontendService.broadcast(mysessions, new MsgVO(EventConstant.EVENT_SUCCESSIVE,num));
        return vo;
     }

    @RpcAnnotation(cmd="successive.info",vo=SuccessiveInfoVO.class,req=SuccessiveReq.class,name="连续登陆信息")
    public SuccessiveInfoVO SuccessiveInfo(Session session ,SuccessiveReq req) throws GameException {
        String pid = session.getPid();

        PlayerSuccessive playerSuccessive=playerSuccessiveDao.getPlayerSuccessive(pid);
        SuccessiveInfoVO vo = rspSuccessiveFactory.SuccessiveInfoVO(playerSuccessive.getDay(),playerSuccessive.getGetPrize(),RspSuccessiveFactory.getVO());
        return vo;
    }

}
