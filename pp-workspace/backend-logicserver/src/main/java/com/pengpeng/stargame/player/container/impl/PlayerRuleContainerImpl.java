package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.rule.PlayerRule;
import com.pengpeng.stargame.rpc.BroadcastHolder;
import com.pengpeng.stargame.rsp.RspTaskFactory;
import com.pengpeng.stargame.statistics.IStatistics;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.dao.ITaskDao;
import com.pengpeng.stargame.vo.task.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家升级规则容器
 * @author 林佛权
 *
 */
@Component
public class PlayerRuleContainerImpl extends HashMapContainer<String, PlayerRule> implements IPlayerRuleContainer {
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Autowired
    private IStatistics statistics;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private RspTaskFactory rspTaskFactory;
    @Override
    public PlayerRule getRule(int level, int type) {
        for(Entry<String,PlayerRule> entry : items.entrySet()){
            PlayerRule rule = entry.getValue();
            if (rule.getType()==type&&rule.getLevel()==level){
                return rule;
            }
        }
        return null;
    }

    @Override
    public void incGoldCoin(Player player, int value,int type) {
        /**
         * 数据统计达人币的消费
         */
        statistics.recordDeductGold(player.getId(),player, type,value);
        player.incGoldCoin(value);
    }

    @Override
    public void decGoldCoin(Player player, int value,int type) {
        /**
         * 成就统计 累积消费达人币
         */
        successRuleContainer.updateSuccessNum(player.getId(),10,value,"");

        /**
         * 任务的
         */
        taskRuleContainer.updateTaskNum(player.getId(), TaskConstants.CONDI_TYPE_21,"",value);

        /**
         * 数据统计达人币的消费
         */
        statistics.recordDeductGold(player.getId(),player, type,value);
        player.decGoldCoin(value);

    }

    @Override
    public void incGameCoin(Player player, int value) {
       player.incGameCoin(value);
    }

    @Override
    public void decGameCoin(Player player, int value) {
       player.decGameCoin(value);
    }

}
