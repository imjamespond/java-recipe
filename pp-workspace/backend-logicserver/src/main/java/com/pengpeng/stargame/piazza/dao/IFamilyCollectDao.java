package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午4:42
 */
public interface IFamilyCollectDao extends BaseDao<String,FamilyCollect> {
    public FamilyCollect getFamilyCollect(String fid);
}
