package com.pengpeng.stargame.gm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.gm.dao.IAddGoldDao;
import com.pengpeng.stargame.model.gm.AddGoldInfo;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午12:11
 */
@Component
@DaoAnnotation(prefix = "gm.addgold")
public class AddGoldDaoImpl extends RedisDao<AddGoldInfo> implements IAddGoldDao {
    @Override
    public Class<AddGoldInfo> getClassType() {
        return AddGoldInfo.class;
    }

    @Override
    public AddGoldInfo getAddGoldInfo() {
        AddGoldInfo addGoldInfo=getBean("");
        if(addGoldInfo==null){
            addGoldInfo=new AddGoldInfo();
            addGoldInfo.setId("");
            addGoldInfo.setDayIds(new ArrayList<String>());
            addGoldInfo.setuIds(new ArrayList<String>());
            addGoldInfo.setNextTime(DateUtil.getNextCountTime());
            saveBean(addGoldInfo);
            return addGoldInfo;
        }
        Date now=new Date();
        if(now.after(addGoldInfo.getNextTime())){
            addGoldInfo.setDayIds(new ArrayList<String>());
            addGoldInfo.setNextTime(DateUtil.getNextCountTime());
            saveBean(addGoldInfo);
        }
        return addGoldInfo;
    }
}
