package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-16下午5:52
 */
@Desc("移动命令请求")
public class MoveReq extends BaseReq {
    @Desc("移动请求人id,如果是自己不用输入,默认情况应该是null")
    private String id;
    @Desc("目标坐标x")
    private int x;
    @Desc("目标坐标y")
    private int y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
