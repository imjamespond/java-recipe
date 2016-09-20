package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyMailControl;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:59
 */
public interface IFamilyMailControlDao extends BaseDao<String,FamilyMailControl> {
    public  FamilyMailControl getFamilyMailControl(String fid);
}
