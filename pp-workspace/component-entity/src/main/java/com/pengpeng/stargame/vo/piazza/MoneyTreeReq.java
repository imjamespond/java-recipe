package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 下午3:32
 */
@Desc("摇钱树 请求")
public class MoneyTreeReq extends BaseReq{
    private String pid;
    @Desc("捡钱时候  传的 唯一Id")
    private String pickMoneyId;
    @Desc("家族Id")
    private String familyId;


    public MoneyTreeReq(){

    }
    public MoneyTreeReq(String familyId){
        this.familyId = familyId;
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPickMoneyId() {
        return pickMoneyId;
    }

    public void setPickMoneyId(String pickMoneyId) {
        this.pickMoneyId = pickMoneyId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
