package com.pengpeng.stargame.vo.wharf;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("码头信息")
@EventAnnotation(name="event.wharf.update",desc="码头数据更新")
public class WharfInfoVO {
	@Desc("剩余时间")
	private long restTime;
    @Desc("播放动画类型:0不播动画1到达2离开")
    private int animType;
    @Desc("船是否已经到达")
    private Boolean shipArrived;
    @Desc("码头是否已启用")
    private Boolean enable;
    @Desc("积分")
    private int credit;
    @Desc("贡献")
    private int contribution;
    @Desc("货单列表")
    private WharfInvoiceInfoVO[] invoices;
    @Desc("家族排行")
    private WharfPlayerRankVO[] famRank;
    @Desc("总排行")
    private WharfPlayerRankVO[] rank;

    public long getRestTime() {
        return restTime;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }

    public int getAnimType() {
        return animType;
    }

    public void setAnimType(int animType) {
        this.animType = animType;
    }

    public Boolean getShipArrived() {
        return shipArrived;
    }

    public void setShipArrived(Boolean shipArrived) {
        this.shipArrived = shipArrived;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public WharfInvoiceInfoVO[] getInvoices() {
        return invoices;
    }

    public void setInvoices(WharfInvoiceInfoVO[] invoices) {
        this.invoices = invoices;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public WharfPlayerRankVO[] getFamRank() {
        return famRank;
    }

    public void setFamRank(WharfPlayerRankVO[] famRank) {
        this.famRank = famRank;
    }

    public WharfPlayerRankVO[] getRank() {
        return rank;
    }

    public void setRank(WharfPlayerRankVO[] rank) {
        this.rank = rank;
    }
}
