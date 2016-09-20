package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.dao.IListDao;
import com.pengpeng.stargame.model.piazza.StarGift;
import org.springframework.data.redis.support.collections.RedisList;

import java.util.List;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午3:28
 */
public interface IStarGiftDao  extends IListDao<String,StarGift>{
    public void addGift(String familyId,StarGift starGift);
    public List<StarGift> getGiftList(String familyId,int start,int num);

    public int getGiftSize(String familyId);


    public String goldCoin();


}
