package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerStallPassenger {
    private int action ;

    private String fashionId ;

    private String purchaseId ;

    private int credit ;//赠积分

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getFashionId() {
        return fashionId;
    }

    public void setFashionId(String fashionId) {
        this.fashionId = fashionId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
