package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MoneyPick;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.model.piazza.PlayerBlessing;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午2:17
 */
@Component("moneyTreeRuleContainer")
public class MoneyTreeRuleContainerImpl extends HashMapContainer<Integer, MoneyTreeRule> implements IMoneyTreeRuleContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    @Override
    public boolean getRunMoneyTree(MoneyTree moneyTree, Date date) {

        if(moneyTree.getRipeTime().before(date)&&moneyTree.getRipeEndTime().after(date)){
            moneyTree.setStatus(2);
            return true;
        }
        /**
         * 成熟时间 已经结束了
         * 1 设置下次成熟时间 和 成熟结束时间
         * 2 清理 玩家祝福数据  玩家摇钱数据
         */
        if (moneyTree.getRipeEndTime().before(date)) {
            Date ripeTime = this.getRipeTime(moneyTree.getFamilyId(),moneyTree.getRipeTime());
            moneyTree.setRipeTime(ripeTime);
            moneyTree.setRipeEndTime(this.getRipeEndTime(ripeTime));

            moneyTree.clearPlayerBlessings();
            moneyTree.clearPlayerShake();

            moneyTree.setBlessingValue(0);
            moneyTree.setStatus(1);
            return true;
        }
        return false;
    }

    @Override
    public Date getRipeTime(String familyId,Date ripeTime) {
        FamilyRule familyRule =familyRuleContainer.getElement(familyId);
        int hour1=Integer.parseInt(familyRule.getTreeRipeTime1().split(":")[0]);
        int minute1=Integer.parseInt(familyRule.getTreeRipeTime1().split(":")[1]);
        int hour2=Integer.parseInt(familyRule.getTreeRipeTime2().split(":")[0]);
        int minute2=Integer.parseInt(familyRule.getTreeRipeTime2().split(":")[1]);

        Calendar time1 = Calendar.getInstance();
        time1.set(Calendar.HOUR_OF_DAY,hour1);
        time1.set(Calendar.MINUTE,minute1);
        time1.set(Calendar.SECOND,0);
        time1.set(Calendar.MILLISECOND,0);

        Date ripeTime1=time1.getTime();
        Date ripeEndTime1=getRipeEndTime(ripeTime1);

        Calendar time2 = Calendar.getInstance();
        time2.set(Calendar.HOUR_OF_DAY,hour2);
        time2.set(Calendar.MINUTE,minute2);
        time2.set(Calendar.SECOND,0);
        time2.set(Calendar.MILLISECOND,0);

        Date ripeTime2=time2.getTime();
        Date ripeEndTime2=getRipeEndTime(ripeTime2);

        Date now=Calendar.getInstance().getTime();

        if(now.before(ripeTime1)){
            return ripeTime1;
        }else if(now.after(ripeTime1)&&now.before(ripeEndTime1)){
            return ripeTime1;
        }else if(now.before(ripeTime2)){
            return ripeTime2;
        }else if(now.after(ripeTime2)&&now.before(ripeEndTime2)){
            return ripeTime2;
        }else{
            return DateUtil.addMinute(ripeTime1,24*60);
        }







//        Calendar c = Calendar.getInstance();
//
//        int lastHour=100;
//        if(ripeTime!=null){
//            lastHour=ripeTime.getHours();
//        }
//
//        int minute = RandomUtil.range(0, 60);
//
//        if (c.get(Calendar.HOUR_OF_DAY) < startTime1) {
//            //如果当前时间 还没到 17点 ,那么下次活动的开启时间 是  17点 到18点区间
//            c.set(Calendar.MINUTE, minute);
//            c.set(Calendar.HOUR_OF_DAY, startTime1);
//        } else if (c.get(Calendar.HOUR_OF_DAY) == startTime1&&lastHour!=startTime1) {
//            //如果当前时间 刚好是 17点 , 上次不是17点区间
//            int addMinute = RandomUtil.range(5, 20);
//            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + addMinute);
//        }  else if (c.get(Calendar.HOUR_OF_DAY) == startTime1&&lastHour==startTime1) {
//            //如果当前时间 刚好是 17点 ,上次成熟时间 是17点区间
//            c.set(Calendar.MINUTE, minute);
//            c.set(Calendar.HOUR_OF_DAY, startTime2);
//        }else if (c.get(Calendar.HOUR_OF_DAY) > startTime1 && c.get(Calendar.HOUR_OF_DAY) < startTime2) {
//            //如果当前时间  在 17点 和 22 点之间
//            c.set(Calendar.MINUTE, minute);
//            c.set(Calendar.HOUR_OF_DAY, startTime2);
//        } else if (c.get(Calendar.HOUR_OF_DAY) == startTime2&&lastHour!=startTime2) {
//            // 如果当前时间 是22点，上次成熟时间 不是22点
//            int addMinute = RandomUtil.range(5, 20);
//            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + addMinute);
//        } else {
//            //如果是23点，那么 下次开启是 第二天的 17-18区间
//            c.set(Calendar.HOUR_OF_DAY, startTime1);
//            c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);
//        }
//        c.set(Calendar.SECOND, 0);
//        return c.getTime();
    }

    @Override
    public Date getRipeEndTime(Date date) {
        return DateUtil.addMinute(date, 20);
    }

    @Override
    public void checkBlessing(Player player, MoneyTree moneyTree, int level) throws AlertException {
        Date date = new Date();
        MoneyTreeRule moneyTreeRule = this.getElement(level);
        PlayerBlessing playerBlessing = moneyTree.getPlayerBlessing(player.getId());
        if (player.getGameCoin() < moneyTreeRule.getGameCoin()) {
            exceptionFactory.throwAlertException("family.moneyTree.nogold");
        }
        if (playerBlessing != null) {
            if (playerBlessing.getNum() >= moneyTreeRule.getNumberOfBlessing()) {
                exceptionFactory.throwAlertException("family.moneyTree.maxnum");
            }
            if (date.getTime() < playerBlessing.getNextTime().getTime()) {
                exceptionFactory.throwAlertException("family.moneyTree.time");
            }
        }
        if (moneyTree.getBlessingValue() >= moneyTreeRule.getBlessingMax()) {
            exceptionFactory.throwAlertException("family.moneyTree.treemax");
        }
    }

    @Override
    public void blessing(FamilyMemberInfo familyMemberInfo,Player player, MoneyTree moneyTree, int level) {
        Date date = new Date();
        MoneyTreeRule moneyTreeRule = this.getElement(level);
        //扣钱
//        player.decGameCoin(moneyTreeRule.getGameCoin());
        playerRuleContainer.decGameCoin(player,moneyTreeRule.getGameCoin());

        //修改祝福信息
        PlayerBlessing playerBlessing = moneyTree.getPlayerBlessing(player.getId());
        if (playerBlessing == null) {
            playerBlessing = new PlayerBlessing(player.getId());
        }
        playerBlessing.setNum(playerBlessing.getNum() + 1);
        playerBlessing.setNextTime(DateUtil.addMinute(date, 10));

        moneyTree.getPlayerBlessings().put(player.getId(), playerBlessing);

        //增加摇钱树的祝福值
        moneyTree.setBlessingValue(moneyTree.getBlessingValue() + moneyTreeRule.getBlessing());

        //贡献
        familyMemberInfo.incDevote(moneyTreeRule.getAcquireDevote(),new Date());




        RewardVO rewardVO=rsp.getRewardVO(3);
        rewardVO.setGold(-moneyTreeRule.getGameCoin());
        rewardVO.setNum(moneyTreeRule.getAcquireDevote());
        BroadcastHolder.add(rewardVO);

    }

    @Override
    public void checkRock(String pid, MoneyTree moneyTree,FamilyMemberInfo familyMemberInfo) throws AlertException {
        /**
         *明星助理有5次 摇钱 机会
         */
        if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){

            if(moneyTree.getShakeNum(pid)>=FamilyConstant.ZL_ROCK_NUM){
                exceptionFactory.throwAlertException("你已经摇钱5次了！");
            }
            return;
        }

        if (moneyTree.getPlayerShake().containsKey(pid)) {
            exceptionFactory.throwAlertException("family.moneyTree.rock");
        }
    }

    @Override
    public boolean rock(Player player, MoneyTree moneyTree, int level,FamilyMemberInfo familyMemberInfo) {

        boolean drop=false;

        MoneyTreeRule moneyTreeRule = this.getElement(level);
        Date date=new Date();
        PlayerBlessing playerBlessing = moneyTree.getPlayerBlessing(player.getId());
        /**
         * 加上一次摇钱记录
         */
        moneyTree.getPlayerShake().put(player.getId(),String.valueOf(moneyTree.getShakeNum(player.getId())+1));
        /**
         *玩家加 游戏币
         *   =(总产出参数/2+总产出参数/2*摇钱树祝福值/祝福值上限)*（奖励参数1*玩家祝福次数*奖励参数2+奖励参数3）
         =(OutputPar/2+OutputPar/2*摇钱树祝福值/blessingMax)*(rewardPar1+玩家祝福次数*rewardPar3+rewardPar3)
         **/
        int gameMoeny= (int) ((moneyTreeRule.getOutputPar()/2+moneyTreeRule.getOutputPar()/2*moneyTree.getBlessingValue()/moneyTreeRule.getBlessingMax())
                *(moneyTreeRule.getRewardPar1()+playerBlessing.getNum()*moneyTreeRule.getRewardPar3()+moneyTreeRule.getRewardPar3()));

//        player.incGameCoin(gameMoeny);
        playerRuleContainer.incGameCoin(player,gameMoeny);

        /**
         * 摇出公共钱
         */

        boolean can= true;
        if(moneyTree.getNextDropTime()!=null&&date.getTime()<moneyTree.getNextDropTime().getTime()){
            can=false;

        }else {
            int r=RandomUtil.range(0,100);
            if(r>moneyTreeRule.getDropChance()){
                can=false;
            }
        }
        if(can){
            /**
             * 掉落游戏币计算
             =(总产出参数/2+总产出参数/2*摇钱树祝福值/祝福值上限)*掉落参数
             =(OutputPar/2+OutputPar/2*摇钱树祝福值/blessingMax)*dropPar

             掉落地上的游戏币数量计算
             =掉落游戏币/掉落面额
             =掉落游戏币/dropPar

             */
            int dropAll=(int) ((moneyTreeRule.getOutputPar()/2+moneyTreeRule.getOutputPar()/2*moneyTree.getBlessingValue()/moneyTreeRule.getBlessingMax())*moneyTreeRule.getDropPar());
            /**
             * 如果是明星助理 要出来的钱 掉落钱袋数量翻倍
             */
            if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){
               dropAll=dropAll*2;
            }
            for(int i=0;i<dropAll/moneyTreeRule.getDropDenomination();i++){
                MoneyPick moneyPick=new MoneyPick();
                moneyPick.setId(Uid.uuid());
                moneyPick.setMoney(moneyTreeRule.getDropDenomination());
                moneyPick.setPosition(moneyTreeRule.getPositions()[RandomUtil.range(0,moneyTreeRule.getPositions().length)]);
                moneyTree.getMoneyPickList().add(moneyPick);
            }

            moneyTree.setNextDropTime(DateUtil.addSecond(date,moneyTreeRule.getDropFrequency()));
            drop= true;
        }



        /**
         * 掉落道具的 实现
         */




        RewardVO rewardVO=rsp.getRewardVO(4);
        rewardVO.setGold(gameMoeny);
        BroadcastHolder.add(rewardVO);

        return drop;

    }

    @Override
    public MoneyPick pickMoney(Player player, MoneyTree moneyTree, String id)throws  AlertException{
        MoneyPick moneyPick=null;
        for(int i=0;i<moneyTree.getMoneyPickList().size();i++){
            if(id.equals(moneyTree.getMoneyPickList().get(i).getId())){
                moneyPick=moneyTree.getMoneyPickList().get(i);
            }
        }
        if(moneyPick!=null){
//            player.incGameCoin(moneyPick.getMoney());
            playerRuleContainer.incGameCoin(player,moneyPick.getMoney());
            moneyTree.getMoneyPickList().remove(moneyPick);
            return moneyPick;
        } else {
            exceptionFactory.throwAlertException("family.moneyTree.pick");
        }
        return moneyPick;
    }

    @Override
    public void setRipe(MoneyTree moneyTree,int minute) {
        Date ripeTime=DateUtil.addSecond(new Date(),minute*60);
        moneyTree.setRipeTime(ripeTime);
        moneyTree.setRipeEndTime(getRipeEndTime(ripeTime));
    }


}
