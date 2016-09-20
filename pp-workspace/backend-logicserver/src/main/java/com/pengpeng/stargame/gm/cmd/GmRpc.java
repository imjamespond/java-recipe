
package com.pengpeng.stargame.gm.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.gameevent.cmd.EventRpc;
import com.pengpeng.stargame.gm.dao.IAddGoldDao;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.gm.AddGoldInfo;
import com.pengpeng.stargame.model.gm.OnlineInfo;
import com.pengpeng.stargame.model.lottery.Lottery;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.RechargeLog;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IRechargeDao;
import com.pengpeng.stargame.req.BaseReq;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.gm.*;
import com.pengpeng.stargame.vo.role.ChargeReq;
import com.pengpeng.stargame.vo.role.PlayerReq;
import com.pengpeng.stargame.vo.role.TimeReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午3:59
 */
@Component
public class GmRpc extends RpcHandler {
    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;

    @Autowired
    private StatusRemote statusService;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IAddGoldDao addGoldDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IRechargeDao rechargeDao;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private EventRpc eventRpc;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyCollectRuleContainer familyCollectRuleContainer;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    private static final Logger logger = Logger.getLogger("infolog");

    @RpcAnnotation(cmd = "gm.p.freeze", lock = false, req = TimeReq.class, name = "封号")
    public void freeze(Session session, TimeReq req) {
        String pid = playerDao.getPid(req.getUid());
        OtherPlayer op = otherPlayerDao.getBean(pid);
        op.setFreezeTime(req.getTime());
        otherPlayerDao.saveBean(op);
    }

    @RpcAnnotation(cmd = "gm.p.speak", lock = false, req = TimeReq.class, name = "禁言")
    public void speak(Session session, TimeReq req) {
        String pid = playerDao.getPid(req.getUid());
        OtherPlayer op = otherPlayerDao.getBean(pid);
        op.setSpeakTime(req.getTime());
        otherPlayerDao.saveBean(op);
    }
    @RpcAnnotation(cmd = "gm.p.farmExpAdd", lock = false, req = CoinReq.class, name = "增加游戏币达人币")
    public void farmExpAdd(Session session, CoinReq req) {
        Player p = playerDao.getBean(req.getPid());
        if (req.getAmount() == 0) {
            return;
        }
        if (req.getType() == 3) {
            if (req.getAmount() > 0) {
                FarmPlayer farmPlayer  = farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
                farmLevelRuleContainer.addFarmExp(farmPlayer,req.getAmount());
                farmPlayerDao.saveBean(farmPlayer);
            }
        }
    }


        @RpcAnnotation(cmd = "gm.p.coin", lock = false, req = CoinReq.class, name = "增加游戏币达人币")
    public void coin(Session session, CoinReq req) {
        Player p = playerDao.getBean(req.getPid());
        if (req.getAmount() == 0) {
            return;
        }
        if (req.getType() == 1) {
            if (req.getAmount() < 0) {
                p.decGameCoin(Math.abs(req.getAmount()));
            } else {
                p.incGameCoin(req.getAmount());
            }
        } else if (req.getType() == 2) {
            if (req.getAmount() < 0) {
                p.decGoldCoin(Math.abs(req.getAmount()));
            } else {
                p.incGoldCoin(req.getAmount());
            }
        }
        playerDao.saveBean(p);
    }

    @RpcAnnotation(cmd = "gm.p.item", lock = false, req = ItemReq.class, name = "添加道具")
    public void item(Session session, ItemReq req) {
        //道具派送
        FarmPackage farmPackage = farmPackageDao.getBean(session.getPid());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(session.getPid());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(session.getPid());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(session.getPid());
        baseItemRulecontainer.addGoodsNoSave(req.getItemId(), req.getNum(), farmPackage, roomPackege, fashionCupboard,farmDecoratePkg);
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
    }

    @RpcAnnotation(cmd = "gm.rule.refresh", lock = false, req = void.class, name = "刷新规则数据", vo = void.class)
    public void ruleRefresh(Session session, ItemReq req) {
        Map<String, IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
        for (IRuleLoader loader : loaders.values()) {
            loader.load();
        }
    }


    @RpcAnnotation(cmd = "gm.p.message", lock = false, req = MsgReq.class, name = "发送消息")
    public void message(Session session, MsgReq req) {
        //全服广播
        if (req.getType() == MsgReq.ALL) {
            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            ChatVO svo = rspRoleFactory.newShoutChat("", "", req.getMsg());
            frontendServiceProxy.broadcast(sessions, svo);
        }
        //玩家广播
        if (req.getType() == MsgReq.SINGLE) {
            Session session1=statusService.getSession(session,req.getId());
            frontendServiceProxy.broadcast(session1,rspRoleFactory.newFamilyChat("", "", req.getMsg()));
        }
        //家族广播
        if (req.getType() == MsgReq.FAMILY) {
            String channelId = FamilyConstant.getChannelId(req.getId());
            Session[] sessions = statusService.getMember(session, channelId);
            ChatVO cvo = rspRoleFactory.newFamilyChat("", "", req.getMsg());
            frontendServiceProxy.broadcast(sessions, cvo);
        }
    }
    @RpcAnnotation(cmd = "gm.p.finishAllNew", lock = false, req = IdReq.class, name = "跳过所有新手任务")
    public void finishAllNew(Session session,IdReq req){

        taskRuleContainer.finishAllNewTask(req.getId());
    }

    @RpcAnnotation(cmd = "gm.p.finishNew", lock = false, req = IdReq.class, name = "完成玩家当前的新手任务")
    public void finishNew(Session session,IdReq req){
        taskRuleContainer.finishCurrentNewTask(req.getId());
    }

    @RpcAnnotation(cmd = "gm.p.addGoldInfo", lock = false, req = AddGoldReq.class, name = "获取每天添加达人币的玩家Id",vo=AddGoldVO.class)
    public AddGoldVO getAddGoldInfo(Session session,AddGoldReq req){
        AddGoldInfo addGoldInfo=addGoldDao.getAddGoldInfo();
        return getAddGoldVO(addGoldInfo);
    }
    @RpcAnnotation(cmd = "gm.p.saveAddGold", lock = false, req = AddGoldReq.class, name = "保存每天添加达人币的玩家Id")
    public AddGoldVO saveAddGold(Session session,AddGoldReq req){

        AddGoldInfo addGoldInfo=addGoldDao.getAddGoldInfo();
        List<String> list=new ArrayList<String>();
        if(req.getUids()!=null){
            for(String uid:req.getUids()){
                list.add(uid);
            }
        }
        addGoldInfo.setuIds(list);
        if(req.getGold()!=0){
            addGoldInfo.setGold(req.getGold());
        }
        addGoldDao.saveBean(addGoldInfo);

        return getAddGoldVO(addGoldInfo);
    }
    @RpcAnnotation(cmd="gm.p.chargeInfo",lock = true,req=ChargeReq.class,name="充值查询",vo=List.class)
    public  RechargeLog[] chargeInfo(Session session,ChargeReq req) throws AlertException {
        List<RechargeLog> rechargeLogList=new ArrayList<RechargeLog>();
//        int d= DateUtil.getDayOfYear(req.getEndTime())-DateUtil.getDayOfYear(req.getStarTime());
        long d= (req.getEndTime().getTime()-req.getStarTime().getTime())/(1000*60*60*24);
        Date now=new Date();
        for(int i=0;i<=d;i++){
            Date tempDate=DateUtil.addMinute(req.getStarTime(),i*24*60);
            String dateKey="";
            if(req.getType()==1){
                dateKey =DateUtil.getDateFormat(tempDate, "yyyyMMdd");
            }
            if(req.getType()==2){
                dateKey = "vip."+DateUtil.getDateFormat(tempDate, "yyyyMMdd");
            }
            for(RechargeLog rechargeLog: rechargeDao.findAll(dateKey).values()){
                rechargeLogList.add(rechargeLog);
            }

        }
        if(req.getUid()!=0){
            String pid = playerDao.getPid(req.getUid());
            Player player = playerDao.getBean(pid);
            if (player==null){
                exceptionFactory.throwAlertException("p.notplayer");
            }
            List<RechargeLog>  uList=new ArrayList<RechargeLog>();
            for(RechargeLog rechargeLog:rechargeLogList){
                if(rechargeLog.getPid().equals(pid)){
                    uList.add(rechargeLog);
                }
            }
            return uList.toArray(new RechargeLog[0]);
        }

        return rechargeLogList.toArray(new RechargeLog[0]);

    }
    @RpcAnnotation(cmd="gm.addVip",lock = true,req=ChargeReq.class,name="Gm添加VIP")
    public void addVip(Session session,ChargeReq req) throws AlertException {
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
        payMemberRuleContainer.addVipTime(pid,req.getAmount()*30*24);
    }

    @RpcAnnotation(cmd="gm.executeGame",lock = true,req=ChargeReq.class,name="Gm调用")
    public void executeGame(Session session,BaseReq req) throws AlertException {
        try {

            //明星广场收集作物活动
            familyCollectRuleContainer.refresh();
            //广场定时掉落
            eventRpc.plazaDrop(null,null);
            logger.info("success GM");
        } catch (GameException e) {
            logger.info(e.getMessage());
            logger.info("error GM");
        }
    }

    @RpcAnnotation(cmd="gm.videoFamilyInfo",lock = false,req=VideoReq.class,name="查询以家族为单位的 道具信息",vo = VideoFamilyInfoVO.class)
    public VideoFamilyInfoVO videoFamilyNum(Session session,VideoReq req) throws AlertException {

        int size=req.getSize();

        List<FamilyMemberInfo> memberInfos = familyDao.getMemberInfoList(req.getfId(), req.getPageNo(), req.getSize());

        List<Map<String,String>> values=new ArrayList<Map<String, String>>();
        for (int i = 0; i < memberInfos.size(); i++) {
            FamilyMemberInfo mi = memberInfos.get(i);
            if (mi == null) {
                continue;
            }
            Player player = playerDao.getBean(mi.getPid());
            Map<String,String> one=new HashMap<String, String>();
            one.put("uid",String.valueOf(player.getUserId()));
            one.put("name",player.getNickName());
            one.put("num",String.valueOf(baseItemRulecontainer.getGoodsNum(mi.getPid(),req.getItemId())));
            values.add(one);
        }

        int max = (int) familyDao.getMemberSize(req.getfId());

        VideoFamilyInfoVO videoFamilyInfoVO=new VideoFamilyInfoVO();
        videoFamilyInfoVO.setMaxPage(max);
        videoFamilyInfoVO.setPageNo(req.getPageNo());
        videoFamilyInfoVO.setValues(values);
        return videoFamilyInfoVO;
    }


    private AddGoldVO getAddGoldVO(AddGoldInfo addGoldInfo){
        AddGoldVO addGoldVO=new AddGoldVO();
        addGoldVO.setGold(addGoldInfo.getGold());
        addGoldVO.setUids(addGoldInfo.getuIds().toArray(new String[0]));
        return addGoldVO;
    }


}