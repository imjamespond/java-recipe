package com.pengpeng.stargame.lottery.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisListDao;
import com.pengpeng.stargame.lottery.dao.ILotteryInfoListDao;
import com.pengpeng.stargame.lottery.dao.IRouletteHistListDao;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.model.lottery.RouletteHist;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
@Component
@DaoAnnotation(prefix = "roulette.info.")
public class RouletteInfoListDaoImpl extends RedisListDao<String ,RouletteHist> implements IRouletteHistListDao {
    public static int LIMIT = 30;

    @Override
    public Class<RouletteHist> getClassType() {
        return RouletteHist.class;
    }

    @Override
    public void addLotteryInfo(String key, RouletteHist oneLotteryInfo) {
        if(getLotterySize(key)>=LIMIT){
            rPop(key);
        }
        lPush(key,oneLotteryInfo);
    }

    @Override
    public List<RouletteHist> getLotteryList(String key) {
        return this.getList(key,0,LIMIT);
    }

    @Override
    public long getLotterySize(String key) {
        return this.size(key);
    }
}
