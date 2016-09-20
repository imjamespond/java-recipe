package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessivePrizeVO;
import org.springframework.stereotype.Component;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */
@Component
public class RspSuccessiveFactory extends RspFactory {

    private static SuccessivePrizeVO[] successivePrizeVOs;

    public SuccessiveInfoVO SuccessiveInfoVO(int day, int getPrize, SuccessivePrizeVO[] spVO){
        SuccessiveInfoVO vo = new SuccessiveInfoVO();
        vo.setDay(day);
        Boolean[] arrGetPrize = new Boolean[7];
        for(int i = 0;i<7;i++){
            int isGetPrize = 1 << i+1;//day=[1-7]
            arrGetPrize[i] = (isGetPrize & getPrize) > 0;
        }
        vo.setGetPrize(arrGetPrize);
        vo.setPrizeVO(spVO);
        return vo;
    }


    static public final SuccessivePrizeVO[] getVO() {
        return successivePrizeVOs;  //To change body of implemented methods use File | Settings | File Templates.
    }

    static public void setVO(SuccessivePrizeVO[] vo) {
        successivePrizeVOs = vo;//To change body of implemented methods use File | Settings | File Templates.
    }


}
