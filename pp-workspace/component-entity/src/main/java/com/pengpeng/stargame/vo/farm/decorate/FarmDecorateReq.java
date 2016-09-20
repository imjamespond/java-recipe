package com.pengpeng.stargame.vo.farm.decorate;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;
import com.pengpeng.stargame.vo.room.DecorateVO;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午12:11
 */
@Desc("农场装扮请求")
public class FarmDecorateReq extends BaseReq{
    @Desc("农场Id ，请求自己的房间的时候传 自己的Id，请求好友房间的时候 传好友Id")
    private String farmId;
    @Desc("物品数量")
    private int num;
    @Desc("物品Id")
    private String itemId;
    @Desc("本次编辑 变化的 装饰品对象数组")
    private DecorateVO[] decorateVOs;
    @Desc("物件ID")
    private String id;
    @Desc(" 1表示清除 2表示扩充")
    private int type;
    @Desc(" 位置")
    private String position;
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
