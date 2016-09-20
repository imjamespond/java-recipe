package com.pengpeng.stargame.farm.dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.farm.dao.IFarmEvaluateDao;
import com.pengpeng.stargame.farm.dao.IFarmRankDao;
import com.pengpeng.stargame.room.dao.IRoomRankDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * User: mql
 * Date: 13-8-12
 * Time: 上午11:19
 */
@Component("farm.rank")
@DaoAnnotation(prefix = "farm.rank.")
public class FarmRankDaoImpl extends RedisZSetDao implements IFarmRankDao {
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();



    @Override
    public void playerChangeFarmLevel(String pId, double farmLevel) {
        if(contains("",pId)){ //如果排行榜包括 跟新数据
           removeBean("",pId);
        }
        addBean("",pId,farmLevel);
//        }else {
//            if(size("")<10){ //如果排行小于10个 直接加入
//                addBean("",pId,farmLevel);
//            }else{ //查出最小的 删除最小的，然后 添加 新的
//                Set<String> set=getMembersAsc("",0,0);
//                String minPid=null;//最小的 那个
//                for(String temPid:set){
//                     minPid=  temPid;
//                }
//                if(farmLevel>getScore("",minPid)){
//                    removeBean("",minPid);
//                    addBean("",pId,farmLevel);
//                }
//            }
//
//        }
    }
}
