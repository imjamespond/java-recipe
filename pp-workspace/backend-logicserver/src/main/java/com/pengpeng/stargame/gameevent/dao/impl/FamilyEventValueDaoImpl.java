package com.pengpeng.stargame.gameevent.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.gameevent.dao.IFamilyEventValueDao;
import com.pengpeng.stargame.model.gameEvent.FamilyEventValue;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-12-31
 * Time: 下午3:31
 */
@Component
@DaoAnnotation(prefix = "gameevent.familyvalue.")
public class FamilyEventValueDaoImpl extends RedisDao<FamilyEventValue> implements IFamilyEventValueDao {
    @Override
    public Class<FamilyEventValue> getClassType() {
        return FamilyEventValue.class;
    }

    @Override
    public FamilyEventValue getFamilyEventValue(String familyId) {
        FamilyEventValue familyEventValue=getBean(familyId);
        if(familyEventValue==null){
            familyEventValue=new FamilyEventValue();
            familyEventValue.setFamilyId(familyId);
            familyEventValue.setStatus(1);
            saveBean(familyEventValue);
            return familyEventValue;
        }
        if(familyEventValue.getStatus()==1){
            if(familyEventValue.getFirecrackerValue()>= GameEventConstant.SPRING_EVENT_VALUE){
                familyEventValue.setStatus(2);
                familyEventValue.subFValue(GameEventConstant.SPRING_EVENT_VALUE);
                familyEventValue.setTime(DateUtil.addMinute(new Date(),GameEventConstant.SPRING_EVENT_STIME));
                saveBean(familyEventValue);
            }
        }

        return familyEventValue;
    }
}
