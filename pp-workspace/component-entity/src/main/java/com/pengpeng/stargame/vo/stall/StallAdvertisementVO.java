package com.pengpeng.stargame.vo.stall;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("摆摊广告")
public class StallAdvertisementVO {

    @Desc("玩家广告")
    private StallPlayerAdvVO[] stallPlayerAdvVO;

    public StallPlayerAdvVO[] getStallPlayerAdvVO() {
        return stallPlayerAdvVO;
    }

    public void setStallPlayerAdvVO(StallPlayerAdvVO[] stallPlayerAdvVO) {
        this.stallPlayerAdvVO = stallPlayerAdvVO;
    }
}
