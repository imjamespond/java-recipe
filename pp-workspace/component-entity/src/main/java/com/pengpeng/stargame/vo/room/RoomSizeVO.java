package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-22
 * Time: 下午6:03
 */
@Desc("房间 大小 Vo")
public class RoomSizeVO {
    @Desc("左边扩建了几次")
    private int x;
    @Desc("右边扩建了几次")
    private int y;

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
}
