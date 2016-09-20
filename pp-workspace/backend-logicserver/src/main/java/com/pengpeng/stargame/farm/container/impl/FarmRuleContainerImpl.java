package com.pengpeng.stargame.farm.container.impl;


import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.pengpeng.stargame.farm.rule.Product;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.model.farm.FarmField;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午10:09
 */
@Component
public class FarmRuleContainerImpl implements IFarmRuleContainer {
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    /**
     * 根据时间 获取 农场信息
     */
    public boolean runFarmPlayer(FarmPlayer farmPlayer,long time){

        /**
         * 检测田地
         */
        boolean isChange = false;
        for (int i = 0; i < farmPlayer.getFields().size(); i++) {
            FarmField ff = farmPlayer.getFields().get(i);
            if (ff.getStatus() == FarmConstant.FIELD_STATUS_PLANT) {
                if (time >ff.getHarvestTime().getTime()) {
                    isChange = true;
                    ff.setStatus(FarmConstant.FIELD_STATUS_RIP);
                }

                /**
                 * 作物 状态的 更改，如果作物 时间 到了 1熟或者2熟 ，设置当前的 成熟Id
                 */
                int oldRipe = ff.getRipeId();
                int ripeId = 0;
                FarmSeedRule farmItemRule = (FarmSeedRule) baseItemRulecontainer.getElement(ff.getSeedId());
                for (int j = 0; j < farmItemRule.getProductList().size(); j++) {
                    Product p = farmItemRule.getProductList().get(j);
                    if (time > ff.getPlantTime().getTime() + p.getTime() * 1000) {
                        ripeId = p.getId();
                    }
                }
                if (oldRipe != ripeId) {
                    isChange = true;
                    ff.setRipeId(ripeId);
                }
                if(ff.getStatus()==FarmConstant.FIELD_STATUS_RIP){//催熟的作物
                    isChange = true;
                    ff.setRipeId(farmItemRule.getProductList().size());
                }
            }
        }

        return isChange;
    }




    @Override
    public void checkEvaluate(FarmEvaluate farmEvaluate,String friendId) throws AlertException {
        boolean bool = farmEvaluate.isEvaluate(friendId);
        if (bool){
             exceptionFactory.throwAlertException("evaluate");
        }
    }

    @Override
    public void evaluate(FarmEvaluate farmEvaluate, FarmEvaluate friendFarmEvaluate) {
        farmEvaluate.evaluate(friendFarmEvaluate.getpId());
        friendFarmEvaluate.incNum(1);
    }

    @Override
    public boolean canHelp(FarmPlayer farmPlayer) {
        for(FarmField farmField:farmPlayer.getFields()){
            if(farmField.getStatus()==FarmConstant.FIELD_STATUS_FREE) {
                continue;
            }
            FarmSeedRule farmSeedRule= (FarmSeedRule) baseItemRulecontainer.getElement(farmField.getSeedId());
            if(farmField.getRipeId()>=farmSeedRule.getProductList().size()){
                continue;
            }
            if (farmField.getRipeId() <= 0) {
                continue;
            }
            if (!farmField.isHarvestRipe(farmField.getRipeId())) {
                return true;
            }
        }
        return  false;
    }

    @Override
    public void setRipe(FarmPlayer farmPlayer) {

        for(FarmField field:farmPlayer.getFields()){
           if(field.getStatus()==FarmConstant.FIELD_STATUS_PLANT){
               field.setStatus(FarmConstant.FIELD_STATUS_RIP);
               FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRulecontainer.getElement(field.getSeedId());
               field.setRipeId(farmSeedRule.getProductList().size());
           }
        }
    }

    @Override
    public int getOpenFieldNum(String pid) {
        FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(pid,System.currentTimeMillis());
        return farmPlayer.getFields().size();
    }
}
