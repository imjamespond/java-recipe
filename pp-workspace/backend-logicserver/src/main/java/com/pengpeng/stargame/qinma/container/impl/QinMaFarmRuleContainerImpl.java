package com.pengpeng.stargame.qinma.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.qinma.container.IQinMaFarmRuleContainer;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.pengpeng.stargame.farm.rule.Product;
import com.pengpeng.stargame.qinma.dao.IQinMaDao;
import com.pengpeng.stargame.qinma.rule.QinMaFarmRule;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.farm.FarmVO;
import com.pengpeng.stargame.vo.farm.FieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-13下午4:44
 */
@Component
public class QinMaFarmRuleContainerImpl extends HashMapContainer<String,QinMaFarmRule> implements IQinMaFarmRuleContainer {

    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IQinMaDao qinMaDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFarmDecorateRuleContainer farmDecorateRuleContainer;

    @Override
    public FarmVO getQinmaFarmVO() {
        QinMaFarmRule qinMaFarmRule=getElement(Constant.QINMA_ID);

        FarmVO farmVO=new FarmVO();
        farmVO.setLevel(qinMaFarmRule.getLevel());
        farmVO.setPid(qinMaFarmRule.getId());
        farmVO.setName(qinMaFarmRule.getName());
        farmVO.setGoodReputation(qinMaDao.getQinMa(Constant.QINMA_ID).getFarmEvaluation());
        List<FieldVO> fieldVOList=new ArrayList<FieldVO>();

        for(QinMaFarmRule.FarmField fd :qinMaFarmRule.getFields()){
            FieldVO fieldVO = new FieldVO();
            fieldVO.setId(Integer.parseInt(fd.getId()));
            /**
             * 设置田地的位置
             */
            if(fd.getPosition()==null||fd.getPosition().equals("")){
                fieldVO.setPosition(farmDecorateRuleContainer.getFieldPosition(fieldVO.getId()));
            } else {
                fieldVO.setPosition(fd.getPosition());
            }
            /**
             * 作物 状态的 更改，如果作物 时间 到了 1熟或者2熟 ，如果玩家还未 收获，那么客户端 显示可以收获状态
             * 如果玩家 已经收获了 显示种植状态
             */
            fieldVO.setStatus(fd.getStatus());
            /**
             *如果有作物
             */
            if (fieldVO.getStatus() != FarmConstant.FIELD_STATUS_FREE) {
                FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRulecontainer.getElement(fd.getSeedId());
                Date ripeTime=DateUtil.convertMsecondToString(System.currentTimeMillis() + farmSeedRule.getGrowthTime() * 1000);
                int ripeId=0;
                fieldVO.setName(farmSeedRule.getName());
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_PLANT) {

                    fieldVO.setImage(farmSeedRule.getGrowthImage());
                    //剩余时间

                    fieldVO.setRipetime(ripeTime.getTime() - System.currentTimeMillis());
                }
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_RIP) {
                    fieldVO.setImage(farmSeedRule.getMatureImage());
                }


                /**
                 * 设置下次成熟时间
                 */
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_PLANT) {
                    Product p = farmSeedRule.getOneProduct(ripeId + 1);
                    fieldVO.setNextRipeTime(ripeTime.getTime() - (farmSeedRule.getGrowthTime() - p.getTime()) * 1000 - System.currentTimeMillis());
                }
            }
            fieldVOList.add(fieldVO);
        }
        /**
         * 设置为开放田地
         */
        for (int i = qinMaFarmRule.getFields().size(); i < 12; i++) {
            FieldVO fieldVO = new FieldVO();
            fieldVO.setId(i + 1);
            fieldVO.setStatus(FarmConstant.FIELD_CLOSE);
            fieldVOList.add(fieldVO);

        }

        farmVO.setFieldVOs(fieldVOList.toArray(new FieldVO[0]));

        return farmVO;
    }

    @Override
    public void checkEvaluate(FarmEvaluate farmEvaluate, String friendId) throws AlertException {
        boolean bool = farmEvaluate.isEvaluate(friendId);
        if (bool){
            exceptionFactory.throwAlertException("evaluate");
        }
    }

    @Override
    public void evaluate(FarmEvaluate farmEvaluate, QinMa qinMa) {
        farmEvaluate.evaluate(qinMa.getId());
        qinMa.incFarmEvaluation(1);
    }


}
