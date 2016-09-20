package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyApplicant;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyAssistantContainer;
import com.pengpeng.stargame.piazza.dao.IApplicationSetDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 家族明星助理
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:52
 */
@Component
public class FamilyAssistantRpc extends RpcHandler {
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFamilyMemberDao familyMemberDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IApplicationSetDao applicationSetDao;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFamilyAssistantContainer familyAssistantContainer;
    private static int STATUS_APPLYING = 0;

    @RpcAnnotation(cmd = "family.assistant.info", req = FamilyAssistantReq.class, name = "助理信息",vo=ApplicationVO.class)
    public FamilyAssistantVO getInfo(Session session, FamilyAssistantReq req) throws AlertException, RuleException {
        String pid = session.getPid();
        FamilyAssistantVO familyAssistantVO = new FamilyAssistantVO();

        FamilyApplicant familyApplicant = new FamilyApplicant();
        familyApplicant.setPid(pid);

        Player player = playerDao.getBean(pid);
        Family family = familyDao.getBeanByStarId(player.getStarId());
        boolean isApplicant = applicationSetDao.isFamilyApplicant(family.getId(),familyApplicant);
        familyAssistantVO.setStatus(isApplicant?FamilyAssistantVO.YES:FamilyAssistantVO.NO);

        Set<String> familyMemberInfoSet = familyMemberDao.getMembersByScore(family.getId(),FamilyConstant.TYPE_ZL,0,2);
        if(familyMemberInfoSet!=null){
            AssistantVO[] assistantVOs = new AssistantVO[familyMemberInfoSet.size()];
            int i = 0;
            for(String id:familyMemberInfoSet){
                Player assistant = playerDao.getBean(id);
                if(null!=assistant) {
                    AssistantVO assistantVO = new AssistantVO();
                    assistantVO.setName(assistant.getNickName());
                    assistantVOs[i] = assistantVO;
                }
                i++;
            }
            familyAssistantVO.setAssistants(assistantVOs);
        }

        return familyAssistantVO;
    }

    @RpcAnnotation(cmd = "family.assistant.apply", req = FamilyAssistantReq.class, name = "申请助理",vo=ApplicationVO.class)
    public ApplicationVO apply(Session session, FamilyAssistantReq req) throws AlertException, RuleException {
        String pid = session.getPid();

        boolean isVip = payMemberRuleContainer.isPayMember(pid);
        FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
        //check
        familyAssistantContainer.check(familyMemberInfo,farmPlayer,isVip);

        FamilyApplicant familyApplicant = new FamilyApplicant();
        familyApplicant.setPid(pid);

        Player player = playerDao.getBean(pid);
        Family family = familyDao.getBeanByStarId(player.getStarId());
        applicationSetDao.addFamilyApplicant(family.getId(),familyApplicant);

        return null;
    }

    @RpcAnnotation(cmd = "family.assistant.approve", req = FamilyAssistantReq.class, name = "批准助理",vo=ApplicationVO.class)
    public ApplicationVO approve(Session session, FamilyAssistantReq req) throws AlertException, RuleException {
        String pid = session.getPid();

        //改变身份
        Player player = playerDao.getBean(pid);
        Family family = familyDao.getBeanByStarId(player.getStarId());
        FamilyMemberInfo memberInfo = familyDao.getMemberInfo(family.getId(), pid);
        memberInfo.setIdentity(FamilyConstant.TYPE_ZL);//明星助理
        familyMemberInfoDao.saveBean(memberInfo);
        familyMemberDao.removeBean(memberInfo.getFamilyId(), memberInfo.getId());
        familyMemberDao.addBean(memberInfo.getFamilyId(), memberInfo.getId(), memberInfo.getIdentity());

        return null;
    }

    @RpcAnnotation(cmd = "family.assistant.stopApply", req = FamilyAssistantReq.class, name = "停止报名",vo=ApplicationVO.class)
    public ApplicationVO stopApply(Session session, FamilyAssistantReq req) throws AlertException, RuleException {

         return null;
    }

    @RpcAnnotation(cmd = "family.assistant.designate", req = FamilyAssistantReq.class, name = "指定助理",vo=ApplicationVO.class)
    public ApplicationVO designate(Session session, FamilyAssistantReq req) throws AlertException, RuleException {

        String channelId = FamilyConstant.getChannelId(req.getFid());
        Session[] sessions = statusService.getMember(session, channelId);

        Family family = familyDao.getBean(req.getFid());
        //对旧助理广播
        Set<String> familyMemberInfoSet = familyMemberDao.getMembersByScore(req.getFid(),FamilyConstant.TYPE_ZL,0,2);
        String assistants = "";
        for(String pid:familyMemberInfoSet){
            Player tp = playerDao.getBean(pid);
            if(null==tp)
                continue;
            assistants += tp.getNickName() + ",";
        }

        String expired = message.getMessage("family.assistant.expired", new String[]{assistants, family.getName()}, Locale.CHINA);
        ChatVO evo = rspRoleFactory.newShoutChat("", "", expired);
        for(String pid:familyMemberInfoSet){
            //先前助理置为粉丝
            FamilyMemberInfo memberInfo = familyDao.getMemberInfo(family.getId(), pid);
            memberInfo.setIdentity(FamilyConstant.TYPE_FS);
            familyMemberInfoDao.saveBean(memberInfo);
            familyMemberDao.removeBean(memberInfo.getFamilyId(), memberInfo.getId());
            familyMemberDao.addBean(memberInfo.getFamilyId(), memberInfo.getId(), memberInfo.getIdentity());

            //广播
            Session playerSession = statusService.getSession(null,pid);
            if(null == playerSession){
                continue;
            }
            frontendService.broadcast(playerSession, evo);

        }

        //机选助理,找出最大的贡献者,找出随机一名申请者
        Set<Applicant> treeSet = new TreeSet<Applicant>();
        List<FamilyApplicant> list = applicationSetDao.getFamilyApplicantSet(req.getFid());
        if(null != list){
            for(FamilyApplicant familyApplicant:list){
                FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(familyApplicant.getPid());
                Applicant applicant = new Applicant();
                applicant.contribution = familyMemberInfo.getWeekContribution();
                applicant.pid = familyApplicant.getPid();
                treeSet.add(applicant);
            }
        }
        Iterator<Applicant> it = treeSet.iterator();
        int size = treeSet.size();
        int random = (int)Math.floor(Math.random()*(size-1))+1;
        int i = 0;
        while(it.hasNext()){
            Applicant applicant = it.next();

            if(i>0) {
                if(i++!=random){
                continue;
                }
            } else {
                i++;
            }

            FamilyMemberInfo memberInfo = familyDao.getMemberInfo(family.getId(), applicant.pid);
            memberInfo.setIdentity(FamilyConstant.TYPE_ZL);//明星助理
            familyMemberInfoDao.saveBean(memberInfo);
            familyMemberDao.removeBean(memberInfo.getFamilyId(), memberInfo.getId());
            familyMemberDao.addBean(memberInfo.getFamilyId(), memberInfo.getId(), memberInfo.getIdentity());
        }



        //对新助理广播
        familyMemberInfoSet = familyMemberDao.getMembersByScore(req.getFid(),FamilyConstant.TYPE_ZL,0,2);
        assistants = "";
        for(String pid:familyMemberInfoSet){
            Player tp = playerDao.getBean(pid);
            if(null==tp)
                continue;
            assistants += tp.getNickName() + ",";
        }
        String promoted = message.getMessage("family.assistant.promoted", new String[]{assistants, family.getName()}, Locale.CHINA);
        ChatVO cvo = rspRoleFactory.newShoutChat("", "", promoted);
        for(String pid:familyMemberInfoSet){
            //广播
            Session playerSession = statusService.getSession(null,pid);
            if(null == playerSession){
                continue;
            }
            frontendService.broadcast(playerSession, cvo);
        }

        //对家族广播
        if(assistants.length()>0){
            String content = message.getMessage("family.assistant.congratulation", new String[]{assistants, family.getName()}, Locale.CHINA);
            ChatVO fvo = rspRoleFactory.newFamilyChat("", "",content);
            frontendService.broadcast(sessions, fvo);
        }

        clean(session,req);
        return null;
    }

    @RpcAnnotation(cmd = "family.assistant.list", req = FamilyAssistantReq.class, name = "申请列表",vo=ApplicationVO.class)
    public ApplicationListVO list(Session session, FamilyAssistantReq req) throws AlertException, RuleException {

        ApplicationListVO appList = new ApplicationListVO();

        List<FamilyApplicant> list = applicationSetDao.getFamilyApplicantSet(req.getFid());
        if(null != list){
            ApplicationVO[] applicationVOs = new ApplicationVO[list.size()];
            int i = 0;
            for(FamilyApplicant fa:list){
                ApplicationVO av = new ApplicationVO();
                av.setPid(fa.getPid());
                applicationVOs[i++] = av;
            }
            appList.setApplicationVOs(applicationVOs);
        }
        return appList;
    }

    @RpcAnnotation(cmd = "family.assistant.clean", req = FamilyAssistantReq.class, name = "清除申请",vo=ApplicationVO.class)
    public ApplicationVO clean(Session session, FamilyAssistantReq req) throws AlertException, RuleException {
        applicationSetDao.cleanApplicant(req.getFid());
        return null;
    }

    private class Applicant implements Comparable<Applicant>{
        public int contribution;
        public String pid;

        @Override
        public int compareTo(Applicant applicant){
            int diff = applicant.contribution - contribution;
            return diff==0?-1:diff;
        }
    }
}
