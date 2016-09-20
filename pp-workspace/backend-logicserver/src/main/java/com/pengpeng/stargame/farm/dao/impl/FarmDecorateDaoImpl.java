package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecorateDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmField;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 14-3-13
 * Time: 下午4:03
 */
@Component()
@DaoAnnotation(prefix = "farm.decorate.")
public class FarmDecorateDaoImpl extends RedisDao<FarmDecorate> implements IFarmDecorateDao{
    @Autowired
    private FarmBuilder farmBuilder;
    @Autowired
    private IFarmDecorateRuleContainer farmDerocateRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Override
    public Class<FarmDecorate> getClassType() {
        return FarmDecorate.class;
    }

    @Override
    public FarmDecorate getFarmDecorate(String pid) {
       FarmDecorate farmDecorate=getBean(pid);
        if(farmDecorate==null){
            farmDecorate=farmBuilder.newFarmDecorate(pid);
            saveBean(farmDecorate);
        }
        /**
         * 时间检测
         */
        if(farmDecorate.checkTime().size()>0){
            saveBean(farmDecorate);
        }
        if(taskRuleContainer.isFinishAllNewTask(pid)){
            /**
             * 刷新小动物
             */
            if(farmDerocateRuleContainer.refreshAnimal(farmDecorate)){
                saveBean(farmDecorate);
            }
        }
        return farmDecorate;
    }
}
