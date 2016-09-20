package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IIdentityRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.piazza.rule.IdentityRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 家族基本规则
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午2:16
 */
@Component("familyRuleContainerImpl")
public class FamilyRuleContainerImpl extends HashMapContainer<String, FamilyRule> implements IFamilyRuleContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IFamilyDao familyDao;

    @Autowired
    private IIdentityRuleContainer identityRuleContainer;

    @Autowired
    private IFamilyBuildingRuleContainer familyBuildingRuleContainer;

    @Autowired
    private GameLoggerWrite gameLoggerWrite;

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IFamilyBuildDao familyBuildDao;


    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public int getDefaultStarId() {
        return 124;//默认为碰碰家族
    }

    @Override
    public void canAddFamily(Family family, FamilyMemberInfo memberInfo) throws AlertException, RuleException {
        if (null == family) {//未找到对应的家族
            exceptionFactory.throwAlertException("family.notfound");
        }
        if (null == memberInfo) {//未找到对应的成员
            exceptionFactory.throwAlertException("family.notfound.member");
        }
        FamilyRule rule = this.getElement(family.getId());
        if (null == rule) {
            exceptionFactory.throwRuleException("family.notfound");
        }
        long max = familyDao.getMemberSize(family.getId());
        //家族是否满员
        if (max >= rule.getMaxMember()) {
            exceptionFactory.throwRuleException("family.max.member");
        }
        //是否已经是家族成员
        //checkMember(family,memberInfo);
//        boolean isMember = isMember(family, memberInfo);
//        if (isMember) {
//            exceptionFactory.throwRuleException("family.has.member");
//        }
    }

    @Override
    public void canRemoveMember(Family family, FamilyMemberInfo memberInfo) throws AlertException, RuleException {
        if (!isMember(family, memberInfo)) {
            exceptionFactory.throwRuleException("family.del.invalid.member");
        }
        if (FamilyConstant.TYPE_MX == memberInfo.getIdentity()) {
            //如果成员是明星
            exceptionFactory.throwRuleException("family.del.ismx");
        }
        if (FamilyConstant.TYPE_ZL == memberInfo.getIdentity()) {
            exceptionFactory.throwRuleException("family.del.iszl");
        }
    }


    @Override
    public boolean canTask(int num) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public void checkMaxFunds(Family family, FamilyBuildInfo bi) throws RuleException {
        int level = bi.getLevel(FamilyConstant.BUILD_BANK);
        FamilyBuildingRule br = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK, level);
        if (family.getFunds() >= br.getMaxFunds()) {
            exceptionFactory.throwRuleException("family.endow.maxbank");
        }
    }

    /**
     * 加入新成员,并建立家族关系
     *
     * @param info
     * @param player
     */
    @Override
    public void joinMember(Family family, FamilyMemberInfo info, Player player) {
        FamilyRule rule = this.getElement(family.getId());
        info.setFamilyId(family.getId());
        player.setStarId(rule.getStarId());
    }

    @Override
    public void familyBankGet(Player player, FamilyBank familyBank, FamilyBuildInfo familyBuildInfo, int gameMoney) throws RuleException {
        if (gameMoney > familyBank.getMoenyByPid(player.getId())) {
            exceptionFactory.throwRuleException("family.bank.no");
        }
//        player.incGameCoin(gameMoney);
        playerRuleContainer.incGameCoin(player,gameMoney);
        familyBank.getMoney(player.getId(), gameMoney);
    }

    @Override
    public void familyBankSave(Player player, FamilyBank familyBank, FamilyBuildInfo familyBuildInfo, int gameMoney) throws RuleException {
        FamilyBuildingRule familyBuildingRule = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK, familyBuildInfo.getLevel(FamilyConstant.BUILD_BANK));
        if (familyBank.getMoenyByPid(player.getId()) + gameMoney > familyBuildingRule.getMaxSave()) {
            exceptionFactory.throwRuleException("family.bank.max");
        }
        if (player.getGameCoin() < gameMoney) {
            exceptionFactory.throwRuleException("gamecoin.notenough");
        }
        familyBank.saveMoney(player.getId(), gameMoney);
//        player.decGameCoin(gameMoney);
        playerRuleContainer.decGameCoin(player,gameMoney);
    }

    @Override
    public void addFailyFunds(Family family, int value) {
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        int level = bi.getLevel(FamilyConstant.BUILD_BANK);
        FamilyBuildingRule br = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK, level);
        if (family.getFunds()+value >= br.getMaxFunds()) {
            family.incFunds(br.getMaxFunds()-family.getFunds());
        }else {
            family.incFunds(value);
        }
    }

    @Override
    public void addFamilyDevote(FamilyMemberInfo familyMemberInfo, int value) {
        familyMemberInfo.incDevote(value, new Date());
        /**
         * 任务的  统计玩家每天获取了 多少家族贡献
         */
        taskRuleContainer.updateTaskNum(familyMemberInfo.getPid(), TaskConstants.CONDI_TYPE_22,"",value);
    }

    @Override
    public void changeFamilyInfo(Family family, String content, int alterType) {
        //@Desc("家族信息修改的类型 1修改名字  2QQ群 3YY号  4公告")
        if (alterType == 1) {
            family.setName(content);
        } else if (alterType == 2) {
            family.setQq(content);
        } else if (alterType == 3) {
            family.setYy(content);
        } else if (alterType == 4) {
            family.setNotice(content);
        }
    }

    @Override
    public void checkModify(FamilyMemberInfo memberInfo, int alterType) throws RuleException {
        if (FamilyConstant.TYPE_MX == memberInfo.getIdentity() || FamilyConstant.TYPE_ZL == memberInfo.getIdentity()) {
            return;
        }
        exceptionFactory.throwRuleException("family.invalid.authority");
    }

    @Override
    public List<FamilyRule> getAll() {
        return new ArrayList<FamilyRule>(items.values());
    }

    @Override
    public FamilyRule getRuleByStarId(int starId) {

        for (FamilyRule rule : items.values()) {
            if (rule.getStarId() == starId) {
                return rule;
            }
        }
        return null;
    }

    @Override
    public void checkMember(Family family, FamilyMemberInfo info) throws RuleException {
        boolean isMember = isMember(family, info);
        if (!isMember) {
            exceptionFactory.throwRuleException("family.notmember");
        }
    }

    @Override
    public boolean isMember(Family family, FamilyMemberInfo info) {
        if (family == null || info == null) {
            return false;
        }
        if (family.getId() == null || info.getFamilyId() == null) {
            return false;
        }
        boolean isMember = familyDao.isMember(family.getId(), info.getPid());
        return isMember;
    }

    @Override
    public int welcome(Family family, Player myPlayer, Player toPlayer) throws RuleException {
        FamilyRule rule = getElement(family.getId());
        if (rule == null) {
            exceptionFactory.throwRuleException("family.join.not");
        }
        int coin = rule.getWelcomeCoin();
        if (coin > myPlayer.getGameCoin()) {
            exceptionFactory.throwRuleException("family.join.notenough");
        }
//        myPlayer.decGameCoin(coin);
//        toPlayer.incGameCoin(coin);
        playerRuleContainer.decGameCoin(myPlayer,coin);
        playerRuleContainer.incGameCoin(toPlayer,coin);
        return coin;
    }

    @Override
    public int getWelcomeCoin(Family family) {
        FamilyRule rule = getElement(family.getId());
        if (rule == null) {
            return 0;
        }
        int coin = rule.getWelcomeCoin();
        return coin;
    }

    @Override
    public RewardVO endow(Family family, FamilyMemberInfo info, Player player, int money, int alterType, FamilyBuildInfo bi) throws RuleException {
        FamilyRule rule = this.getElement(family.getId());
        if (null == rule) {
            exceptionFactory.throwRuleException("family.endow.fail");
        }
        boolean isMax = false;//家族经费是否达到了最大
        int level = bi.getLevel(FamilyConstant.BUILD_BANK);
        FamilyBuildingRule br = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK, level);
        if (family.getFunds() >= br.getMaxFunds()) {
            isMax = true;
        }
        if (alterType == 8) {//8游戏币捐献,9达人币捐献
            Date date = new Date();
//            player.decGameCoin(money);
            playerRuleContainer.decGameCoin(player,money);
            int devote = money / rule.getGameCoinDevote();
            addFamilyDevote(info,devote);//加个人贡献 奖励
            info.endow(devote, date);    //加 日捐献经费 记录
            if (!isMax) {  //如果家族经费没有达到最大那么 添加经费，否则不加
                family.incFunds(devote);
            }
            RewardVO vo = new RewardVO();
            vo.setType(5);
            vo.setNum(devote);
            //任务
            taskRuleContainer.updateTaskNum(player.getId(), TaskConstants.CONDI_TYPE_16, "", money);

            //日志
            String value = String.valueOf(0) + GameLogger.SPLIT + money;
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_21, player.getId(), value));
            return vo;
        } else if (alterType == 9) {//9达人币捐献
            Date date = new Date();
//            player.decGoldCoin(money);
            playerRuleContainer.decGoldCoin(player,money, PlayerConstant.GOLD_ACTION_10);
            int devote = money / rule.getGoldCoinDevote();
            addFamilyDevote(info,devote);//加个人贡献 奖励
            info.endow(devote, date);    //加 日捐献经费 记录
            if (!isMax) {
                family.incFunds(devote);
            }
            RewardVO vo = new RewardVO();
            vo.setType(5);
            vo.setNum(devote);

            //日志
            String value = money + GameLogger.SPLIT + 0;
            gameLoggerWrite.write(new GameLogger(GameLogger.LOG_21, player.getId(), value));

            return vo;
        }

        exceptionFactory.throwRuleException("family.endow.fail");
        return null;
    }

    @Override
    public void checkEndow(Family family, FamilyMemberInfo info, FamilyBuildInfo bi, Player player, int money, int alterType) throws RuleException {
        FamilyRule rule = this.getElement(family.getId());
        if (null == rule) {
            exceptionFactory.throwRuleException("family.endow.fail");
        }
        if (alterType == 8 && player.getGameCoin() < money) {//8游戏币捐献,9达人币捐献
            exceptionFactory.throwRuleException("gamecoin.notenough");
        } else if (alterType == 9 && player.getGoldCoin() < money) {//9达人币捐献
            exceptionFactory.throwRuleException("goldcoin.notenough");
        }

        int level = bi.getLevel(FamilyConstant.BUILD_BANK);
        FamilyBuildingRule br = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK, level);
//        if (family.getFunds()>=br.getMaxFunds()){
//            exceptionFactory.throwRuleException("family.endow.maxbank");
//        }
        if (alterType == 8) {//8游戏币捐献,9达人币捐献
            int devote = money / rule.getGameCoinDevote();
            if (devote <= 0) {
                exceptionFactory.throwRuleException("family.endow.zero1", new Object[]{rule.getGameCoinDevote()});
            }
            if (info.getDayFunds() + devote > rule.getFundsLimit()) {
                exceptionFactory.throwRuleException("family.endow.maxfunds", new Object[]{rule.getFundsLimit()});
            }
        } else if (alterType == 9) {//9达人币捐献
            int devote = money / rule.getGoldCoinDevote();
            if (devote <= 0) {
                exceptionFactory.throwRuleException("family.endow.zero2", new Object[]{rule.getGoldCoinDevote()});
            }
            if (info.getDayFunds() + devote > rule.getFundsLimit()) {
                exceptionFactory.throwRuleException("family.endow.maxfunds", new Object[]{rule.getFundsLimit()});
            }
        } else {
            exceptionFactory.throwRuleException("family.endow.fail");
        }
    }

    @Override
    public void checkChangeFamily(Family oldFamily, Family newFamily, FamilyMemberInfo memberInfo, Player player) throws RuleException, AlertException {
        //变更家族是可以退出原家族的
//        if (newFamily.getId().equalsIgnoreCase(oldFamily.getId())){
//            //默认家族不能退出
//            exceptionFactory.throwRuleException("family.change.default");
//        }
        FamilyRule rule = getElement(newFamily.getId());
        if (null == rule) {
            exceptionFactory.throwRuleException("family.notfound");
        }
        if (memberInfo.isChangeFamily(new Date())) {
            //已经变更过家族
            exceptionFactory.throwRuleException("family.change.already");
        }
        //检查是否新家族成员
        boolean isMember = isMember(newFamily, memberInfo);
//        if (isMember){
//            exceptionFactory.throwRuleException("family.change.member");
//        }
//        if (memberInfo.isWeekChangeFamily(new Date())){//如果本周更换过家族
//            if (player.getGameCoin()<rule.getChangeCoin()){//就要检查游戏币够不够
//            exceptionFactory.throwRuleException("gamecoin.notenough");
//            }
//        }
        if (memberInfo.getChangeFamilyNum() >= 1) {
            if (player.getGameCoin() < rule.getChangeCoin()) {//如果免费更换过家族
                exceptionFactory.throwRuleException("gamecoin.notenough");
            }
        }
        //检查是否可以加入新家族
        canAddFamily(newFamily, memberInfo);
        //是否能够删除旧家族的关系
        canRemoveMember(oldFamily, memberInfo);
    }

    @Override
    public void changeFamily(Family oldFamily, Family newFamily, FamilyMemberInfo memberInfo, Player player, FamilyBank familyBank) throws RuleException, AlertException {
        Date now = new Date();
        FamilyRule rule = getElement(newFamily.getId());
        memberInfo.setFamilyId(newFamily.getId());
        player.setStarId(rule.getStarId());
//        if (memberInfo.isWeekChangeFamily(now)){//如果本周更换过家族
//            player.decGameCoin(rule.getChangeCoin());//就要扣减游戏币
//        }
        if (memberInfo.getChangeFamilyNum() >= 1) { //如果免费换过一次家族
//            player.decGameCoin(rule.getChangeCoin());//就要扣减游戏币
            playerRuleContainer.decGameCoin(player,rule.getChangeCoin());
        }
        memberInfo.setChangeFamilyNum(memberInfo.getChangeFamilyNum() + 1);
        if (!familyDao.isMember(newFamily.getId(), memberInfo.getPid())) {//成员列表如果不包括玩家 那么加入
            familyDao.addMember(newFamily.getId(), memberInfo.getPid(), memberInfo.getIdentity());
        }
        memberInfo.changeFamily(now);//设置变更家族的时间
        memberInfo.setDayContribution(0);
        memberInfo.setWeekContribution(0);
        memberInfo.setContribution(0);//总贡献清0;
        familyDao.removeMember(oldFamily.getId(), player.getId());

        familyBank.delete(player.getId());//家族存款清零
    }

    @Override
    public int getBoon(FamilyBuildInfo bi, FamilyMemberInfo info) {
        int level = bi.getLevel(FamilyConstant.BUILD_CASTLE);
        FamilyBuildingRule biRule = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_CASTLE, level);
        IdentityRule rule = identityRuleContainer.getElement(info.getIdentity());
        float rate = rule.getBoonRate();
        int value = (int) (biRule.getBoon() * rate);
        return value;
    }

    @Override
    public int receiveBoon(FamilyBuildInfo bi, FamilyMemberInfo info, Player player) {
        int level = bi.getLevel(FamilyConstant.BUILD_CASTLE);
        FamilyBuildingRule biRule = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_CASTLE, level);
        IdentityRule rule = identityRuleContainer.getElement(info.getIdentity());
        float rate = rule.getBoonRate();
        int value = (int) (biRule.getBoon() * rate);
//        player.incGameCoin(value);
        playerRuleContainer.incGameCoin(player,value);
        info.receiveBoon(new Date());

        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_22, player.getId(), String.valueOf(value)));

        return value;
    }

    @Override
    public void checkBoon(Family family, FamilyBuildInfo bi, FamilyMemberInfo info, Player player) throws RuleException, AlertException {
        checkMember(family, info);//检查是否成员
        IdentityRule rule = identityRuleContainer.getElement(info.getIdentity());
        if (rule == null) {
            exceptionFactory.throwAlertException("family.identity.invalid");
        }
        boolean isBoon = info.isBoon(new Date());
        if (isBoon) {//已领取家族福利
            exceptionFactory.throwAlertException("family.boon.isboon");
        }
    }
}
