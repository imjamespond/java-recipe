package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisListDao;
import com.pengpeng.stargame.dao.RedisSetDao;
import com.pengpeng.stargame.model.piazza.FamilyApplicant;
import com.pengpeng.stargame.piazza.dao.IApplicationSetDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
@Component
@DaoAnnotation(prefix = "family.applicant.set")
public class ApplicationSetDaoImpl extends RedisSetDao<String ,FamilyApplicant> implements IApplicationSetDao {

    @Override
    public Class<FamilyApplicant> getClassType() {
        return FamilyApplicant.class;
    }

    @Override
    public void addFamilyApplicant(String key, FamilyApplicant familyApplicant) {
        insertBean(key, familyApplicant);
    }

    @Override
    public boolean isFamilyApplicant(String fid, FamilyApplicant familyApplicant) {
        return isMember(fid,familyApplicant);
    }

    @Override
    public List<FamilyApplicant> getFamilyApplicantSet(String key) {
        return members(key);
    }

    @Override
    public long getFamilyApplicantSize(String key) {
        return this.size(key);
    }

    @Override
    public void cleanApplicant(String key) {
        clean(key);
    }


}
