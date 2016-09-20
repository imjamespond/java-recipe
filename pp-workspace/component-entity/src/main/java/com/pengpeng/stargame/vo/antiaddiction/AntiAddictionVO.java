package com.pengpeng.stargame.vo.antiaddiction;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.lottery.OneLotteryInfoVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("防沉迷信息")
public class AntiAddictionVO {
	@Desc("在线时间秒")
	private long onlineTime;
    @Desc("是否认证")
    private boolean certificate;
    @Desc("是否满十八")
    private boolean eightteen;

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public boolean isCertificate() {
        return certificate;
    }

    public void setCertificate(boolean certificate) {
        this.certificate = certificate;
    }

    public boolean isEightteen() {
        return eightteen;
    }

    public void setEightteen(boolean eightteen) {
        this.eightteen = eightteen;
    }
}
