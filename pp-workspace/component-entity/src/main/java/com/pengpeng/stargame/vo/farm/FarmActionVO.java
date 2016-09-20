package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * User: mql
 * Date: 13-11-5
 * Time: 上午10:24
 */
@Desc("单个动态VO")
public class FarmActionVO {
    private String fid;
    @Desc("动作类型  1访问 2好评 3帮忙收获")
    private int type;
    @Desc("动作发送的时间 毫秒数 ")
    private long date;
    @Desc("帮忙收获的农场作物id")
    private String itemId;
    @Desc("帮忙收获的农场作物名字")
    private String itemName;
    @Desc("帮忙收获的数量")
    private int num;
    @Desc("头像")
    private String portrait;
    @Desc("名字")
    private String name;

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
