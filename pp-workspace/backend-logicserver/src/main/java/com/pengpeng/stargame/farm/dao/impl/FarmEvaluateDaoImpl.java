package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmEvaluateDao;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:45
 */
@Component
@DaoAnnotation(prefix = "farm.evaluate.")
public class FarmEvaluateDaoImpl extends RedisDao<FarmEvaluate>  implements IFarmEvaluateDao {

    @Autowired
    private FarmBuilder farmBuilder;
    @Override
    public Class<FarmEvaluate> getClassType() {
        return FarmEvaluate.class;
    }

    @Override
    public FarmEvaluate getFarmEvaluate(String pid) {
        FarmEvaluate farmEvaluate=this.getBean(pid);
            if (farmEvaluate==null){
                farmEvaluate= farmBuilder.newFarmEvaluate(pid);
                this.saveBean(farmEvaluate);
            }
        if(farmEvaluate.check(System.currentTimeMillis())){
            farmEvaluate.setNextTime(DateUtil.getNextCountTime());
            this.saveBean(farmEvaluate);
        }

        return farmEvaluate;
    }
}
