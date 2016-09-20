package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 14-3-17
 * Time: 下午4:04
 */
@Component()
@DaoAnnotation(prefix = "farmdecorate.pkg.")
public class FarmDecoratePkgDaoImpl extends RedisDao<FarmDecoratePkg> implements IFarmDecoratePkgDao {

    @Autowired
    private FarmBuilder farmBuilder;

    @Override
    public Class<FarmDecoratePkg> getClassType() {
        return FarmDecoratePkg.class;
    }

    @Override
    public FarmDecoratePkg getFarmDecoratePkg(String pid) {
        FarmDecoratePkg farmDecoratePkg = super.getBean(pid);
        if(farmDecoratePkg == null){
            farmDecoratePkg = new FarmDecoratePkg();
            farmDecoratePkg.setId(pid);
            super.saveBean(farmDecoratePkg);
        }
        /**
         * 检测过期物品
         */
        List<String > list=farmDecoratePkg.checkTime();
        if(list.size()>0){
            saveBean(farmDecoratePkg);
        }

        return farmDecoratePkg;
    }
}
