package com.pengpeng.stargame.lottery.dao;

import com.pengpeng.stargame.dao.IListDao;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.model.lottery.RouletteHist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
public interface IRouletteHistListDao extends IListDao<String,RouletteHist> {

    public void addLotteryInfo(String key, RouletteHist rh);
    public List<RouletteHist> getLotteryList(String key);
    public long getLotterySize(String key);
}
