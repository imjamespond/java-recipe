package com.pengpeng.stargame.vo.piazza.collectcrop;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:15
 */
@Desc("收集信息VO")
public class FamilyCollectInfoVO {
    @Desc("描述")
    private String des;
    @Desc("需要的数量")
    private int needNum;
    @Desc("已经完成的数量")
    private int num;
    @Desc("剩余的毫秒数")
    private long residueTime;
    @Desc("需要收集的物品Id")
    private String itemId;
    @Desc("需要收集的物品名字")
    private String name;
    @Desc("自己的物品数量")
    private int myNum;
    @Desc("奖励说明")
    private String rewardDes;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public long getResidueTime() {
        return residueTime;
    }

    public void setResidueTime(long residueTime) {
        this.residueTime = residueTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    public String getRewardDes() {
        return rewardDes;
    }

    public void setRewardDes(String rewardDes) {
        this.rewardDes = rewardDes;
    }
}
