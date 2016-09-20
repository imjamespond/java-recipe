package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.ISetDao;
import com.pengpeng.stargame.model.piazza.FamilyApplicant;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
public interface IApplicationSetDao extends ISetDao<String,FamilyApplicant> {

    public void addFamilyApplicant(String key, FamilyApplicant familyApplicant);
    public List<FamilyApplicant> getFamilyApplicantSet(String key);
    public long getFamilyApplicantSize(String key);
    public boolean isFamilyApplicant(String fid, FamilyApplicant familyApplicant);
    public void cleanApplicant(String key);
}
