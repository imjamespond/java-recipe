package com.pengpeng.stargame.model.farm;

import java.util.Date;

/**
 * 农场动态信息
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:05
 */
public class FarmAction {
    private String fid;
    //动作类型  1访问 2好评 3帮忙收获
    private int type;
    //动作发送的时间
    private Date date;
    //帮忙收获的农场作物id
    private String itemId;
    //帮忙收获的数量
    private int num;

    public FarmAction(){

    }

    public FarmAction(String fid,int type,String itemId,int num){
        this.fid=fid;
        this.type=type;
        this.itemId=itemId;
        this.num=num;
        this.date=new Date();

    }
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
