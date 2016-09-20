package com.pengpeng.stargame.qinma.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.qinma.rule.QinMaFarmRule;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.vo.farm.FarmVO;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-13下午4:42
 */
public interface IQinMaFarmRuleContainer extends IMapContainer<String,QinMaFarmRule> {

    public FarmVO getQinmaFarmVO();


    /**
     * 可否评价 好友的农场
     * @param farmEvaluate
     * @param friendId
     * @throws com.pengpeng.stargame.exception.AlertException
     */
    public void checkEvaluate(FarmEvaluate farmEvaluate,String friendId) throws AlertException;

    /**
     * 评价好友农场
     * @param farmEvaluate
     * @param qinMa
     */
    public void evaluate(FarmEvaluate farmEvaluate,QinMa qinMa);
}
