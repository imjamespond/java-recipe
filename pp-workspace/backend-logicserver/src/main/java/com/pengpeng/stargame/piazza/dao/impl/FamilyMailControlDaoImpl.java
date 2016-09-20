package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyMailControl;
import com.pengpeng.stargame.piazza.dao.IFamilyMailControlDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberCollectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:00
 */
@Component
@DaoAnnotation(prefix = "pza.family.collect.mail.")
public class FamilyMailControlDaoImpl extends RedisDao<FamilyMailControl> implements IFamilyMailControlDao {
    @Autowired
    private IFamilyMemberCollectDao familyMemberCollectDao;
    @Override
    public Class<FamilyMailControl> getClassType() {
        return FamilyMailControl.class;
    }

    @Override
    public FamilyMailControl getFamilyMailControl(String fid) {
        FamilyMailControl familyMailControl=getBean(fid);
        if(familyMailControl==null){
            familyMailControl=new FamilyMailControl();
            familyMailControl.setfId(fid);
            saveBean(familyMailControl);
            return familyMailControl;
        }
        //过期的 玩家捐献排行 Key
        Set<String> keys= familyMailControl.check();
        if(keys.size()>0){
            saveBean(familyMailControl);
            for(String key:keys){ //删除掉之前用于 发奖励邮件的 排行
                long size=familyMemberCollectDao.size(key);
                familyMemberCollectDao.removeRange(key,0,size);
            }
        }
        return familyMailControl;
    }
}
