package com.pengpeng.stargame.stall.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.stall.dao.IStallAssistantDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "stall.assistant.")
public class StallAssistantDaoImpl extends RedisDao<StallAssistant> implements IStallAssistantDao {

    @Override
    public Class<StallAssistant> getClassType() {
        return StallAssistant.class;
    }


    @Override
    public StallAssistant getStallAssistant(String itemId) {
        StallAssistant sa = this.getBean(itemId);
        if(null == sa){
            sa = new StallAssistant();
            sa.setId(itemId);
        }
        if(sa.getShelfMap() == null){
            sa.setShelfMap(new HashMap<String, PlayerAssistant>());
        }
        return sa;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
