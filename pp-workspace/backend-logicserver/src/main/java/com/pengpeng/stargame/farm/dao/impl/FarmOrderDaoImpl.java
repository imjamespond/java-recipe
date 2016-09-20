package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.IFarmOrderRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmOrderDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午10:35
 */
@Component
@DaoAnnotation(prefix = "farm.order.")
public class FarmOrderDaoImpl extends RedisDao<FarmOrder> implements IFarmOrderDao {
    @Autowired
    private FarmBuilder farmBuilder;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmOrderRuleContainer farmOrderRuleContainer;
    @Override
    public Class<FarmOrder> getClassType() {
        return FarmOrder.class;
    }

    @Override
    public FarmOrder getFarmOrder(String pId) {
        FarmOrder farmOrder=getBean(pId);
        if(farmOrder==null){
            farmOrder= farmBuilder.newFarmOrder(pId);
            saveBean(farmOrder);
        }
        if(farmOrderRuleContainer.getFarmOrder(farmOrder,System.currentTimeMillis(),farmPlayerDao.getFarmLevel(pId))){
            saveBean(farmOrder);
        }
        return farmOrder;
    }
}
