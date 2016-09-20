package com.pengpeng.stargame.stall.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.stall.dao.IStallAdvertisementDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.stall.adv")
public class StallAdvertisementDaoImpl extends RedisDao<StallAdvertisement>  implements IStallAdvertisementDao {

    @Override
    public Class<StallAdvertisement> getClassType() {
        return StallAdvertisement.class;
    }

    @Override
    public StallAdvertisement getStallAdvertisement() {
        StallAdvertisement stallAdvertisement=this.getBean("");
        if(null == stallAdvertisement){
            stallAdvertisement = new StallAdvertisement();
            saveBean(stallAdvertisement);
        }
        if(null == stallAdvertisement.getAdvList()){
            stallAdvertisement.setAdvList(new ArrayList<PlayerStallAdvertisement>());
        }

        return stallAdvertisement;
    }

}
