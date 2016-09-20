package com.pengpeng.stargame.vo.farm.process;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseItemVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;

import java.util.List;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午12:18
 */
@Desc("农场加工 物品VO")
public class FarmProcessItemVO extends BaseItemVO {
    @Desc("编号，唯一")
    private String proceId;
    @Desc("1表示食品，2表示工艺，3表示其他")
    private int type;
    @Desc("加工的物品编号")
    private String items;
    @Desc("加工的物品的名字")
    private String itemsName;
    @Desc("完成加工所需要的时间,单位秒")
    private int time;
    @Desc("所需要的材料编辑  GoodsVO数组")
    private GoodsVO[] materialGoodsVO;


    public String getProceId() {
        return proceId;
    }

    public void setProceId(String proceId) {
        this.proceId = proceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public GoodsVO[] getMaterialGoodsVO() {
        return materialGoodsVO;
    }

    public void setMaterialGoodsVO(GoodsVO[] materialGoodsVO) {
        this.materialGoodsVO = materialGoodsVO;
    }
}
