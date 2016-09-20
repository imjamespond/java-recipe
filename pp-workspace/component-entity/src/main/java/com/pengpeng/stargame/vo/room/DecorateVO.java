package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午7:23
 */
@Desc("一个装饰 Vo")
public class DecorateVO {
    @Desc("装饰品唯一Id")
    private String id;
    @Desc("改变类型 ：客户端请求消息的时候 需要  1表示新加 2表示修改（需要传Id）比如 移动位置 3 回收  4 卖出 5清理")
    private int changeType;
    @Desc("添加的类型 1表示购买 2表示从背包里面托的")
    private int addType;
    @Desc("如果是从背包里面脱的，那么对应的 格子Id")
    private String itemGid;
    @Desc("装饰品Id")
    private String itemId;
    @Desc("装饰的位置，客户端 定义")
    private String position;
    @Desc("图片")
    private String image;
    @Desc("类型 服务端返回给客户端的时候 传的 装饰品类型")
    private int type;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
    }

    public String getItemGid() {
        return itemGid;
    }

    public void setItemGid(String itemGid) {
        this.itemGid = itemGid;
    }
}
