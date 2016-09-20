package com.pengpeng.stargame.success.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午12:17
 */
@Component
@DaoAnnotation(prefix = "player.success.")
public class PlayerSucessDaoImpl extends RedisDao<PlayerSuccessInfo> implements IPlayerSuccessDao {
    @Autowired
    private ISuccessRuleContainer successRuleContainer;
    @Override
    public Class<PlayerSuccessInfo> getClassType() {
        return PlayerSuccessInfo.class;
    }

    @Override
    public PlayerSuccessInfo getPlayerSuccessInfo(String pid) {
        PlayerSuccessInfo playerSuccessInfo=getBean(pid);
        if(playerSuccessInfo==null){
            playerSuccessInfo=new PlayerSuccessInfo(pid);
            saveBean(playerSuccessInfo);
        }
        /**
         * 初始化成就信息，如果 表中有新的 信息加入，加入到玩家数据中
         */
        if(successRuleContainer.init(playerSuccessInfo)){
           saveBean(playerSuccessInfo);
        }
        if(successRuleContainer.checkSuccess(playerSuccessInfo)){
            saveBean(playerSuccessInfo);
        }
        return playerSuccessInfo;
    }
}
