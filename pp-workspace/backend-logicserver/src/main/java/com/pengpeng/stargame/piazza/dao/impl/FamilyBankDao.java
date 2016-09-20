package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.piazza.dao.IFamilyBankDao;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-5
 * Time: 上午11:14
 */
@Component("familyBankDao")
@DaoAnnotation(prefix = "family.bank.")
public class FamilyBankDao extends RedisDao<FamilyBank> implements IFamilyBankDao{
    @Override
    public Class<FamilyBank> getClassType() {
        return FamilyBank.class;
    }

    @Override
    public FamilyBank getFamilyBank(String familyId) {
        FamilyBank familyBank=getBean(familyId);
        if(familyBank==null){
            familyBank=new FamilyBank();
            familyBank.setFamilyId(familyId);
            saveBean(familyBank);
            return familyBank;
        }
        return familyBank;
    }
}
