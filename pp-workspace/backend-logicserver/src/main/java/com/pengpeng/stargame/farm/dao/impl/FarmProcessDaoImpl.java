package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmProcessDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午4:56
 */
@Component("farmprocessDao")
@DaoAnnotation(prefix = "farm.process.")
public class FarmProcessDaoImpl extends RedisDao<FarmProcessQueue> implements IFarmProcessDao {


    @Autowired
    private FarmBuilder farmBuilder;
    @Override
    public Class<FarmProcessQueue> getClassType() {
        return FarmProcessQueue.class;
    }

    @Override
    public FarmProcessQueue getFarmProcessQueue(String pid) {
        FarmProcessQueue farmProcessQueue=getBean(pid);

        if(farmProcessQueue==null){
            farmProcessQueue=farmBuilder.newFarProcessQueue(pid);
            saveBean(farmProcessQueue);
        }
        if(farmProcessQueue.calculate()){
            saveBean(farmProcessQueue);
        }

        return farmProcessQueue;
    }
}
