package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.player.container.IActiveRuleContainer;
import com.pengpeng.stargame.player.dao.IActivePlayerDao;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.rule.ActiveRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-11下午5:18
 */
@Component
@DaoAnnotation(prefix = "active.")
public class ActivePlayerDaoImpl extends RedisDao<ActivePlayer> implements IActivePlayerDao {

    @Autowired
    private IOtherPlayerDao otherPlayerDao;
    @Autowired
    private IActiveRuleContainer activeRuleContainer;
    private int onlineTime = 120*60;
    @Override
    public Class<ActivePlayer> getClassType() {
        return ActivePlayer.class;
    }

    @Override
    public ActivePlayer getBean(String index) {
        ActivePlayer player =  super.getBean(index);
        if (null==player){
            player = new ActivePlayer(index);
        }
        if (null!=player){
            player.init();
        }
        OtherPlayer op = otherPlayerDao.getBean(index);
        if (op.isTodayLogin()){
            ActiveRule rule = activeRuleContainer.getElement(PlayerConstant.ACTIVE_TYPE_1);
            if (rule!=null){
                if (!player.isMax(PlayerConstant.ACTIVE_TYPE_1,rule.getFinishMax())){
                    player.finish(PlayerConstant.ACTIVE_TYPE_1, 1);
                }
            }
        }
        if (op.getAccumulateOnlineTime(new Date())>=onlineTime){
            ActiveRule rule = activeRuleContainer.getElement(PlayerConstant.ACTIVE_TYPE_2);
            if (rule!=null){
                if (!player.isMax(PlayerConstant.ACTIVE_TYPE_2,rule.getFinishMax())){
                    player.finish(PlayerConstant.ACTIVE_TYPE_2, 1);
                }
            }
        }else{
            player.setFinish(PlayerConstant.ACTIVE_TYPE_2, 0);
        }
        this.saveBean(player);
        return player;
    }
}
