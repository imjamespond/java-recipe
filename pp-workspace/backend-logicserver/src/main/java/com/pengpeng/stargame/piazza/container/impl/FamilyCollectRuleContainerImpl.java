package com.pengpeng.stargame.piazza.container.impl;


import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.collectcrop.CollectControl;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyMailControl;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.*;
import com.pengpeng.stargame.piazza.rule.FamilyCollectRule;
import com.pengpeng.stargame.piazza.rule.FamilyRule;

import com.pengpeng.stargame.player.container.IMailRuleContainer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectRankVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberCollectVO;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberColletPageVO;
import com.pengpeng.stargame.vo.role.MailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:07
 */
@Component
public class FamilyCollectRuleContainerImpl extends HashMapContainer<String,FamilyCollectRule> implements IFamilyCollectRuleContainer {
    @Autowired
    private IFamilyCollectControlDao familyCollectControlDao;
    @Autowired
    private IFamilyCollectDao familyCollectDao;
    @Autowired
    private IFamilyMemberCollectDao familyMemberCollectDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFamilyMailControlDao familyMailControlDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IMailRuleContainer mailRuleContainer;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private MessageSource message;
    @Autowired
    private IIntegralRuleContainerImpl iIntegralRuleContainer;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public void refresh() {
        Date now=new Date();
        CollectControl collectControl=familyCollectControlDao.getCollectControl();
        /**
         * 开始刷新
         */
        if(collectControl.getNextTime()==null||now.after(collectControl.getNextTime())){
            String ruleid= getFamilyRuleId(null);
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                FamilyCollect familyCollect =familyCollectDao.getFamilyCollect(familyRule.getId());
                familyCollect.setNum(0);
                familyCollect.setStartTime(now);
                familyCollect.setFinishTime(null);
                familyCollect.setRanking(0);
                familyCollect.setRuleId(ruleid);
               familyCollectDao.saveBean(familyCollect);

                //清空 排行
                long size=familyMemberCollectDao.size(familyRule.getId());
                familyMemberCollectDao.removeRange(familyRule.getId(),0,size);
            }

          //设置下次刷新时间
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + FamilyConstant.COLLECT_DAY);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.HOUR_OF_DAY,20); //晚上20：00
            collectControl.setNextTime(cal.getTime());
            collectControl.setRanking(1);
            familyCollectControlDao.saveBean(collectControl);
        }
    }

    @Override
    public String getFamilyRuleId(String fid) {
        List<FamilyCollectRule> familyCollectRuleList=new ArrayList<FamilyCollectRule>();
        for(FamilyCollectRule familyCollectRule:this.values()){
            if(familyCollectRule.getFamilyId().equals("family_1")){
                familyCollectRuleList.add(familyCollectRule);
            }
        }
        int rnu= RandomUtil.range(0,familyCollectRuleList.size());
        return familyCollectRuleList.get(rnu).getCollectId();
    }

    @Override
    public void chechDonate(String pid, FamilyCollect familyCollect,int num) throws GameException {
        FamilyCollectRule familyCollectRule=getElement(familyCollect.getRuleId());
        if(baseItemRulecontainer.getGoodsNum(pid,familyCollectRule.getItemId())<num){
            exceptionFactory.throwAlertException("你仓库没有此道具!");
        }
        if(familyCollect.getRanking()!=0){
            exceptionFactory.throwAlertException("家族收集活动已经完成!");
        }
//        if(familyCollect.getNum()+num>familyCollectRule.getItemNum()){
//            exceptionFactory.throwAlertException("数量已经达到了最大!");
//        }
    }

    @Override
    public void donate(String pid, FamilyCollect familyCollect,int num) {
        FamilyCollectRule familyCollectRule=getElement(familyCollect.getRuleId());
        //扣除物品
        baseItemRulecontainer.useGoods(pid,familyCollectRule.getItemId(),num);

        //增加家族收集 进度
        familyCollect.setNum(familyCollect.getNum()+num);
        //获得本次收集活动 玩家排行的 排序Key
        String skey=getKey(familyCollect.getStartTime(),familyCollect.getFid());
        //增加玩家 贡献值
        familyMemberCollectDao.incrementScore(skey,num,pid); //用于发邮件奖励的控制
        familyMemberCollectDao.incrementScore(familyCollect.getFid(),num,pid); //用于显示
    }

    @Override
    public void mailControl( FamilyCollect familyCollect,String fid,String pid) throws AlertException {
        FamilyCollectRule familyCollectRule=getElement(familyCollect.getRuleId());
        if(familyCollect.getNum()>=familyCollectRule.getItemNum()){ //收集完成
//            /**
//             * 五一活动 前三名 发奖励
//             */
//            Set<ZSetOperations.TypedTuple<String>> daySet = familyMemberCollectDao.getReverseRangeWithScores(familyCollect.getFid(), 0, 4);
//            Iterator<ZSetOperations.TypedTuple<String>> dayIt = daySet.iterator();
//            int i = 0;
//            while (dayIt.hasNext()) {
//                ZSetOperations.TypedTuple<String> typedTuple = dayIt.next();
//                String tpid = typedTuple.getValue();
//                i++;
//                sendTop3RewardMail(tpid,i);
//            }
            /**
             * 设置 活动控制的 排序id
             */
            CollectControl collectControl=familyCollectControlDao.getCollectControl();
            familyCollect.setRanking(collectControl.getRanking());//设置收集排行
            familyCollect.setFinishTime(new Date());//设置完成时间
            collectControl.setRanking(collectControl.getRanking()+1);
            familyCollectControlDao.saveBean(collectControl);//更改总控制排行

            //如果有积分奖励
            if(FamilyConstant.COLLECT_REWARD.get(familyCollect.getRanking())!=null){
                String skey=getKey(familyCollect.getStartTime(),familyCollect.getFid());
                FamilyMailControl familyMailControl=familyMailControlDao.getFamilyMailControl(fid);
                familyMailControl.addMailRewards(skey,new Date());
                familyMailControl.addControlNum(skey, familyCollectRule.getItemNum());
                familyMailControl.addAllIntegral(skey,familyCollect.getRanking());
                familyMailControlDao.saveBean(familyMailControl);
            }
            /**
             * 给完成的玩家发邮件奖励
             */
            checkRewardMail(pid);


        }
    }

    @Override
    public MemberColletPageVO getMemberCollectPageVo(FamilyCollect familyCollect, int page) {
        if(page==0){
            page=1;
        }
        int size=7;
        int star = (page-1)*size;
        int end = page*size-1;
        MemberColletPageVO memberColletPageVO=new MemberColletPageVO();
//        String skey=getKey(familyCollect.getStartTime(),familyCollect.getFid());
        String skey=familyCollect.getFid();
        List<MemberCollectVO> memberCollectVOList=new ArrayList<MemberCollectVO>();
        FamilyCollectRule familyCollectRule=getElement(familyCollect.getRuleId());
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(familyCollectRule.getItemId());
        Set<ZSetOperations.TypedTuple<String>> set=familyMemberCollectDao.getReverseRangeWithScores(skey,star,end);
        for(ZSetOperations.TypedTuple<String> typedTuple:set){
            MemberCollectVO memberCollectVO=new MemberCollectVO();
            memberCollectVO.setPid(typedTuple.getValue());
            memberCollectVO.setNum(typedTuple.getScore().intValue());
            Player player=playerDao.getBean(typedTuple.getValue());
            memberCollectVO.setPname(player.getNickName());
            memberCollectVOList.add(memberCollectVO);
        }
        int all= (int) familyMemberCollectDao.size(skey);
        int maxPage=(all%size==0?all/size:(all/size)+1);
        memberColletPageVO.setPage(page);
        memberColletPageVO.setName(baseItemRule.getName());
        memberColletPageVO.setNeedNum(familyCollectRule.getItemNum());
        memberColletPageVO.setMaxPage(maxPage);
        memberColletPageVO.setMemberCollectVOs(memberCollectVOList.toArray(new MemberCollectVO[0]));
        return memberColletPageVO;
    }

    @Override
    public FamilyCollectRankVO[] getFamilyCollectRankVO() {
        List<FamilyCollectRankVO> familyCollectRankVOList=new ArrayList<FamilyCollectRankVO>();
        Set<String > allkey=new HashSet<String>();
        for(FamilyRule familyRule:familyRuleContainer.getAll()){
            allkey.add(familyRule.getId());
        }
        Map<String,FamilyCollect> familyCollectMap=familyCollectDao.mGet(allkey);
        for (FamilyRule familyRule:familyRuleContainer.getAll()){
            FamilyCollect familyCollect=familyCollectMap.get(familyRule.getId());
            if(familyCollect==null){
                familyCollect=familyCollectDao.getFamilyCollect(familyRule.getId());
            }
            FamilyCollectRankVO familyCollectRankVO=new FamilyCollectRankVO();
            familyCollectRankVO.setFid(familyCollect.getFid());
            familyCollectRankVO.setFname(familyRuleContainer.getElement(familyCollect.getFid()).getName());


            String ruleId=familyCollect.getRuleId();
            if(ruleId==null){
                FamilyCollect familyCollect1=familyCollectDao.getFamilyCollect("family_1");
               ruleId=familyCollect1.getRuleId();
            }
            FamilyCollectRule familyCollectRule=getElement(ruleId);

            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(familyCollectRule.getItemId());
            familyCollectRankVO.setItemName(baseItemRule.getName());
            familyCollectRankVO.setNeedNum(familyCollectRule.getItemNum());
            familyCollectRankVO.setNum(familyCollect.getNum());
            if(familyCollect.getFinishTime()!=null){
                familyCollectRankVO.setFinishTime(familyCollect.getFinishTime().getTime());
            }
            familyCollectRankVOList.add(familyCollectRankVO);
        }
        return familyCollectRankVOList.toArray(new FamilyCollectRankVO[0]);
    }

    @Override
    public void checkRewardMail(String pid) throws AlertException {
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
        if(familyMemberInfo.getFamilyId()==null||familyMemberInfo.getFamilyId().equals("")){
            return;
        }
        String fid=familyMemberInfo.getFamilyId();
        FamilyMailControl familyMailControl=familyMailControlDao.getFamilyMailControl(fid);
        if(familyMailControl.getMailRewads().size()==0){
            return;
        }
        for(Map.Entry<String,Date> entry:familyMailControl.getMailRewads().entrySet()){
            //如果 上次的 活动排行榜，里面包含了玩家，那么应该发送积分奖励的邮件 给玩家
            if(familyMemberCollectDao.contains(entry.getKey(),pid)){
               //计算积分
                int ranking=familyMailControl.getAllIntegral().get(entry.getKey());//上次排行
                int myNum=familyMemberCollectDao.getScore(entry.getKey(),pid).intValue();//自己贡献的数量
                int familyNum=familyMailControl.getNum().get(entry.getKey());//家族上次总共需要的数量
                float p=(float)myNum/(float)familyNum; //自己占的百分比
                if(p>1){
                    p=1;
                }
                int allintegral=FamilyConstant.COLLECT_REWARD.get(ranking);
                int  integral= (int)(allintegral*p);
                if(integral==0){
                    integral=1;
                }
//                //从排行榜中删除 这个玩家
                familyMemberCollectDao.removeBean(entry.getKey(),pid);

//                /**
//                 * 记录积分获取动作
//                 */
//                iIntegralRuleContainer.addIntegralAction(pid, IIntegralRuleContainerImpl.INTEGRAL_ACTION_2,integral);
                //发送邮件
                sendRewardMail(pid, integral, entry.getValue(), ranking);
                BroadcastHolder.add(new MsgVO(EventConstant.EVENT_NEW_MAIL,1));
            }
        }
    }

    @Override
    public void addReward(String pid, String ruleId, int num) {
        RewardVO  rewardVO=new RewardVO();
        FamilyCollectRule familyCollectRule=get(ruleId);
        if(familyCollectRule.getExp()!=0){ //农场经验
            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer, familyCollectRule.getExp()*num);
            farmPlayerDao.saveBean(farmPlayer);
            rewardVO.setFarmExp(familyCollectRule.getExp()*num);
        }
        if(familyCollectRule.getGameCoin()>0){
            Player player = playerDao.getBean(pid);
//            player.incGameCoin(familyCollectRule.getGameCoin()*num);
            playerRuleContainer.incGameCoin(player,familyCollectRule.getGameCoin()*num);
            playerDao.saveBean(player);
            BroadcastHolder.add(roleFactory.newPlayerVO(player));
            rewardVO.setGold(familyCollectRule.getGameCoin()*num);
        }
        if(familyCollectRule.getFanValue()>0){ //粉丝值
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
                family.incFansValue(familyCollectRule.getFanValue()*num);
                familyDao.saveBean(family);
            }
            rewardVO.setFanValue(familyCollectRule.getFanValue()*num);
        }
        if(familyCollectRule.getFunding()>0){ //家族经费
            FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
            Family family = familyDao.getBean(familyMemberInfo.getFamilyId());

            if (familyRuleContainer.isMember(family, familyMemberInfo)) {
//                family.incFunds(familyCollectRule.getFunding()*num);
                familyRuleContainer.addFailyFunds(family,familyCollectRule.getFunding()*num);
                familyDao.saveBean(family);
            }
            rewardVO.setFunding(familyCollectRule.getFunding()*num);
        }
        BroadcastHolder.add(rewardVO);
    }

    /**
     * 获取每期活动 家族成员 贡献排行 Key
     * @param date
     * @param fid
     * @return
     */
    public String getKey(Date date,String fid){
        String skey=fid+"_"+DateUtil.getDateFormat(date,"yyyy-MM-dd");
        return skey;
    }

    /**
     * 发送积分奖励的邮件
     * @param pid
     * @param integral
     * @throws AlertException
     */
    public void sendRewardMail(String pid,int integral,Date date,int ranking) throws AlertException {
        MailReq mailReq=new MailReq();
        mailReq.setType(0);
        //family.collect7=恭喜贵家族收集活动获得第{0}名的好成绩，家族累计获得{1}积分，按照你所贡献的物品的比重计算，你将获得{1}积分。
        mailReq.setTitle("积分奖励");
        mailReq.setIntegralType(IIntegralRuleContainerImpl.INTEGRAL_ACTION_2);
        int all=FamilyConstant.COLLECT_REWARD.get(ranking);
        mailReq.setContent( message.getMessage("family.collect7", new String[]{String.valueOf(ranking),String.valueOf(all),String.valueOf(integral)}, Locale.CHINA));
        mailReq.setAttachments(""+":"+integral+":"+ BaseRewardConstant.TYPE_CREDIT);
        mailRuleContainer.sendMail(pid,mailReq);
    }
    /**
     * 发送积分奖励的邮件
     * @param pid
     * @throws AlertException
     */
    public void sendTop3RewardMail(String pid,int rank) throws AlertException {
        MailReq mailReq=new MailReq();
        mailReq.setType(0);
        mailReq.setTitle("五一劳动最光荣奖");
        int s=0;
        if(rank==1){
            s=50;
        }
        else if(rank==2){
            s=20;
        }
        else  if(rank==3){
            s=10;
        }
        else {
            return;
        }
        String itmeid="items_29901";
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(itmeid);
        mailReq.setContent("恭喜你是在的家族获得明星收集获得的第"+rank+"名荣誉，特此奖励 "+baseItemRule.getName()+" *1 ，和"+s+"点网站积分");
        mailReq.setIntegralType(IIntegralRuleContainerImpl.INTEGRAL_ACTION_9);
        StringBuffer sb=new StringBuffer();
        sb.append(""+":"+s+":"+ BaseRewardConstant.TYPE_CREDIT);
        sb.append(",");
        sb.append(itmeid+":"+1+":"+ BaseRewardConstant.TYPE_ITEM);
        mailReq.setAttachments(sb.toString());
        mailRuleContainer.sendMail(pid,mailReq);
    }
}
