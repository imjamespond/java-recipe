package com.pengpeng.stargame.lottery.container.impl;

import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.lottery.container.ILotteryContainer;
import com.pengpeng.stargame.lottery.rule.LotteryRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.lottery.Lottery;
import com.pengpeng.stargame.model.lottery.LotteryItem;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import com.pengpeng.stargame.model.lottery.RouletteConstant;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.BaseRewardVO;
import com.pengpeng.stargame.vo.lottery.RouletteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class LotteryContainerImpl extends HashMapContainer<String, LotteryRule> implements ILotteryContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private IPlayerRuleContainer playerRuleContainer;

    public static int LIMITNUM = 5;
    private static Map<Integer, List<LotteryRule>> groupsOfRulesFree = new HashMap<Integer, List<LotteryRule>>();
    private static Map<Integer, List<LotteryRule>> groupsOfRulesGold = new HashMap<Integer, List<LotteryRule>>();

    private static List<Roulette> rouletteList0 = new ArrayList<Roulette>();//
    private static List<Roulette> rouletteList1 = new ArrayList<Roulette>();//

    public Lottery lotteryDraw(int type, int sex) {
        LotteryRule gift = null;
        List<LotteryRuleVal> lrList = new ArrayList<LotteryRuleVal>();//符合条件的奖品
        List<LotteryItem> lil = new ArrayList<LotteryItem>();//返回奖品列表
        Iterator<Entry<String, LotteryRule>> it = this.items.entrySet().iterator();
        int probability = 0;
        //获得抽奖物品
        while (it.hasNext()) {
            Entry<String, LotteryRule> en = it.next();
            LotteryRule lr = en.getValue();
            if (lr.getType() != type) {
                continue;
            }
            if (lr.getSex() != 2) {
                if (lr.getSex() != sex) {
                    continue;
                }
            }

            lrList.add(new LotteryRuleVal(lr, probability, lr.getProbability()));
            probability += lr.getProbability();
        }
        int random = (int) Math.floor(probability * Math.random());
        for (int i = 0; i < lrList.size(); i++) {
            LotteryRuleVal lrv = lrList.get(i);
            if (random >= lrv.preVal && random < lrv.val) {
                gift = lrv.lr;
                lrList.remove(i);
                break;
            }
        }
        //每个组获得1个随机值
        Map<Integer, List<LotteryRule>> ruleMap = null;
        switch (type){
            case LotteryRule.TYPE_GOLD:
                ruleMap = groupsOfRulesGold;
                break;
            default:
                ruleMap = groupsOfRulesFree;
        }
        Iterator<Entry<Integer, List<LotteryRule>>> git = ruleMap.entrySet().iterator();
        while (git.hasNext()) {
            //从奖品组获取
            Entry<Integer, List<LotteryRule>> entry = git.next();
            List<LotteryRule> glist = entry.getValue();
            if (null == glist||entry.getKey() == gift.getGroupId())
                continue;

            //筛选未中奖物品
            List<LotteryRule> filteredRule = new ArrayList<LotteryRule>();
            for(LotteryRule rule:glist)  {
                if (rule.getSex() != 2) {
                    if (rule.getSex() != sex) {
                        continue;
                    }
                }
                filteredRule.add(rule);
            }

            int counterNum = filteredRule.size();
            int index = (int) Math.floor(counterNum * Math.random());
            LotteryRule temp = filteredRule.get(index);

            LotteryItem li = new LotteryItem();
            li.setItemId(temp.getItemId());
            li.setNum(temp.getNum());
            li.setGameCoin(temp.getGameCoin());
            lil.add(li);
            if (lil.size() >= LIMITNUM) {
                break;
            }
        }

        Lottery l = new Lottery();
        l.setItemId(gift.getItemId());
        l.setNum(gift.getNum());
        l.setType(gift.getType());
        l.setLotteryItem(lil);
        l.setGroupId(gift.isSpeaker() ? 1 : 0);
        l.setGameCoin(gift.getGameCoin());
        return l;
    }

    private class LotteryRuleVal {
        public LotteryRule lr;
        public int preVal;
        public int val;

        public LotteryRuleVal(LotteryRule lr, int preVal, int val) {
            this.lr = lr;
            this.preVal = preVal;
            this.val = preVal + val;
        }
    }

    @Override
    public void addElementAndSort(LotteryRule rule) {
        Map<Integer, List<LotteryRule>> ruleMap = null;
        switch (rule.getType()){
            case LotteryRule.TYPE_GOLD:
                ruleMap = groupsOfRulesGold;
                break;
            default:
                ruleMap = groupsOfRulesFree;
        }

        int groupId = rule.getGroupId();
        List<LotteryRule> list = null;
        if (ruleMap.containsKey(groupId)) {
            list = ruleMap.get(groupId);
        } else {
            list = new ArrayList<LotteryRule>();
            ruleMap.put(groupId, list);
        }
        list.add(rule);
    }

    @Override
    public void addRouletteList(String item,String name, int num, int type, int probability, int gender, int speaker) {
        Roulette attach = new Roulette();

        BaseRewardVO vo = new BaseRewardVO(item, type, num, name);
        attach.vo = vo;
        attach.probability = probability;
        attach.speaker = speaker;
        if(gender==0) {
            rouletteList0.add(attach);
        }else if(gender==1){
            rouletteList1.add(attach);
        }else {
            rouletteList0.add(attach);
            rouletteList1.add(attach);
        }
    }

    @Override
    public void deduct(Player player, PlayerLottery pl) throws AlertException {

        if(pl.getRouletteNum() >= RouletteConstant.FREE){
            if(player.getGoldCoin()<RouletteConstant.GOLD_COIN){
                //exception
                exceptionFactory.throwAlertException("not.enough.goldcoin");
            }
            playerRuleContainer.decGoldCoin(player,RouletteConstant.GOLD_COIN, PlayerConstant.GOLD_ACTION_26);
            //经验增加
            pl.setRouletteExp(pl.getRouletteExp()+RouletteConstant.EXP_STEP);
        }else {
            pl.setRouletteNum(pl.getRouletteNum()+1);
            pl.setrRefreshTime(new Date());
        }

        //经验检视
        if(pl.getRouletteExp()>= RouletteConstant.EXP){
            pl.setRouletteNum(pl.getRouletteNum() - 1);
            pl.setRouletteExp(0);
        }

    }

    @Override
    public RouletteVO rouletteDraw(Player player ,PlayerLottery pl) throws AlertException {//term
        if(DateUtil.getDayOfYear(pl.getrRefreshTime()) != DateUtil.getDayOfYear(new Date())){
            pl.setRouletteNum(0);//次数置空
        }

        List<Roulette> rouletteList = null;
        if(player.getSex()==0)
            rouletteList = rouletteList0;
        else{
            rouletteList = rouletteList1;
        }
        if(rouletteList.size()<12)
            exceptionFactory.throwAlertException("invalid.roulette.config");
        int probability =0;
        //获得随机值
        int counterNum = 12;
        Roulette[] restList = new Roulette[counterNum];
/*        while (--counterNum > 0) {
            int index = (int) Math.floor(counterNum * Math.random());
            Roulette temp = rouletteList.get(counterNum);
            Roulette rdm = rouletteList.get(index);
            if(restList[index]==null){
                restList[index] = temp;//最后的放入随机位置
                restList[counterNum] = rdm;//随机的放入最后位置
            }else{
                restList[index] = temp;//最后的放入随机位置
                restList[counterNum] = restList[index];//从已经存在的取
            }
        }*/
        while (--counterNum >= 0) {
            restList[counterNum] = rouletteList.get(counterNum);
        }

        BaseRewardVO[] bvo = new BaseRewardVO[restList.length];

        for(int i = 0;i<restList.length;++i){
            Roulette r = restList[i];
            bvo[i] = r.vo;
            probability += r.probability;
        }

        RouletteVO vo = new RouletteVO();
        Random random = new Random();
        int chosen = random.nextInt(probability);
        int mark = 0;
        for(int i = 0;i<restList.length;++i){
            Roulette r = restList[i];
            if (chosen >= mark && chosen < mark + r.probability) {
                vo.setReward(i);
                pl.setrReward(r.vo);
                pl.setRouletteSpeaker(r.speaker);
                break;
            }
            mark += r.probability;
        }

        vo.setRewardVOs(bvo);
        return vo;
    }

    @Override
    public int rouletteAccept(Player player, PlayerLottery pl) throws AlertException {
        int notify = 0;

        FarmPackage fp = null;
        RoomPackege rp = null;
        FashionCupboard fc = null;
        FarmPlayer farmPlayer= null;
        FamilyMemberInfo familyMemberInfo = null;
        FarmDecoratePkg farmDecoratePkg=null;

        BaseRewardVO attach = pl.getrReward();
        if (null != attach) {
            if (BaseRewardConstant.TYPE_GAMECOIN == attach.getType()) {
                if(attach.getNum()>0){
                    player.incGameCoin(attach.getNum());
                    notify |= BaseRewardConstant.NOTIFY_GAMECOIN;
                }
            }
            if (BaseRewardConstant.TYPE_GOLDCOIN == attach.getType()) {
                if(attach.getNum()>0){
                    playerRuleContainer.incGoldCoin(player,attach.getNum(), PlayerConstant.GOLD_ACTION_26);
                    notify |= BaseRewardConstant.NOTIFY_GOLDCOIN;
                }
            }
            else if (BaseRewardConstant.TYPE_FARMEXP == attach.getType()) {
                if(attach.getNum()>0){
                    if(null == farmPlayer)
                        farmPlayer=farmPlayerDao.getFarmPlayer(player.getId(), System.currentTimeMillis());
                    farmLevelRuleContainer.addFarmExp(farmPlayer, attach.getNum());
                    notify |= BaseRewardConstant.NOTIFY_FARMEXP;
                }
            }
            else if (BaseRewardConstant.TYPE_FAMILY_CONTRIBUTION == attach.getType()) {
                if(attach.getNum()>0){
                    if(null == farmPlayer)
                        familyMemberInfo = familyMemberInfoDao.getFamilyMember(player.getId());
                    familyMemberInfo.incDevote(attach.getNum(), new Date());
                    notify |= BaseRewardConstant.NOTIFY_FAMILY_CONTRIBUTION;
                }
            }
//                else if (BaseRewardConstant.TYPE_CREDIT == attach.vo.getType()) {
//                    if(attach.vo.getNum()>0){
//                        try {
////                            siteDao.addCustomPointsByGame(player.getUserId(), attachment.getNum());
//                            iIntegralRuleContainer.addIntegralAction(player.getId(),mailInfo.getIntegralType(),attach.vo.getNum());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            exceptionFactory.throwAlertException("active.gamecoin");
//                        }
//                        notify |= BaseRewardConstant.NOTIFY_CREDIT;
//                    }
//                }
            else if (BaseRewardConstant.TYPE_ITEM == attach.getType()) {
                if (attach.getId() == null) {
                    exceptionFactory.throwAlertException("item.id.invalid");
                }
                //entity
                if(null == fp)
                    fp = farmPackageDao.getBean(player.getId());
                if(null == rp)
                    rp = roomPackegeDao.getBean(player.getId());
                if(null == fc)
                    fc = fashionCupboardDao.getBean(player.getId());
                if(null==farmDecoratePkg)
                    farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(player.getId());
                //item rule
                BaseItemRule baseItemRule = baseItemRulecontainer.getElement(attach.getId());
                if (baseItemRule == null) {
                    exceptionFactory.throwAlertException("item.add.failed");
                }
                //add item
                if (!baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule, attach.getNum(), fp, rp, fc,farmDecoratePkg)) {
                    if (baseItemRule.getItemtype() == BaseItemConstant.TYPE_FARM)
                        exceptionFactory.throwAlertException("package.farm.full");
                    else if (baseItemRule.getItemtype() == BaseItemConstant.TYPE_FASHION)
                        exceptionFactory.throwAlertException("package.fashion.full");
                    else
                        exceptionFactory.throwAlertException("item.add.failed");
                }
                notify |= BaseRewardConstant.NOTIFY_ITEM;
            }
        }
        //persistence
        if(null != fp)
            farmPackageDao.saveBean(fp);
        if(null != rp)
            roomPackegeDao.saveBean(rp);
        if(null != fc)
            fashionCupboardDao.saveBean(fc);
        if(null != farmPlayer)
            farmPlayerDao.saveBean(farmPlayer);
        if(null != familyMemberInfo)
            familyMemberInfoDao.saveBean(familyMemberInfo);
        if(null!=farmDecoratePkg)
            farmDecoratePkgDao.saveBean(farmDecoratePkg);
        if((notify&BaseRewardConstant.NOTIFY_GAMECOIN)>0)
            playerDao.saveBean(player);

        return notify;
    }

    private class Roulette {
        public int probability;
        public int speaker;
        public BaseRewardVO vo;
    }
}



