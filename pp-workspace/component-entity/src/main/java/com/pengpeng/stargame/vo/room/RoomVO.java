package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午7:19
 */
@Desc("房间 Vo")
public class RoomVO {
    @Desc("房间所有人的Id")
    private String pId;
    @Desc("房间魅力值")
    private int  glamour;
    @Desc("房间所有者的名字")
    private String name;
    @Desc("今日好评数量")
    private int goodReputation;
    @Desc("能否评价0可以   1  不能")
    private int canR;

    @Desc("房间内的装饰品列表")
    private DecorateVO [] decorateVOs;
    @Desc("左边扩建了几次")
    private int x;
    @Desc("右边扩建了几次")
    private int y;
    @Desc("扩建标志 0左 1 右  2表示没有正在扩建")
    private int extension;
    @Desc("扩建剩余时间")
    private long time;
    public RoomVO(){

    }

    public RoomVO(String pId){
      this.pId=pId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getGlamour() {
        return glamour;
    }

    public void setGlamour(int glamour) {
        this.glamour = glamour;
    }

    public DecorateVO[] getDecorateVOs() {
        return decorateVOs;
    }

    public void setDecorateVOs(DecorateVO[] decorateVOs) {
        this.decorateVOs = decorateVOs;
    }

    public int getGoodReputation() {
        return goodReputation;
    }

    public void setGoodReputation(int goodReputation) {
        this.goodReputation = goodReputation;
    }

    public int getCanR() {
        return canR;
    }

    public void setCanR(int canR) {
        this.canR = canR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getExtension() {
        return extension;
    }

    public void setExtension(int extension) {
        this.extension = extension;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
