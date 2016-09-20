package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午12:11
 */
@Desc("个人房间 请求")
public class RoomIdReq extends BaseReq{
    @Desc("房间Id ，请求自己的房间的时候传 自己的Id，请求好友房间的时候 传好友Id")
    private String roomId;
    @Desc("物品数量")
    private int num;
    @Desc("物品Id")
    private String itemId;
    @Desc("本次编辑 变化的 装饰品对象数组")
    private DecorateVO[] decorateVOs;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public DecorateVO[] getDecorateVOs() {
        return decorateVOs;
    }

    public void setDecorateVOs(DecorateVO[] decorateVOs) {
        this.decorateVOs = decorateVOs;
    }


}
