package com.pengpeng.stargame.qinma.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.QinMa;

/**
 * User: mql
 * Date: 13-8-14
 * Time: 上午11:15
 */
public interface IQinMaDao extends BaseDao<String,QinMa> {

    public QinMa getQinMa(String id) ;
}
