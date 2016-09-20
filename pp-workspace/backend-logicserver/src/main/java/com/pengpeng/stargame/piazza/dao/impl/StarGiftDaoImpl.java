package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisListDao;
import com.pengpeng.stargame.model.piazza.StarGift;
import com.pengpeng.stargame.piazza.dao.IStarGiftDao;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午3:36
 */
@Component()
@DaoAnnotation(prefix = "pza.stargift.")
public class StarGiftDaoImpl extends RedisListDao<String ,StarGift> implements IStarGiftDao{
    @Override
    public Class getClassType() {
        return StarGift.class;
    }



    @Override
    public void addGift(String familyId, StarGift starGift) {
        lPush(familyId,starGift);
    }

    @Override
    public List<StarGift> getGiftList(String familyId,int start, int num) {
        return this.getList(familyId,start,num);
    }

    @Override
    public int getGiftSize(String familyId) {
        return (int) this.size(familyId);
    }

    @Override
    public String goldCoin() {
        return "gold";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
