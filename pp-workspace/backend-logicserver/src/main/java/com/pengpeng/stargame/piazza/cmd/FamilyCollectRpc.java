package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.LockRedis;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyCollectDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyCollectRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspFamilyCollectFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.HintVO;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.BuildVO;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectReq;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.management.StringValueExp;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:03
 */
@Component
public class FamilyCollectRpc extends RpcHandler {
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
    private IFamilyCollectDao familyCollectDao;
    @Autowired
    private RspFamilyCollectFactory familyCollectFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private IFamilyCollectRuleContainer familyCollectRuleContainer;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private LockRedis lockRedis;

    @RpcAnnotation(cmd = "family.collect.info", req = FamilyCollectReq.class, name = "收集面板信息", vo = FamilyCollectInfoVO.class)
    public FamilyCollectInfoVO collectInfo(Session session, FamilyCollectReq req) throws GameException {
        if(req.getFid()==null){
            return null;
        }
        FamilyCollect familyCollect = familyCollectDao.getFamilyCollect(req.getFid());
        return familyCollectFactory.getFamilyCollectInfoVO(familyCollect);
    }

    @RpcAnnotation(cmd = "family.collect.donate", req = FamilyCollectReq.class, name = "捐献物品", vo = FamilyCollectInfoVO.class)
    public FamilyCollectInfoVO donate(Session session, FamilyCollectReq req) throws GameException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(req.getFid());
        Player player = playerDao.getBean(session.getPid());
        familyRuleContainer.checkMember(family, familyMemberInfo);
        if (req.getNum() <= 0) {
            return null;
        }

        String lock = "family.collect.donate." + req.getFid();
        if (!lockRedis.lock(lock)) {
//            BroadcastHolder.add(new HintVO("服务器繁忙，请稍后重试！"));
            return null;
        }
        try {
            FamilyCollect familyCollect = familyCollectDao.getFamilyCollect(req.getFid());
            FamilyCollectRule familyCollectRule = familyCollectRuleContainer.getElement(familyCollect.getRuleId());
            familyCollectRuleContainer.chechDonate(session.getPid(), familyCollect, req.getNum());
            if (familyCollect.getNum() + req.getNum() > familyCollectRule.getItemNum()) {
                req.setNum(familyCollectRule.getItemNum() - familyCollect.getNum());//如果玩家 捐献的数量 大于总量，设置成刚好完成的数量
            }
            familyCollectRuleContainer.donate(session.getPid(), familyCollect, req.getNum());

            //发放奖励
            familyCollectRuleContainer.addReward(session.getPid(), familyCollect.getRuleId(), req.getNum());
            //是否收集完成 以及一些处理
            familyCollectRuleContainer.mailControl(familyCollect, family.getId(), session.getPid());
            familyCollectDao.saveBean(familyCollect);


            /**
             * 家族 广播
             * 玩家名给家族明星贡献了道具名*X，收集进度提升为XX
             */

            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(familyCollectRule.getItemId());
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            float a = (float) ((familyCollect.getNum() * 1.0) / (familyCollectRule.getItemNum() * 1.0));
            float percent = a * 100;
            String result = numberFormat.format(percent);
            //family.collect1={0}给家族明星贡献了{1}*{2}，收集进度提升为{3}
            ChatVO vo = rspRoleFactory.newFamilyChat("", "", message.getMessage("family.collect1", new String[]{player.getNickName(), baseItemRule.getName(), String.valueOf(req.getNum()), String.valueOf(result)}, Locale.CHINA));
            Session[] fsessions = statusRemote.getMember(session, familyMemberInfo.getFamilyId());
            frontendService.broadcast(fsessions, vo);


            if (familyCollect.getRanking() > 0) {//收集已经完成了
                Session[] sessions = statusRemote.getMember(session, SessionUtil.KEY_CHAT_WORLD);
                int integral = 0;
                if (FamilyConstant.COLLECT_REWARD.containsKey(familyCollect.getRanking())) {
                    integral = FamilyConstant.COLLECT_REWARD.get(familyCollect.getRanking());
                }
                ChatVO tvo = null; //完成的时候 家族广播
                ChatVO svo = null; //完成的时候 全服广播
                if (integral == 0) {
                    //family.collect3=恭喜本次收集活的本家族获得第{0}名的好成绩，很遗憾未能进入前三不能获得积分奖励
                    tvo = rspRoleFactory.newFamilyChat("", "", message.getMessage("family.collect3", new String[]{String.valueOf(familyCollect.getRanking())}, Locale.CHINA));
                    //family.collect6=经过家族名成员的不断努力，{0}顺利完成收集任务，在所有家族排行当中获得第{1}名
                    svo = rspRoleFactory.newShoutChat("", "", message.getMessage("family.collect6", new String[]{family.getName(), String.valueOf(familyCollect.getRanking())}, Locale.CHINA));
                } else {
                    //family.collect2=恭喜本次收集活的本家族获得第{0}名的好成绩，家族累计获得{1}积分奖励，将以邮件的形式按贡献比例发给本家族成员
                    tvo = rspRoleFactory.newFamilyChat("", "", message.getMessage("family.collect2", new String[]{String.valueOf(familyCollect.getRanking()), String.valueOf(integral)}, Locale.CHINA));
                    //family.collect5=经过家族名成员的不断努力，{0}顺利完成收集任务，在所有家族排行当中获得第{1}名，获得累计{2}积分奖励
                    svo = rspRoleFactory.newShoutChat("", "", message.getMessage("family.collect5", new String[]{family.getName(), family.getName(), String.valueOf(familyCollect.getRanking()), String.valueOf(integral)}, Locale.CHINA));
                }
                frontendService.broadcast(fsessions, tvo);
                frontendService.broadcast(sessions, svo);
            } else {
                //所有家族收集进度达到25%，50%，75%时，全服公告：经过家族名成员的不断努力，该家族收集进度达到XX%。
                float a1 = (float) (familyCollect.getNum() * 1.0 / familyCollectRule.getItemNum() * 1.0);
                int p = (int) (a1 * 100);
                ChatVO svo = null; //完成的时候 全服广播
                if (p == 25 || p == 50 || p == 75) {
                    svo = rspRoleFactory.newShoutChat("", "", message.getMessage("family.collect4", new String[]{family.getName(), String.valueOf(p)}, Locale.CHINA));
                }
                if (svo != null) {
                    Session[] sessions = statusRemote.getMember(session, SessionUtil.KEY_CHAT_WORLD);
                    frontendService.broadcast(sessions, svo);
                }

            }

            return familyCollectFactory.getFamilyCollectInfoVO(familyCollect);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockRedis.unlock(lock);
        }
        return null;
    }

    @RpcAnnotation(cmd = "family.collect.memberrank", req = FamilyCollectReq.class, name = "本族排行", vo = MemberColletPageVO.class)
    public MemberColletPageVO memberrank(Session session, FamilyCollectReq req) throws GameException {
        FamilyCollect familyCollect = familyCollectDao.getFamilyCollect(req.getFid());
        return familyCollectRuleContainer.getMemberCollectPageVo(familyCollect, req.getPage());
    }

    @RpcAnnotation(cmd = "family.collect.familyrank", req = FamilyCollectReq.class, name = "家族排行", vo = FamilyCollectRankVO[].class)
    public FamilyCollectRankVO[] familyrank(Session session, FamilyCollectReq req) throws GameException {
        FamilyCollect familyCollect = familyCollectDao.getFamilyCollect(req.getFid());
        return familyCollectRuleContainer.getFamilyCollectRankVO();
    }

}
