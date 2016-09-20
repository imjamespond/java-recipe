package com.pengpeng.stargame.model.farm.decorate;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-17
 * Time: 下午3:22
 * 农场内的 单个装饰 包括 小动物
 */
public class OneDecorate {
    private String id;
    private String iId; //物品id

    private String p; //位置

    private Date sTime;//出现的时间

    private Date eTime;//有效期

    private Date effectTime;//小动物上次作用时间

    public OneDecorate(){

    }

    public OneDecorate(String id, String itemId, String position,Date effective) {
        this.iId = itemId;
        this.p = position;
        this.eTime=effective;
    }

    /**
     *
     * @param id
     * @param itemId
     * @param position
     * @param effective   过期时间
     * @param effectTime  动物的作用时间
     */
    public OneDecorate(String id, String itemId, String position,Date effective,Date effectTime) {
        this.iId = itemId;
        this.p = position;
        sTime=new Date();
        this.effectTime=effectTime;
        this.eTime=effective;
    }


    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public Date getsTime() {
        return sTime;
    }

    public void setsTime(Date sTime) {
        this.sTime = sTime;
    }

    public Date getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }

    public Date geteTime() {
        return eTime;
    }

    public void seteTime(Date eTime) {
        this.eTime = eTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
