package com.pengpeng.stargame.api.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.dao.IFarmRankDao;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.RechargeLog;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IRechargeDao;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomRankDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vo.api.*;
import com.pengpeng.stargame.vo.piazza.StarGiftReq;
import com.pengpeng.stargame.vo.role.ChargeReq;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 13-8-15
 * Time: 下午2:06
 */
@Component()
public class ApiRpc extends RpcHandler{
    @Autowired
    private IRoomRankDao roomRankDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IRoomPlayerDao roomPlayerDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;

    @Autowired
    private MessageSource message;
    @Autowired
    private IFarmRankDao farmRankDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;


    @Autowired
    private IRechargeDao rechargeDao;

    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IVipDao vipDao;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private IStatistics statistics;

    private static String URL="http://istar.pengpeng.com/game/res/icon/starPortrait/%s.png";


    @RpcAnnotation(cmd = "family.fansValue.top", req = StarGiftReq.class, name = "明星粉丝值排行",vo= FamilyRankVO[].class)
    public FamilyRankVO[]  familyTop(Session session, StarGiftReq req) throws AlertException, RuleException {
        /**
         *
         */
        List<Family> familyList=familyDao.getRank();
        List<Family> familyListTop=familyList.subList(0,10);
        List<FamilyRankVO> familyRankVOList=new ArrayList<FamilyRankVO>();

        for(int i=0;i<familyListTop.size();i++){
            Family family=   familyListTop.get(i);
            FamilyRule familyRule=familyRuleContainer.getElement(family.getId())  ;
            FamilyRankVO familyRankVO=new FamilyRankVO();
            familyRankVO.setRank(i+1);
            familyRankVO.setStarId(familyRule.getStarId());
            familyRankVO.setFansValue(family.getFansValue());
            familyRankVO.setStarName(familyRule.getStarName());
            familyRankVO.setIconUrl(String.format(URL,familyRule.getStarIcon()));
            familyRankVOList.add(familyRankVO);


        }
        return familyRankVOList.toArray(new FamilyRankVO[0]);
    }



    @RpcAnnotation(cmd = "room.top", req = RoomIdReq.class, name = "获取房间豪华度排行",vo=RoomRankVO[].class)
    public RoomRankVO[] roomtop(Session session, RoomIdReq req) throws AlertException {
        List<RoomRankVO> roomRankVOList=new ArrayList<RoomRankVO>();

        Set<String> pids= roomRankDao.getMembers("", 0, 4);

        int i=0;
        for(String id:pids){
            RoomPlayer roomPlayer=roomPlayerDao.getRoomPlayer(id);
            RoomRankVO roomRankVO=new RoomRankVO();

            FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(id);//我的家族成员信息
            FamilyRule familyRule=familyRuleContainer.getElement(memberInfo.getFamilyId());
            if(familyRule!=null){
                roomRankVO.setStarName(familyRule.getStarName());
                roomRankVO.setStarId(familyRule.getStarId());

            }

            Player player=playerDao.getBean(id);
            i++;

            roomRankVO.setUname(player.getNickName());
            roomRankVO.setLuxuryDegree(roomRankDao.getScore("",id).intValue());
            roomRankVO.setRank(i);
            roomRankVO.setUid(player.getUserId());
            roomRankVOList.add(roomRankVO);
        }

        return roomRankVOList.toArray(new RoomRankVO[0]) ;
    }

    @RpcAnnotation(cmd = "getPlayerInfo", req = ApiReq.class, name = "获取玩家信息",vo=PlayerResult.class)
    public PlayerResult getPlayerInfo(Session session,  ApiReq req) throws AlertException {
        String pid = playerDao.getPid(req.getUid());
        if (pid==null){
            return new PlayerResult(202,message.getMessage("p.notplayer", new String[]{}, Locale.CHINA));
        }
        Player player = playerDao.getBean(pid);
        if (player==null){
            return new PlayerResult(202,message.getMessage("p.notplayer", new String[]{}, Locale.CHINA));
        }
        RoomPlayer roomPlayer=roomPlayerDao.getRoomPlayer(pid);
        PlayerResult playerResult=new PlayerResult();
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(pid);//我的家族成员信息
        FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(pid,System.currentTimeMillis());

        playerResult.setName(player.getNickName());
        playerResult.setGameCoin(player.getGameCoin());
        playerResult.setGoldCoin(player.getGoldCoin());
        playerResult.setLuxuryDegree(roomItemRuleContainer.getLuxuryValue(roomPlayer));
        playerResult.setFarmLevel(farmPlayer.getLevel());
        if(memberInfo.getFamilyId()!=null&&!memberInfo.getFamilyId().equals("")){
            FamilyRule familyRule=familyRuleContainer.getElement(memberInfo.getFamilyId());
            if(null!=familyRule){
                playerResult.setStarName(familyRule.getStarName());
                playerResult.setStarId(familyRule.getStarId());
                if(memberInfo.getIdentity()== FamilyConstant.TYPE_MX){
                    playerResult.setPosition("明星");

                }else if(memberInfo.getIdentity()== FamilyConstant.TYPE_ZL){
                    playerResult.setPosition("明星助理");
                }  else if(memberInfo.getIdentity()== FamilyConstant.TYPE_CJFS){
                    playerResult.setPosition("超级粉丝");
                }   else if(memberInfo.getIdentity()== FamilyConstant.TYPE_FS){
                    playerResult.setPosition("普通粉丝");
                }
            }

        }

        PlayerVip playerVip=vipDao.getPlayerVip(player.getId());
        playerResult.setVip(playerVip.getViP());
        if(playerVip.getViP()==1){
            playerResult.setEndTime(playerVip.getEndTime());
        }


        return playerResult;
    }


    @RpcAnnotation(cmd = "farm.top", req = ApiReq.class, name = "获取农场等级排行",vo=FarmRankVO [].class)
    public FarmRankVO[] farmtop(Session session, RoomIdReq req) throws AlertException {
        List<FarmRankVO> farmRankVOList=new ArrayList<FarmRankVO>();

        Set<String> pids= farmRankDao.getMembers("", 0, 4);

        int i=0;
        for(String id:pids){
            FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(id,System.currentTimeMillis());
            FarmRankVO farmRankVO=new FarmRankVO();

            FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(id);//我的家族成员信息
            FamilyRule familyRule=familyRuleContainer.getElement(memberInfo.getFamilyId());
            if(familyRule!=null){
                farmRankVO.setStarName(familyRule.getStarName());
                farmRankVO.setStarId(familyRule.getStarId());

            }

            Player player=playerDao.getBean(id);
            i++;

            farmRankVO.setUname(player.getNickName());
            farmRankVO.setFarmLevel(farmPlayer.getLevel());
            farmRankVO.setRank(i);
            farmRankVO.setUid(player.getUserId());
            farmRankVO.setExp(farmPlayer.getLevel()*10000000+farmPlayer.getExp());
            farmRankVOList.add(farmRankVO);
        }
        Collections.sort(farmRankVOList, new Comparator<FarmRankVO>() {
            @Override
            public int compare(FarmRankVO o1, FarmRankVO o2) {
                if (o1.getExp() > o2.getExp()) {
                    return -1;
                }
                if (o1.getExp() < o2.getExp()) {
                    return 1;
                }
                return 0;
            }
        });


        return farmRankVOList.toArray(new FarmRankVO[0]) ;
    }

    @RpcAnnotation(cmd="p.charge",lock = true,req=ChargeReq.class,name="充值,加达人币")
    public void charge(Session session,ChargeReq req) throws AlertException {
        String pid = playerDao.getPid(req.getUid());
        if (pid==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        Player player = playerDao.getBean(pid);
        if (player==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        player.incGoldCoin(req.getAmount());

        /**
         * 分析数据统计
         */
        statistics.recordRecharge(player.getId(),player,req.getAmount()/10);

        player.setRechargeTime(new Date());
        playerDao.saveBean(player);
        String dateKey = DateUtil.getDateFormat(new Date(), "yyyyMMdd");
        rechargeDao.insertBean(dateKey,new RechargeLog(pid,req.getAmount(),req.getPo(),player.getUserId(),player.getNickName()));
        /**
         * 充值活动
         */
        Date now=new Date();
        EventRule eventRule=eventRuleContainer.getElement("1");
        if(eventRule!=null){
            if(now.after(DateUtil.convertStringToDate(null,eventRule.getStartTime()))&&now.before(DateUtil.convertStringToDate(null,eventRule.getEndTime()))){
                if(req.getAmount()>=288){
                    payMemberRuleContainer.addVipTime(pid,30*24);
                }
            }
        }
        /**
         * 成就统计 累积充值
         */
        successRuleContainer.updateSuccessNum(pid,15,req.getAmount()/10,"");

        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_3,pid,String.valueOf(req.getAmount())));
    }

    @RpcAnnotation(cmd="p.chargeVip",lock = true,req=ChargeReq.class,name="充值VIP")
    public void chargeVip(Session session,ChargeReq req) throws AlertException {
        String pid = playerDao.getPid(req.getUid());
        if (pid==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        Player player = playerDao.getBean(pid);
        if (player==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        if(req.getAmount()==0){
            return;
        }
        /**
         * 分析数据统计
         */
        statistics.recordRecharge(player.getId(),player,req.getAmount()*10);

        player.setRechargeTime(new Date());
        playerDao.saveBean(player);

        payMemberRuleContainer.addVipTime(pid,req.getAmount()*30*24);
        String dateKey = "vip."+DateUtil.getDateFormat(new Date(), "yyyyMMdd");
        rechargeDao.insertBean(dateKey,new RechargeLog(pid,req.getAmount(),req.getPo(),player.getUserId(),player.getNickName()));

        /**
         * 充值活动
         */
        Date now=new Date();
        EventRule eventRule=eventRuleContainer.getElement("1");
        if(eventRule!=null){
            if(now.after(DateUtil.convertStringToDate(null,eventRule.getStartTime()))&&now.before(DateUtil.convertStringToDate(null,eventRule.getEndTime()))){
                if(req.getAmount()>=12){
                    player.incGoldCoin(88);
                    playerDao.saveBean(player);
                }
            }
        }
//        rechargeDao.insertBean(dateKey,new RechargeLog(pid,req.getAmount(),req.getPo()));
//        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_3,pid,String.valueOf(req.getAmount())));
    }



}
