package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.lottery.dao.IPlayerlotteryDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerInfo;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.container.IActiveRuleContainer;
import com.pengpeng.stargame.player.dao.IActivePlayerDao;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerInfoDao;
import com.pengpeng.stargame.player.dao.impl.PlayerDaoImpl;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.small.game.cmd.SmallGameRpc;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import com.pengpeng.stargame.successive.container.ISuccessiveContainer;
import com.pengpeng.stargame.successive.dao.IPlayerSuccessiveDao;
import com.pengpeng.stargame.util.SceneConstant;
import com.pengpeng.stargame.util.StringTrimUtils;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.map.MoveVO;
import com.pengpeng.stargame.vo.role.*;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 人物信息
 * @auther jian.huang@pengpeng.com
 * @since: 13-11-11上午11:57
 */
@Component()
public class PlayerInfoRpc extends RpcHandler {
    private static final Logger logger = Logger.getLogger(PlayerInfoRpc.class);
    @Autowired
    private PlayerDaoImpl playerDao;
    @Autowired
    private IPlayerInfoDao playerInfoDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyDao familyDao;

    @Autowired
    private RspRoleFactory roleFactory;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IFarmPlayerDao farmPlayerDao;

    @Autowired
    private IRoomPlayerDao roomPlayerDao;

    @Autowired
    private IFashionPlayerDao fashionPlayerDao;

    @Autowired
    private SmallGameRpc smallGameRpc;
    @Autowired
    private IPlayerSuccessDao playerSuccessDao;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @RpcAnnotation(cmd="player.info",lock = false,req=IdReq.class,name="取得人物信息")
    public PlayerInfoVO getPlayerInfo(Session session,IdReq idReq) throws AlertException {
        String pid = idReq.getId();
        Player player = playerDao.getBean(pid);
        if (null==player){
            exceptionFactory.throwAlertException("p.notfound");
        }
        PlayerInfo playerInfo=playerInfoDao.getPlayerInfo(pid);
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        FarmPlayer fpObj = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(pid);
        RoomPlayer roomPlayer = roomPlayerDao.getRoomPlayer(pid);
        PlayerInfoVO vo = roleFactory.newPlayerInfoVO(player,playerInfo,family,familyMemberInfo,fpObj,fashionPlayer,roomPlayer);

        //小游戏排行
        SmallGameReq req = new SmallGameReq();
        req.setPid(pid);
        SmallGameListVO sVO = smallGameRpc.getMyRank( null, req);
        vo.setGameRank(sVO);

        //成就
        PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(session.getPid());
        vo.setTitle(successRuleContainer.getElement(playerSuccessInfo.getUseId()).getName());
        return vo;
    }


    @RpcAnnotation(cmd="player.district.update",lock = true,req=PlayerDistrictReq.class,name="更新区域信息")
    public PlayerInfoVO updatePlayerDistrict(Session session,PlayerDistrictReq req) throws AlertException  {
        PlayerInfo playerInfo=playerInfoDao.getPlayerInfo(req.getPid());
        playerInfo.setProvince(req.getProvince());
        playerInfo.setCity(req.getCity());
        playerInfoDao.saveBean(playerInfo);

        IdReq idReq = new IdReq();
        idReq.setId(req.getPid());
        return getPlayerInfo(null,idReq);
    }

    @RpcAnnotation(cmd="player.title.update",lock = true,req=PlayerTitleReq.class,name="更新称号信息")
    public PlayerInfoVO updatePlayerTitle(Session session,PlayerTitleReq req) throws AlertException  {
        PlayerInfo playerInfo=playerInfoDao.getPlayerInfo(req.getPid());
        playerInfo.setTitle(req.getTitle());
        playerInfoDao.saveBean(playerInfo);
        IdReq idReq = new IdReq();
        idReq.setId(req.getPid());
        return getPlayerInfo(null,idReq);
    }

    @RpcAnnotation(cmd="player.birth.update",lock = true,req=PlayerBirthReq.class,name="更新生日信息")
    public PlayerInfoVO updatePlayerBirth(Session session,PlayerBirthReq req) throws AlertException  {
        PlayerInfo playerInfo=playerInfoDao.getPlayerInfo(req.getPid());
        playerInfo.setBirth(new Date(req.getBirth()));
        playerInfoDao.saveBean(playerInfo);
        IdReq idReq = new IdReq();
        idReq.setId(req.getPid());
        return getPlayerInfo(null,idReq);
    }

    @RpcAnnotation(cmd="player.signature.update",lock = true,req=PlayerSignatureReq.class,name="更新签名信息")
    public PlayerInfoVO updatePlayerSignature(Session session,PlayerSignatureReq req) throws AlertException  {
        PlayerInfo playerInfo=playerInfoDao.getPlayerInfo(req.getPid());
        playerInfo.setSignature(req.getSignature());
        playerInfoDao.saveBean(playerInfo);
        IdReq idReq = new IdReq();
        idReq.setId(req.getPid());
        return getPlayerInfo(null,idReq);
    }
}
