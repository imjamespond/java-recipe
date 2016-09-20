package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.*;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IMoneyTreeDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.pengpeng.stargame.player.cmd.PlayerRpc;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspMoneyTreeFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 摇钱树
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:51
 */
@Component
public class MoneyTreeRpc extends RpcHandler {
    @Autowired
    private GameLoggerWrite gameLoggerWrite;

    @Autowired
    private IMoneyTreeRuleContainer moneyTreeRuleContainer;
    @Autowired
    private RspMoneyTreeFactory moneyTreeFactory;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFamilyBuildDao familyBuildDao;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private PlayerRpc playerRpc;
    @Autowired
    private IMoneyTreeDao moneyTreeDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;

    @Autowired
    private MessageSource message;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;


    public MoneyTreeVO getMoneyTreeVO(String familyId, String pid) {
        MoneyTree moneyTree = moneyTreeDao.getMoneyTree(familyId, new Date());
        FamilyBuildInfo info = familyBuildDao.getBean(familyId);
        MoneyTreeRule moneyTreeRule = moneyTreeRuleContainer.getElement(info.getLevel(FamilyConstant.BUILD_TREE));
        return moneyTreeFactory.getMoneyTreeVO(pid, moneyTree, moneyTreeRule);
    }

    @RpcAnnotation(cmd = "moneytree.getinfo", req = MoneyTreeReq.class, name = "获取摇钱树信息", vo = MoneyTreeVO.class)
    public MoneyTreeVO getinfo(Session session, MoneyTreeReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return null;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);

        return getMoneyTreeVO(familyMemberInfo.getFamilyId(), session.getPid());
    }

    @RpcAnnotation(cmd = "moneytree.pickMoney", req = MoneyTreeReq.class, name = "捡取地面上的钱", vo = MoneyPickInfoVO.class)
    public MoneyPickInfoVO pickMoney(Session session, MoneyTreeReq req) throws AlertException, RuleException {

        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);

        MoneyTree moneyTree = moneyTreeDao.getMoneyTree(familyMemberInfo.getFamilyId(), new Date());
        Player player = playerDao.getBean(session.getPid());
        MoneyPick moneyPick = moneyTreeRuleContainer.pickMoney(player, moneyTree, req.getPickMoneyId());

        moneyTreeDao.saveBean(moneyTree);
        playerDao.saveBean(player);

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions, moneyTreeFactory.getMoneyDropVO(moneyTree));

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));

        return moneyTreeFactory.getMoneyPickInfoVO(moneyPick);


    }

    @RpcAnnotation(cmd = "moneytree.blessing", req = MoneyTreeReq.class, name = "祝福", vo = void.class)
    public void blessing(Session session, MoneyTreeReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);

        FamilyBuildInfo info = familyBuildDao.getBean(familyMemberInfo.getFamilyId());
        MoneyTree moneyTree = moneyTreeDao.getMoneyTree(familyMemberInfo.getFamilyId(), new Date());
        Player player = playerDao.getBean(session.getPid());
        moneyTreeRuleContainer.checkBlessing(player, moneyTree, info.getLevel(FamilyConstant.BUILD_TREE));
        moneyTreeRuleContainer.blessing(familyMemberInfo, player, moneyTree, info.getLevel(FamilyConstant.BUILD_TREE));

        moneyTreeDao.saveBean(moneyTree);
        playerDao.saveBean(player);
        familyMemberInfoDao.saveBean(familyMemberInfo);


        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_15,"",1);


        frontendService.broadcast(new Session[]{session}, getMoneyTreeVO(familyMemberInfo.getFamilyId(), session.getPid()));


        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));

        ChatVO vo = rspRoleFactory.newFamilyChat("", player.getNickName(), message.getMessage("family.moneyTree.be", new String[]{player.getNickName()}, Locale.CHINA));
        Session[] sessions = statusService.getMember(session, familyMemberInfo.getFamilyId());
        frontendService.broadcast(sessions, vo);

         //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_19, session.getPid(), ""));

    }

    @RpcAnnotation(cmd = "moneytree.rock", req = MoneyTreeReq.class, name = "摇钱", vo = void.class)
    public void rock(Session session, MoneyTreeReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);

        FamilyBuildInfo info = familyBuildDao.getBean(familyMemberInfo.getFamilyId());
        MoneyTree moneyTree = moneyTreeDao.getMoneyTree(familyMemberInfo.getFamilyId(), new Date());
        Player player = playerDao.getBean(session.getPid());
        moneyTreeRuleContainer.checkRock(session.getPid(), moneyTree,familyMemberInfo);
        boolean rockMoney = moneyTreeRuleContainer.rock(player, moneyTree, info.getLevel(FamilyConstant.BUILD_TREE),familyMemberInfo);

        playerDao.saveBean(player);
        moneyTreeDao.saveBean(moneyTree);

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions, moneyTreeFactory.getMoneyDropVO(moneyTree));

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));

        if (rockMoney) {
            ChatVO vo = rspRoleFactory.newFamilyChat("", player.getNickName(), message.getMessage("family.moneyTree.rockMoney", new String[]{player.getNickName()}, Locale.CHINA));
            Session[] sessions1 = statusService.getMember(session, familyMemberInfo.getFamilyId());
            frontendService.broadcast(sessions1, vo);
        }

    }

    @RpcAnnotation(cmd = "moneytree.getFamilyIds", req = MoneyTreeReq.class, name = "获取所有家族Id列表", vo = String[].class)
    public String[] getFamilyIds(Session session, MoneyTreeReq req) throws AlertException, RuleException {
        List<FamilyRule> familyRuleList = familyRuleContainer.getAll();
        String[] ids = new String[familyRuleList.size()];
        int i = 0;
        for (FamilyRule familyRule : familyRuleList) {
            ids[i] = familyRule.getId();
            i++;
        }

        return ids;
    }

    @RpcAnnotation(cmd = "moneytree.getMoneyTreeHint", req = MoneyTreeReq.class, name = "获取摇钱树的提示信息", vo = MoneyTreeHintVO.class)
    public MoneyTreeHintVO  getMoneyTreeHint(Session session, MoneyTreeReq req) throws AlertException, RuleException {
        MoneyTreeHintVO moneyTreeHintVO=new MoneyTreeHintVO();
        MoneyTree moneyTree = moneyTreeDao.getMoneyTree(req.getFamilyId(), new Date());
        Date now=new Date();
        Date date1= DateUtil.addMinute(moneyTree.getRipeTime(),-5);
        Date date2=moneyTree.getRipeTime();
        Date date3=moneyTree.getRipeEndTime();

        moneyTreeHintVO.setFamilyId(req.getFamilyId());

        if(now.before(date1)){
            moneyTreeHintVO.setDate(date1);
            moneyTreeHintVO.setContent(message.getMessage("family.moneyTree.hint1",null,Locale.CHINA));
        }
        if(now.after(date1)&&now.before(date2)){

            moneyTreeHintVO.setDate(date2);
            moneyTreeHintVO.setContent(message.getMessage("family.moneyTree.hint2",null,Locale.CHINA));
        }
        if(now.after(date2)&&now.before(date3)){

            moneyTreeHintVO.setDate(date3);
            moneyTreeHintVO.setContent(message.getMessage("family.moneyTree.hint3",null,Locale.CHINA));

        }

        return moneyTreeHintVO;
    }


}
