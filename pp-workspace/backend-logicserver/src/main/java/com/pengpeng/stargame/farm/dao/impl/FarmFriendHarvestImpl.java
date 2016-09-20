package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmFriendHarvestDao;
import com.pengpeng.stargame.model.farm.FarmFriendHarvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-4-28
 * Time: 上午11:41
 */
@Component("farmFriendHarvestDao")
@DaoAnnotation(prefix = "farm.friendharvest.")
public class FarmFriendHarvestImpl extends RedisDao<FarmFriendHarvest> implements IFarmFriendHarvestDao {
    @Autowired
    private  FarmBuilder farmBuilder;
    @Override
    public Class<FarmFriendHarvest> getClassType() {
        return FarmFriendHarvest.class;
    }

    @Override
    public FarmFriendHarvest getFarmFriendHarvest(String pId) {
        FarmFriendHarvest farmFriendHarvest=getBean(pId);
        if(farmFriendHarvest==null){
            farmFriendHarvest= farmBuilder.newFarmFriendHarvest(pId);
            saveBean(farmFriendHarvest);
        }
        if (farmFriendHarvest.check(new Date())){
            this.saveBean(farmFriendHarvest);
        }
        return farmFriendHarvest;
    }
}
