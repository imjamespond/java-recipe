package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 地图传送点
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-9下午4:43
 */
@Desc("传送点")
public class TransferVO {
    @Desc("id")
    private String id;
    @Desc("当前地图")
    private String currId;
    @Desc("当前坐标x")
    private int currX;
    @Desc("当前坐标y")
    private int currY;
    @Desc("目标地图")
    private String targetId;
    @Desc("目标坐标x")
    private int targetX;
    @Desc("目标坐标y")
    private int targetY;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrId() {
        return currId;
    }

    public void setCurrId(String currId) {
        this.currId = currId;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String target) {
        this.targetId = target;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}
