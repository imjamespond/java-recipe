package com.pengpeng.stargame.lottery.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisListDao;
import com.pengpeng.stargame.lottery.dao.ILotteryInfoListDao;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.model.piazza.StarGift;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
@Component
@DaoAnnotation(prefix = "lotteryinfo.")
public class LotteryInfoListDaoImpl extends RedisListDao<String ,OneLotteryInfo> implements ILotteryInfoListDao {
    public static int LIMIT = 5;

    @Override
    public Class<OneLotteryInfo> getClassType() {
        return OneLotteryInfo.class;
    }

    @Override
    public void addLotteryInfo(String key, OneLotteryInfo oneLotteryInfo) {
        if(getLotterySize(key)>=LIMIT){
            rPop(key);
        }
        lPush(key,oneLotteryInfo);
    }

    @Override
    public List<OneLotteryInfo> getLotteryList(String key) {
        return this.getList(key,0,LIMIT);
    }

    @Override
    public long getLotterySize(String key) {
        return this.size(key);
    }
}
