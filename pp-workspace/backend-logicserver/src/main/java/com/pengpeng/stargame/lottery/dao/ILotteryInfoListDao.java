package com.pengpeng.stargame.lottery.dao;

import com.pengpeng.stargame.dao.IListDao;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.model.piazza.StarGift;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:41
 */
public interface ILotteryInfoListDao extends IListDao<String,OneLotteryInfo> {

    public void addLotteryInfo(String key, OneLotteryInfo starGift);
    public List<OneLotteryInfo> getLotteryList(String key);
    public long getLotterySize(String key);
}
