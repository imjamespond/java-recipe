package com.pengpeng.admin.piazza.dao;

import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.tongyi.dao.IBaseDao;

import java.util.List;

/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface IFamilyRuleDao extends IBaseDao<FamilyRule>{
    public List<FamilyRule> findAll();

}