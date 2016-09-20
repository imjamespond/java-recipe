package com.pengpeng.stargame.vo.rank;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午4:23
 */
@Desc("排行VO")
public class RankVO {
    @Desc("玩家名字")
    private String pName;
    @Desc("家族名字")
    private String fName;
    @Desc("家族Id")
    private String fId;
    @Desc("玩家Id")
    private String pId;
    @Desc("排行数值")
    private int value;
    @Desc("第二排行数值")
    private int value1;


    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }
}
