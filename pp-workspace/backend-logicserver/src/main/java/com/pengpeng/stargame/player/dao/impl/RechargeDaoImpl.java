package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisMapDao;
import com.pengpeng.stargame.model.player.RechargeLog;
import com.pengpeng.stargame.player.dao.IRechargeDao;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-8上午11:11
 */
@Component
@DaoAnnotation(prefix = "recharge.")
public class RechargeDaoImpl extends RedisMapDao<String,RechargeLog> implements IRechargeDao{
    @Override
    public Class<RechargeLog> getClassType() {
        return RechargeLog.class;
    }

}
