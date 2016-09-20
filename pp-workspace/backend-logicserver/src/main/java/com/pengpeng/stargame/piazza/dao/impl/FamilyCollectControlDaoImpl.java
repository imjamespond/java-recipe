package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.collectcrop.CollectControl;
import com.pengpeng.stargame.piazza.dao.IFamilyCollectControlDao;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:17
 */
@Component
@DaoAnnotation(prefix = "pza.family.collect.control")
public class FamilyCollectControlDaoImpl extends RedisDao<CollectControl> implements IFamilyCollectControlDao {
    @Override
    public Class<CollectControl> getClassType() {
        return CollectControl.class;
    }

    @Override
    public CollectControl getCollectControl() {
        CollectControl collectControl=getBean("");
        if(collectControl==null){
            collectControl=new CollectControl();
            saveBean(collectControl);
        }
        return collectControl;
    }
}
