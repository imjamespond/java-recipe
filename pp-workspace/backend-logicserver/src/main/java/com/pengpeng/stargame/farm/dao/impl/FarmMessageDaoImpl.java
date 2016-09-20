package com.pengpeng.stargame.farm.dao.impl;


import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmMessageDao;
import com.pengpeng.stargame.model.farm.FarmMessage;
import com.pengpeng.stargame.model.farm.FarmMessageInfo;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午11:52
 */
@Component
@DaoAnnotation(prefix = "farm.message.")
public class FarmMessageDaoImpl extends RedisDao<FarmMessageInfo> implements IFarmMessageDao {
    @Autowired
    private FarmBuilder farmBuilder;
    @Override
    public Class<FarmMessageInfo> getClassType() {
        return FarmMessageInfo.class;
    }

    @Override
    public FarmMessageInfo getFarmMessageInfo(String pid) {
        FarmMessageInfo farmMessageInfo=getBean(pid);
        if(farmMessageInfo==null){
            farmMessageInfo=farmBuilder.newFarmMessageInfo(pid);
            saveBean(farmMessageInfo);
            return farmMessageInfo;
        }
        if(farmMessageInfo.check(new Date())) {
            saveBean(farmMessageInfo);
        }

        return farmMessageInfo;
    }
}
