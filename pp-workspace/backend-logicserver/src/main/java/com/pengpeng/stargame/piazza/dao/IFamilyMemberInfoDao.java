package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 上午10:32
 */
public interface IFamilyMemberInfoDao extends BaseDao<String, FamilyMemberInfo> {

    public FamilyMemberInfo getFamilyMember(String pId);


}
