package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.PiazzaBuilder;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.*;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFamilyFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.*;
import com.pengpeng.stargame.vo.role.PlayerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 家族管理
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:52
 */
@Component
public class FamilyManagerRpc extends RpcHandler {
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @Autowired
    private RspFamilyFactory rspFamilyFactory;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private RspRoleFactory rspRoleFactory;

    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private ISceneRuleContainer sceneRuleContainer;

    @Autowired
    private MessageSource message;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    PiazzaBuilder piazzaBuilder;

    @Autowired
    private IFamilyBuildDao familyBuildDao;

    @Autowired
    private IFamilyMemberDao familyMemberDao;


    @Autowired
    private IOtherPlayerDao otherPlayerDao;
    @Autowired
    private IFamilyBankDao familyBankDao;
    @Autowired
    private IStatistics statistics;

    @Autowired
    private IFamilyBuildingRuleContainer familyBuildingRuleContainer;
    @RpcAnnotation(cmd = "family.get.member", req = FamilyReq.class, vo = FamilyMemberVO.class, name = "取得家族成员信息")
    public FamilyMemberVO getMembers(Session session, FamilyReq req) throws AlertException {
        if(req.getFamilyId()==null){
            return null;
        }
        Player player = playerDao.getBean(session.getPid());
        FamilyMemberInfo memberInfo = familyDao.getMemberInfo(req.getFamilyId(), req.getPid());
        if(memberInfo.getFamilyId()==null||memberInfo.getFamilyId().equals("")){
            return null;
        }
        FamilyBuildInfo bi = familyBuildDao.getBean(req.getFamilyId());
        FamilyMemberVO vo = rspFamilyFactory.newFamilyMemberVO(bi, memberInfo, player);
        return vo;
    }

    @RpcAnnotation(cmd = "family.list.member", req = FamilyReq.class, vo = FamilyMemberPageVO.class, name = "取得家族成员列表")
    public FamilyMemberPageVO listMembers(Session session, FamilyReq req) throws AlertException, RuleException {
        int size = 7;
        Family family = familyDao.getBean(req.getFamilyId());
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        familyRuleContainer.checkMember(family, memberInfo);//是否家族成员
        List<FamilyMemberInfo> memberInfos = familyDao.getMemberInfoList2(family.getId(), session.getPid(), req.getPageNo(), size);
        FamilyMemberVO[] list = new FamilyMemberVO[memberInfos.size()];
        for (int i = 0; i < memberInfos.size(); i++) {
            FamilyMemberInfo mi = memberInfos.get(i);
            if (mi == null) {
                continue;
            }
            Player player = playerDao.getBean(mi.getPid());
            FamilyBuildInfo bi = familyBuildDao.getBean(mi.getFamilyId());
            list[i] = rspFamilyFactory.newFamilyMemberVO(bi, mi, player);
        }
        int max = (int) familyDao.getMemberSize(family.getId());
        if (max % size > 0) {
            max = max / size + 1;
        } else {
            max = max / size;
        }
        FamilyMemberPageVO page = new FamilyMemberPageVO();
        page.setFamilyMemberVOs(list);
        page.setPageNo(req.getPageNo());
        page.setMaxPage(max);
        return page;
    }

//    @Deprecated
//    @RpcAnnotation(TaskRpc = "family.add.member", req = FamilyReq.class,vo= FamilyPanelVO.class, name = "增加家族成员")
//    public FamilyPanelVO addMember(Session session,FamilyReq req) throws AlertException{
//        Player player = playerDao.getBean(session.getPid());
//        Family family = familyDao.getBean(req.getFamilyId());
//        FamilyMemberInfo memberInfo = memberInfoDao.getBean(req.getPid());
//        familyRuleContainer.canAddFamily(family, memberInfo);
//        familyRuleContainer.addFamilyMember(family, memberInfo);
//        familyDao.saveBean(family);
//        memberInfoDao.saveBean(memberInfo);
//        return rspFamilyFactory.newFamilyPanelVO(family, memberInfo,player);
//    }

    @Deprecated
    @RpcAnnotation(cmd = "family.remove.member", req = FamilyReq.class, vo = void.class, name = "退出家族")
    public void removeMember(Session session, FamilyReq req) throws RuleException, AlertException {
        Player player = playerDao.getBean(session.getPid());
        Family family = familyDao.getBeanByStarId(player.getStarId());
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if (memberInfo == null) {
            exceptionFactory.throwTxtException("不是家族成员");
        }
        familyRuleContainer.checkMember(family, memberInfo);//是否家族成员
        familyDao.removeMember(family.getId(), memberInfo.getPid());
        familyMemberInfoDao.deleteBean(memberInfo.getId());
        statusRemote.outerChannel(session, FamilyConstant.getChannelId(family));
    }


    @RpcAnnotation(cmd = "family.join", lock = true, req = FamilyReq.class, name = "加入家族", vo = FamilyInfoVO.class)
    public FamilyInfoVO join(Session session, FamilyReq req) throws AlertException, RuleException {
        Player player = playerDao.getBean(session.getPid());
        Family family = familyDao.getBeanByStarId(player.getStarId());
        if (family == null) {//如果没有
            family = familyDao.getBeanByStarId(familyRuleContainer.getDefaultStarId());
        }
        //新家族成员家族id必须是null,加入成功后自动为家族的id
        FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(session.getPid());
        familyRuleContainer.canAddFamily(family, info);
        familyRuleContainer.joinMember(family, info, player);
        if(!familyMemberDao.contains(info.getFamilyId(),info.getId())){
            familyMemberDao.addBean(info.getFamilyId(), info.getId(), info.getIdentity());
        }
        familyMemberInfoDao.saveBean(info);
        playerDao.saveBean(player);
        //同步session数据
        Map<String, String> map = SessionUtil.newUpdateParam(session, SessionUtil.KEY_CHANNEL_FAMILY, family.getId());
        frontendService.onSessionEvent(session, map);
        statusRemote.enterChannel(session, FamilyConstant.getChannelId(family));
        //同步session数据
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        String content = message.getMessage("family.welcome", new String[]{player.getNickName()}, Locale.CHINA);
        PlayerVO playerVO = rspRoleFactory.newPlayerVO(player);
        frontendServiceProxy.broadcast(session, playerVO);
        int coin = familyRuleContainer.getWelcomeCoin(family);
        WelcomeFamilyVO vo = new WelcomeFamilyVO(session.getPid(), player.getNickName(), content, coin);
        Session[] sessions = getOnlineMember(session, family, player);
        //Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
        frontendServiceProxy.broadcast(sessions, vo);

        //新成员加入家族时在家族频道公告
        String channelId = FamilyConstant.getChannelId(family);
        Session[] fsessions = statusRemote.getMember(session, channelId);
        String wcontent = message.getMessage("family.welcome2", new String[]{player.getNickName()}, Locale.CHINA);
        ChatVO cvo = rspRoleFactory.newFamilyChat("", "", wcontent);
        frontendServiceProxy.broadcast(fsessions, cvo);

        //日志
        String value = "" + GameLogger.SPLIT + req.getFamilyId();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_23, player.getId(), value));
        return rspFamilyFactory.newFamilyInfoVO(family, bi, info);
    }

    private Session[] getOnlineMember(Session s, Family family, Player player) {
        //TODO:这个方法执行的效率是非常低的,希望需求能够改变
//        String channelId = sceneRuleContainer.getChannelScene(s.getScene(),player);
        //取得所有的成员
        long size = familyDao.getMemberSize(family.getId());
        Set<String> list = familyDao.getMembers(family.getId(), 1, (int) size);
        List<String> members = new ArrayList<String>(list);
        List<Session> online = new ArrayList<Session>();
        //取得在线成员
        for (String id : members) {
            if (id.equalsIgnoreCase(player.getId())) {//如果是自己则排除
                continue;
            }
            Session ss = statusRemote.getSession(null, id);
            if (ss != null) {
                online.add(ss);
            }
        }

        //从在线成员中随机找5个
        Session[] sessions = randomSession(online, online.size());
        return sessions;
    }

    /**
     * 随机取得家族里在线的用户,来欢迎新成员
     *
     * @param sessions
     * @param cnt
     * @return
     */
    private Session[] randomSession(List<Session> sessions, int cnt) {
        if (sessions.size() <= cnt) {
            return sessions.toArray(new Session[0]);
        }
        Random rnd = new Random();
        Session[] s = new Session[cnt];
        Set<String> sets = new HashSet<String>();//排重用
        for (int i = 0; i < cnt - 1 && i < sessions.size(); i++) {
            int idx = rnd.nextInt(sessions.size());
            if (sessions.get(idx) == null || sets.contains(sessions.get(idx).getPid())) {
                i--;
                continue;
            }
            sets.add(sessions.get(idx).getPid());
            s[i] = sessions.get(idx);
        }
        sets.clear();
        return s;
    }

    @RpcAnnotation(cmd = "family.welcome", lock = true, req = FamilyReq.class, name = "欢迎家族成员", vo = void.class)
    public void welcome(Session session, FamilyReq req) throws AlertException, RuleException {
        //聊天拦,欢迎词
        //达人币欢迎
        Player toPlayer = playerDao.getBean(req.getPid());
        if (req.getAlterType() == 6) {
            //免费欢迎
            Player myPlayer = playerDao.getBean(session.getPid());
            Family family = familyDao.getBeanByStarId(myPlayer.getStarId());
            String channelId = FamilyConstant.getChannelId(family);
            Session[] sessions = statusRemote.getMember(session, channelId);
            String txt = "family.welcome.free" + (new Random().nextInt(3) + 1);
            String msg = message.getMessage(txt, new String[]{toPlayer.getNickName()}, Locale.CHINA);
            ChatVO voChat = new ChatVO("fans", myPlayer.getId(), myPlayer.getNickName(), msg);
            frontendServiceProxy.broadcast(sessions, voChat);
        } else if (req.getAlterType() == 7) {
            //游戏币欢迎
            Player myPlayer = playerDao.getBean(session.getPid());
            Family family = familyDao.getBeanByStarId(myPlayer.getStarId());
            int coin = familyRuleContainer.welcome(family, myPlayer, toPlayer);
            playerDao.saveBean(myPlayer);
            playerDao.saveBean(toPlayer);
            PlayerVO vo = rspRoleFactory.newPlayerVO(myPlayer);
            frontendServiceProxy.broadcast(session, vo);

            Session toSession = statusRemote.getSession(null, toPlayer.getId());
            if (toSession != null) {
                PlayerVO toVO = rspRoleFactory.newPlayerVO(toPlayer);
                frontendServiceProxy.broadcast(toSession, toVO);
            }
            String channelId = FamilyConstant.getChannelId(family);
            Session[] sessions = statusRemote.getMember(session, channelId);
            String msg = message.getMessage("family.welcome.coin", new String[]{myPlayer.getNickName(), String.valueOf(coin), toPlayer.getNickName()}, Locale.CHINA);
            ChatVO voChat = new ChatVO("fans", msg);
            frontendServiceProxy.broadcast(sessions, voChat);
        }
    }

    @RpcAnnotation(cmd = "family.change", lock = true, req = FamilyReq.class, name = "变更家族", vo = FamilyInfoVO.class)
    public FamilyInfoVO changeFamily(Session session, FamilyReq req) throws AlertException, RuleException {
        OtherPlayer op = otherPlayerDao.getBean(session.getPid());
        Player player = playerDao.getBean(session.getPid());
        Family newFamily = familyDao.getBean(req.getFamilyId());//取得新家族
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getBean(session.getPid());//我的家族成员信息
        Family oldFamily = familyDao.getBean(memberInfo.getFamilyId());
        FamilyBank familyBank=familyBankDao.getFamilyBank(oldFamily.getId());
        familyRuleContainer.checkChangeFamily(oldFamily, newFamily, memberInfo, player);
        familyRuleContainer.changeFamily(oldFamily, newFamily, memberInfo, player,familyBank);
        playerDao.saveBean(player);
        familyDao.saveBean(newFamily);
        familyDao.saveBean(oldFamily);
        familyMemberInfoDao.saveBean(memberInfo);
        otherPlayerDao.saveBean(op);
        Map<String, String> map = SessionUtil.newUpdateParam(session, SessionUtil.KEY_CHANNEL_FAMILY, newFamily.getId());
        frontendService.onSessionEvent(session, map);
        //退出原来的家族频道,加入新的家族频道
        statusRemote.outerChannel(session, FamilyConstant.getChannelId(oldFamily));
        statusRemote.enterChannel(session, FamilyConstant.getChannelId(newFamily));
        //推送欢迎词
        String content = message.getMessage("family.welcome", new String[]{player.getNickName()}, Locale.CHINA);
        int coin = familyRuleContainer.getWelcomeCoin(newFamily);
        PlayerVO playerVO = rspRoleFactory.newPlayerVO(player);
        frontendServiceProxy.broadcast(session, playerVO);
        WelcomeFamilyVO welcomeFamilyVO = new WelcomeFamilyVO(session.getPid(), player.getNickName(), content, coin);
        Session[] sessions = getOnlineMember(session, newFamily, player);
        frontendServiceProxy.broadcast(sessions, welcomeFamilyVO);

        FamilyBuildInfo bi = familyBuildDao.getBean(newFamily.getId());
        FamilyInfoVO vo = rspFamilyFactory.newFamilyInfoVO(newFamily, bi, memberInfo);

        //日志
        String value = oldFamily.getId() + GameLogger.SPLIT + req.getFamilyId();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_23, player.getId(), value));
        statistics.recordChangeFamily(player.getId(),player);
        return vo;
    }


    @RpcAnnotation(cmd = "family.endow", lock = true, req = FamilyReq.class, name = "捐献经费", vo = RewardVO.class)
    public RewardVO endow(Session session, FamilyReq req) throws AlertException, RuleException {
        int money = req.getNum();
        Player player = playerDao.getBean(session.getPid());
        FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(info.getFamilyId());
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        familyRuleContainer.checkMember(family, info);
        familyRuleContainer.checkEndow(family, info, bi, player, money, req.getAlterType());
        RewardVO rewardVO = familyRuleContainer.endow(family, info, player, money, req.getAlterType(),bi);
        familyDao.saveBean(family);
        familyMemberInfoDao.saveBean(info);
        playerDao.saveBean(player);

        PlayerVO vo = rspRoleFactory.newPlayerVO(player);
        frontendServiceProxy.broadcast(session, vo);

        int level = bi.getLevel(FamilyConstant.BUILD_BANK);
        FamilyBuildingRule br = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK,level);
        if (family.getFunds()>=br.getMaxFunds()){
            exceptionFactory.throwRuleException("family.endow.max");
        }
        return rewardVO;
    }

    @RpcAnnotation(cmd = "family.get.devote", req = FamilyReq.class, name = "取得家族贡献比例", vo = FamilyDevoteVO.class)
    public FamilyDevoteVO getDevoteInfo(Session session, FamilyReq req) throws AlertException, RuleException {
        FamilyRule rule = familyRuleContainer.getElement(req.getFamilyId());
        FamilyDevoteVO vo = rspFamilyFactory.newFamilyDevoteVO(rule);
        return vo;
    }

    @RpcAnnotation(cmd = "family.receive.boon", req = FamilyReq.class, name = "领取家族福利", vo = Integer.class)
    public int getBoon(Session session, FamilyReq req) throws RuleException, AlertException {
        Player player = playerDao.getBean(session.getPid());
        Family family = familyDao.getBean(req.getFamilyId());
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(player.getId());
        familyRuleContainer.checkBoon(family, bi, info, player);
        int boon = familyRuleContainer.receiveBoon(bi, info, player);
        playerDao.saveBean(player);
        familyMemberInfoDao.saveBean(info);
        PlayerVO vo = rspRoleFactory.newPlayerVO(player);
        frontendServiceProxy.broadcast(session, vo);

        return boon;
    }


    @RpcAnnotation(cmd = "family.change.identity", req = FamilyReq.class, vo = FamilyMemberVO.class, name = "变更家族成员身份")
    public FamilyMemberVO changeIdentity(Session session, FamilyReq req) throws AlertException, RuleException {
        Player player = playerDao.getBean(session.getPid());
        Family family = familyDao.getBeanByStarId(player.getStarId());
        FamilyMemberInfo memberInfo = familyDao.getMemberInfo(family.getId(), player.getId());

        if (req.getAlterType() == 10) {
            familyRuleContainer.checkMember(family, memberInfo);
            memberInfo.setIdentity(req.getMemberType());
            familyMemberDao.removeBean(memberInfo.getFamilyId(), memberInfo.getId());
            familyMemberDao.addBean(memberInfo.getFamilyId(), memberInfo.getId(), memberInfo.getIdentity());
            familyMemberInfoDao.saveBean(memberInfo);
        }
        FamilyBuildInfo bi = familyBuildDao.getBean(req.getFamilyId());
        FamilyMemberVO vo = rspFamilyFactory.newFamilyMemberVO(bi, memberInfo, player);
        return vo;
    }
}
