package com.pengpeng.stargame.vo.piazza.collectcrop;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:23
 */
@Desc("家族收集排行VO")
public class FamilyCollectRankVO {
    @Desc("家族Id")
    private String fid;
    @Desc("家族名字")
    private String fname;
    @Desc("道具的名字")
    private String itemName;
    @Desc("需要的数量")
    private int needNum;
    @Desc("完成的数量")
    private int num;
    @Desc("完成时间")
    private long finishTime;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }
}
