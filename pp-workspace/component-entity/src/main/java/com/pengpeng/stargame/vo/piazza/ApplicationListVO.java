package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.lottery.LotteryGoodsVO;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("申请助理列表")
public class ApplicationListVO {
	@Desc("家族id")
	private String fid;
    @Desc("申请助理列表")
    private ApplicationVO[] applicationVOs;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public ApplicationVO[] getApplicationVOs() {
        return applicationVOs;
    }

    public void setApplicationVOs(ApplicationVO[] applicationVOs) {
        this.applicationVOs = applicationVOs;
    }
}
