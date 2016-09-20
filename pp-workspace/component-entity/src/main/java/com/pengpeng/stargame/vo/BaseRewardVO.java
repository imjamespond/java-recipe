package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-9-14
 * Time: 下午5:50
 */
@Desc("任务奖励")
public class BaseRewardVO {
    @Desc("物品道具id")
    private String id;
    @Desc("奖励类型:1:游戏币,2:农场经验,3:商业经验,4:积分奖励,5:道具奖励 6：家族经费 7：家族贡献 8:免费赞音乐的次数")
    private int type;
    @Desc("奖励数量")
    private int num;
    @Desc("物品类型")
    private int goodsType;
    @Desc("物品的名字")
    private String itemName;

    public BaseRewardVO(){

    }
    public BaseRewardVO(String id, int type, int num,String name) {
        this.setId(id);
        this.setType(type);
        this.setNum(num);
        this.setItemName(name);
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
