package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.antiaddiction.container.IAntiAddictionContainer;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmRankDao;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-4-24
 * Time: 下午3:55
 */
@Component
public class FarmLevelRuleContainerImpl extends HashMapContainer<Integer, FarmLevelRule> implements IFarmLevelRuleContainer {
    @Autowired
    private FarmBuilder farmBuilder;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IAntiAddictionContainer antiAddictionContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspRoleFactory roleFactory;
    private static final Map<Integer, Integer> ADD_FIELD = new HashMap<Integer, Integer>();
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    @Autowired
    private IFarmRankDao farmRankDao;

    static {
        ADD_FIELD.put(5, 1);
        ADD_FIELD.put(10, 1);
        ADD_FIELD.put(15, 1);
        ADD_FIELD.put(20, 1);
        ADD_FIELD.put(25, 1);
        ADD_FIELD.put(30, 1);
        ADD_FIELD.put(35, 1);
    }

    @Override
    public void check(FarmPlayer farmPlayer) throws AlertException {
        FarmLevelRule farmLevelRule = this.getElement(farmPlayer.getLevel() + 1);
        if (farmLevelRule == null) {
            throw new AlertException("等级达到最大！");
        }
        if (farmLevelRule.getNeedExp() > farmPlayer.getExp()) {
            throw new AlertException("等级不够，不能升级！");
        }
    }

    @Override
    public void upgrade(FarmPlayer farmPlayer) {
        int needExp = this.getNeedExpByLevel(farmPlayer.getLevel() + 1);
        farmPlayer.setLevel(farmPlayer.getLevel() + 1);
        farmPlayer.setExp(farmPlayer.getExp() - needExp);
    }

    @Override
    public int getNeedExpByLevel(int level) {
        FarmLevelRule fr = this.getElement(level);
        return fr.getNeedExp();
    }

    @Override
    public void addFarmExp(FarmPlayer farmPlayer, int exp) {
        /**
         * 防沉迷
         */
        if (exp != 0) {
            exp = antiAddictionContainer.decline(farmPlayer.getId(), exp);
        }
        farmPlayer.setExp(farmPlayer.getExp() + exp);
        if (farmPlayer.getExp() >= this.getNeedExpByLevel(farmPlayer.getLevel() + 1)) {
            farmPlayer.setLevel(farmPlayer.getLevel() + 1);
            /**
             * 农场升级
             */
//            if (farmPlayer.getFields().size() < FarmConstant.FIELD_ALL_NUM) {
                addField(farmPlayer);
//            }

            /**
             * 排行榜处理
             */
            farmRankDao.playerChangeFarmLevel(farmPlayer.getId(), farmPlayer.getLevel());

            /**
             * 添加 广播 数据
             */
            RewardVO rewardVO = rsp.getRewardVO(2);
            rewardVO.setNum(farmPlayer.getLevel());
            BroadcastHolder.add(rewardVO);

            farmPlayer.setExp(farmPlayer.getExp() - this.getNeedExpByLevel(farmPlayer.getLevel()));

            /**
             * 递归调用
             */
            this.addFarmExp(farmPlayer, 0);
        }

    }

    @Override
    public void addField(FarmPlayer farmPlayer) {
        if (farmPlayer.getLevel() % 5 == 0) {
            //旧代码
//            farmPlayer.addField(farmBuilder.newFarmField(farmBuilder.getFileId(farmPlayer.getFields())));
            /**
             * 农场装饰功能的 代码
             */
            if(farmPlayer.getFields().size()<FarmConstant.FIELD_SEND_NUM){
                farmPlayer.addField(farmBuilder.newFarmField(farmBuilder.getFileId(farmPlayer.getFields())));
            } else{
                Player player=playerDao.getBean(farmPlayer.getId());
                baseItemRulecontainer.addGoods(player,FarmConstant.FIELD_ID,1);
            }
        }

    }
}
