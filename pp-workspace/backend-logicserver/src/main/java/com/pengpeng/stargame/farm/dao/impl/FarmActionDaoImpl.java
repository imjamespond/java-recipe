package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmActionDao;
import com.pengpeng.stargame.model.farm.FarmAction;
import com.pengpeng.stargame.model.farm.FarmActionInfo;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午11:19
 */
@Component()
@DaoAnnotation(prefix = "farm.action.")
public class FarmActionDaoImpl extends RedisDao<FarmActionInfo> implements IFarmActionDao  {
    @Autowired
    private  FarmBuilder farmBuilder;
    @Override
    public Class<FarmActionInfo> getClassType() {
        return FarmActionInfo.class;
    }

    @Override
    public FarmActionInfo getFarmActionInfo(String pid) {
        FarmActionInfo farmActionInfo=this.getBean(pid);
        if(farmActionInfo==null){
            farmActionInfo=farmBuilder.newFarmActionInfo(pid);
            return farmActionInfo;
        }
        /**
         * 数据的刷新
         */
        if(farmActionInfo.getNextTime().after(new Date())){
            farmActionInfo.setDayVisite(0);
            farmActionInfo.setNextTime(DateUtil.getNextCountTime());
            saveBean(farmActionInfo);
        }
        /**
         * 过期数据的删除
         */
        Date now=new Date();
        List<FarmAction> delete=new ArrayList<FarmAction>();
        for(FarmAction farmAction:farmActionInfo.getFarmActionList()){
            if(DateUtil.getWeekOfYear(new Date())!=DateUtil.getWeekOfYear(farmAction.getDate())){
                delete.add(farmAction);
            }
        }
        if(delete.size()>0){
            farmActionInfo.getFarmActionList().removeAll(delete);
            farmActionInfo.setFarmActionList(farmActionInfo.getFarmActionList());
            saveBean(farmActionInfo);
        }
        return farmActionInfo;
    }
}
