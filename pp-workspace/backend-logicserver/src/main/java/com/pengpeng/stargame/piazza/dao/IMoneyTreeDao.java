package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.MoneyTree;

import java.util.Date;

/**
 * User: mql
 * Date: 13-7-1
 * Time: 上午10:44
 */
public interface IMoneyTreeDao extends BaseDao<String,MoneyTree>{
    /**
     * 获取指定时间 时候的 摇钱树 信息
     * @param familyId
     * @param date
     * @return
     */
    public MoneyTree getMoneyTree(String familyId,Date date);
}
