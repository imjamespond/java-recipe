package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.PiazzaBuilder;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 上午10:33
 */
@Component
@DaoAnnotation(prefix = "pza.family.member.")
public class FamilyMemberInfoDaoImpl extends RedisDao<FamilyMemberInfo> implements IFamilyMemberInfoDao{

    @Autowired
    private PiazzaBuilder piazzaBuilder;
    @Override
    public Class<FamilyMemberInfo> getClassType() {
        return FamilyMemberInfo.class;
    }

    @Override
    public FamilyMemberInfo getBean(String index) {
        FamilyMemberInfo info = super.getBean(index);
        if(info==null){
            info=piazzaBuilder.newFamilyMemberInfo(null, index, FamilyConstant.TYPE_FS);
        }
        if (null!=info){
            info.init();
        }
        return info;
    }

    @Override
    public FamilyMemberInfo getFamilyMember(String pid) {
        if (StringUtils.isBlank(pid)){
            return null;
        }
        FamilyMemberInfo familyMemberInfo =getBean(pid) ;
        return familyMemberInfo;
    }

}
