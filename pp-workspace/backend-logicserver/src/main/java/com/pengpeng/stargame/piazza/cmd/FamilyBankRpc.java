package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.dao.IPlayerEventDao;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.gameEvent.FamilyBankEvent;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBankDao;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFamilyFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.piazza.MoneyTreeReq;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午10:34
 */
@Component
public class FamilyBankRpc extends RpcHandler {
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyBankDao familyBankDao;
    @Autowired
    private IFamilyBuildDao familyBuildDao;
    @Autowired
    private RspFamilyFactory rspFamilyFactory;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private IPlayerEventDao playerEventDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @RpcAnnotation(cmd = "family.bank.info", req = FamilyBankReq.class, name = "家族银行面板信息", vo = FamilyBankVO.class)
    public FamilyBankVO rankInfo(Session session, FamilyBankReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return null;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        Player player=playerDao.getBean(session.getPid());
        FamilyBank familyBank=familyBankDao.getFamilyBank(family.getId());
        FamilyBuildInfo familyBuildInfo=familyBuildDao.getBean(family.getId());
        return rspFamilyFactory.newFamilyBankVO(player,familyBank,familyBuildInfo,family);
    }
    @RpcAnnotation(cmd = "family.bank.get", req = FamilyBankReq.class, name = "取款", vo = FamilyBankVO.class)
    public FamilyBankVO rankGet(Session session, FamilyBankReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return null;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        Player player=playerDao.getBean(session.getPid());
        FamilyBank familyBank=familyBankDao.getFamilyBank(family.getId());
        FamilyBuildInfo familyBuildInfo=familyBuildDao.getBean(family.getId());
        familyRuleContainer.familyBankGet(player,familyBank,familyBuildInfo,req.getGameMoney());
        familyBankDao.saveBean(familyBank);
        playerDao.saveBean(player);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));


        return rspFamilyFactory.newFamilyBankVO(player,familyBank,familyBuildInfo,family);
    }

    @RpcAnnotation(cmd = "family.bank.save", req = FamilyBankReq.class, name = "存款", vo = FamilyBankVO.class)
    public FamilyBankVO rankSave(Session session, FamilyBankReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return null;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        Player player=playerDao.getBean(session.getPid());
        FamilyBank familyBank=familyBankDao.getFamilyBank(family.getId());
        FamilyBuildInfo familyBuildInfo=familyBuildDao.getBean(family.getId());
        familyRuleContainer.familyBankSave(player, familyBank, familyBuildInfo,req.getGameMoney());
        familyBankDao.saveBean(familyBank);
        playerDao.saveBean(player);

        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, rspRoleFactory.newPlayerVO(player));

        /**
         * 存款活动相关
         */
        int gameMoney=familyBank.getMoenyByPid(session.getPid());
        EventRule eventRule=eventRuleContainer.getFamilyBankEventRule();
        PlayerEvent playerEvent=playerEventDao.getPlayerEvent(session.getPid());
        FamilyBankEvent familyBankEvent=playerEvent.getFamilyBankEvent(eventRule.getId());
        for(String reward:eventRule.getFamilyBankRewards()){
            String [] oneReward=reward.split(",");
            if(Integer.parseInt(oneReward[0])<=gameMoney) {
                if(!familyBankEvent.getCanReward().contains(oneReward[0])&&!familyBankEvent.getReewarded().contains(oneReward[0])){
                    familyBankEvent.getCanReward().add(oneReward[0]);
                }
            }
        }
        playerEvent.updateFamilyBankEvent(eventRule.getId(),familyBankEvent);
        playerEventDao.saveBean(playerEvent);
        //图标 通知
        if(familyBankEvent.getCanReward().size()>0){
            frontendService.broadcast(session, new MsgVO(EventConstant.EVENT_F_B,familyBankEvent.getCanReward().size()));
        }

        return rspFamilyFactory.newFamilyBankVO(player,familyBank,familyBuildInfo,family);
    }
    @RpcAnnotation(cmd = "family.bank.playerMoney", req = FamilyBankReq.class, name = "获取玩家存款数量", vo = Integer.class)
    public int getPlayerMoney(Session session, FamilyBankReq req) throws AlertException, RuleException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        if(familyMemberInfo.getFamilyId()==null){
            return 0;
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        Player player=playerDao.getBean(session.getPid());
        FamilyBank familyBank=familyBankDao.getFamilyBank(family.getId());
        return familyBank.getMoenyByPid(session.getPid());
    }
}
