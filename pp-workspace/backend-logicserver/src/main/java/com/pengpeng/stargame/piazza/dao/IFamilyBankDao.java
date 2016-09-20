package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.FamilyBank;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午11:11
 */
public interface IFamilyBankDao extends BaseDao<String ,FamilyBank>{

    FamilyBank getFamilyBank(String familyId);
}
