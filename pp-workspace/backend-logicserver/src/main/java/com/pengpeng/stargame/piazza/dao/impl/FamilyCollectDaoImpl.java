package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;
import com.pengpeng.stargame.piazza.dao.IFamilyCollectDao;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午4:43
 */
@Component
@DaoAnnotation(prefix = "pza.family.collect.info.")
public class FamilyCollectDaoImpl extends RedisDao<FamilyCollect> implements IFamilyCollectDao {
    @Override
    public Class<FamilyCollect> getClassType() {
        return FamilyCollect.class;
    }

    @Override
    public FamilyCollect getFamilyCollect(String fid) {
        FamilyCollect familyCollect=getBean(fid);
        if(familyCollect==null){
            familyCollect=new FamilyCollect();
            familyCollect.setFid(fid);
            saveBean(familyCollect);
            return familyCollect;
        }
        return familyCollect;
    }
}
