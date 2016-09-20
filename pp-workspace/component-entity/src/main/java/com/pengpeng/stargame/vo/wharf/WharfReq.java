package com.pengpeng.stargame.vo.wharf;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("码头请求")
public class WharfReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("货单序数0-8")
    private int invoiceOrder;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getInvoiceOrder() {
        return invoiceOrder;
    }

    public void setInvoiceOrder(int invoiceOrder) {
        this.invoiceOrder = invoiceOrder;
    }
}
