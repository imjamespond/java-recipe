package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmBoxRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmBoxDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午3:38
 */
@Component
public class FarmBoxRuleContainer implements IFarmBoxRuleContainer{
    @Autowired
    private IDropGfitRuleContainer dropGfitRuleContainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public void refreshFarmBox(PlayerFarmBox playerFarmBox) {
        int probability=10;
        if(playerFarmBox.getAllboxnum()==0){
            probability=100;
        }
        if(playerFarmBox.getAllboxnum()==1){
            probability=50;
        }
        if(playerFarmBox.getAllboxnum()==2){
            probability=30;
        }
        if(playerFarmBox.getAllboxnum()==3){
            probability=20;
        }
        int randomNum = RandomUtil.range(0, 100);
        if(randomNum<=probability){//出现宝箱
            int randomNum1 = RandomUtil.range(0, 100);
            if(playerFarmBox.getOpenboxnum()>=5){//最多开5次 没有锁的箱子
              playerFarmBox.setBoxstatu(1);
            }else {
                if(randomNum1<70){
                    playerFarmBox.setBoxstatu(1);
                } else {
                    playerFarmBox.setBoxstatu(2);
                }
            }

        } else {
            playerFarmBox.setBoxstatu(0);
        }
    }

    @Override
    public RewardVO openBox(PlayerFarmBox playerFarmBox,Player player) throws RuleException {
        playerFarmBox.setAllboxnum(playerFarmBox.getAllboxnum()+1);
        String giftId="";
        if(playerFarmBox.getBoxstatu()==1){
            giftId="giftId_17";
            if(player.getGoldCoin()< FarmConstant.OPEN_BOX){
                exceptionFactory.throwRuleException("goldcoin.notenough");
            }
//            player.decGoldCoin( FarmConstant.OPEN_BOX);
            playerRuleContainer.decGoldCoin(player,FarmConstant.OPEN_BOX, PlayerConstant.GOLD_ACTION_2);
        }
        if(playerFarmBox.getBoxstatu()==2){
            giftId="giftId_16";
            playerFarmBox.setOpenboxnum(playerFarmBox.getOpenboxnum()+1);
        }
        DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(giftId);
        DropItem dropItem=dropGiftRule.getReward();
        RewardVO rewardVO=new RewardVO();
        if(dropItem.getItemId().equals(Constant.GAME_MONEY_ID)){
//            player.incGameCoin(dropItem.getNum());
            playerRuleContainer.incGameCoin(player,dropItem.getNum());
            rewardVO.setGold(dropItem.getNum());
        } else if(dropItem.getItemId().equals(Constant.GOLD_MONEY_ID)){
//            player.incGoldCoin(dropItem.getNum());
            playerRuleContainer.incGoldCoin(player,dropItem.getNum(),PlayerConstant.GOLD_ACTION_50);
            rewardVO.setRmb(dropItem.getNum());
        }else if(dropItem.getItemId().equals(Constant.FARM_EXP_ID)){
            FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(playerFarmBox.getPid(),System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer,dropItem.getNum());
            farmPlayerDao.saveBean(farmPlayer);
            rewardVO.setFarmExp(dropItem.getNum());
        }else if(dropItem.getItemId().equals(Constant.INTEGRAL_ID)){
            try {
                /**
                 * 记录积分获取动作
                 */
                iIntegralRuleContainer.addIntegralAction(playerFarmBox.getPid(), IIntegralRuleContainerImpl.INTEGRAL_ACTION_6,dropItem.getNum());
                rewardVO.setIntegral(dropItem.getNum());
            } catch (Exception e) {
                exceptionFactory.throwRuleException("active.gamecoin");
            }
        } else {
            baseItemRulecontainer.addGoods(player,dropItem.getItemId(),dropItem.getNum());
            rewardVO.addGoodsVO(rsp.getGoodsVo(dropItem.getItemId(),dropItem.getNum()));
        }
        return rewardVO;
    }
}
