package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.stall.StallConstant;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("摆摊路人信息")
public class StallPassengerInfoVO {
    @Desc("路人信息")
    private StallPassengerVO[] passengerVOs;
    @Desc("下次请求")
    private long refreshTime;
    public StallPassengerVO[] getPassengerVOs() {
        return passengerVOs;
    }

    public void setPassengerVOs(StallPassengerVO[] passengerVOs) {
        this.passengerVOs = passengerVOs;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }
}
