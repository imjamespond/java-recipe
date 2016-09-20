package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.piazza.PiazzaBuilder;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午3:37
 */
@Component
@DaoAnnotation(prefix = "pza.build.")
public class FamilyBuildDaoImpl extends RedisDao<FamilyBuildInfo> implements IFamilyBuildDao {
    @Autowired
    private PiazzaBuilder builder;
    @Override
    public Class<FamilyBuildInfo> getClassType() {
        return FamilyBuildInfo.class;
    }

    @Override
    public FamilyBuildInfo getBean(String index){
        FamilyBuildInfo bi = super.getBean(index);
        if (bi==null){
            bi = builder.newFamilyBuildInfo(index);
            saveBean(bi);
        }
        return bi;
    }
}
