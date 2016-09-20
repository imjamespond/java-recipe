package com.pengpeng.admin.smallgame.dao;

import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import com.tongyi.dao.IBaseDao;

import java.util.List;

/**
 * 生成模版 dao.vm
 * @author fangyaoxia
 */

public interface ISmallGameRuleDao extends IBaseDao<SmallGameRule>{
    public List<SmallGameRule> findAll();

}