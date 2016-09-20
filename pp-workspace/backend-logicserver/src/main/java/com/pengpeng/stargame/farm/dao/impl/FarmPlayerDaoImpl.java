package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmField;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-27 下午4:45
 */
@Component("farmPlayDao")
@DaoAnnotation(prefix = "farm.")
public class FarmPlayerDaoImpl extends RedisDao<FarmPlayer> implements IFarmPlayerDao {


    @Autowired
    private FarmBuilder farmBuilder;
    @Autowired
    private IFarmRuleContainer farmRuleContainer;
    @Override
    public Class<FarmPlayer> getClassType() {
        return FarmPlayer.class;
    }

    @Override
    public FarmPlayer getFarmPlayer(String pId,long time) {
        FarmPlayer fpObj = super.getBean(pId);
        if (fpObj == null) {
            //如果玩家 没有农场信息 那么初始化
            fpObj = farmBuilder.newFarm(pId);
            this.saveBean(fpObj);
        }
        /**
         * 田地的位置处理
         */
//        boolean  isSetP=false;
//        for(FarmField farmField:fpObj.getFields()){
//            if(farmField.getPosition()==null||farmField.getPosition().equals("")){
//                farmField.setPosition(FarmBuilder.FIELD_P.get(farmField.getId()));
//                isSetP=true;
//            }
//        }
//        if(isSetP){
//            saveBean(fpObj);
//        }
        if(farmRuleContainer.runFarmPlayer(fpObj,System.currentTimeMillis())){
            this.saveBean(fpObj);
        }
        return fpObj;

    }

    @Override
    public int getFarmLevel(String pId) {
        return this.getFarmPlayer(pId,System.currentTimeMillis()).getLevel();
    }
}
