package com.pengpeng.stargame.lucky.tree.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.lucky.tree.dao.IPlayerLuckyTreeDao;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "p.lucky.tree.")
public class PlayerLuckyTreeDaoImpl extends RedisDao<PlayerLuckyTree>  implements IPlayerLuckyTreeDao {

    @Override
    public Class<PlayerLuckyTree> getClassType() {
        return PlayerLuckyTree.class;
    }

    @Override
    public PlayerLuckyTree getPlayerLuckyTree(String pid) {
        boolean needSave = false;
        PlayerLuckyTree playerLuckyTree=this.getBean(pid);
        if(null == playerLuckyTree){
            playerLuckyTree = new PlayerLuckyTree();
            playerLuckyTree.setPid(pid);
            playerLuckyTree.setLevel(1);//等级初始为1
            needSave = true;
        }

        if(null == playerLuckyTree.getWaterDate()){
            playerLuckyTree.setWaterDate(new Date(0));
            needSave = true;
        }

        if(null == playerLuckyTree.getFreeDate()){
            playerLuckyTree.setFreeDate(new Date(0));
            needSave = true;
        }

        if(null == playerLuckyTree.getGoldDate()){
            playerLuckyTree.setGoldDate(new Date(0));
            needSave = true;
        }
        if(null == playerLuckyTree.getGoldExtraDate()){
            playerLuckyTree.setGoldExtraDate(new Date(0));
            needSave = true;
        }
        if(needSave){
            saveBean(playerLuckyTree);
        }

        return playerLuckyTree;
    }


}
