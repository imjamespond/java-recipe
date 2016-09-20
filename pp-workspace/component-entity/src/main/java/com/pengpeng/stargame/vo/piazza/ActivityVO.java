package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.farm.GoodsVO;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 下午2:42
 */
@Desc("活动VO")
public class ActivityVO {
    @Desc("活动的Id")
    private String id;
    @Desc("活动名字")
    private String name;
    @Desc("活动时间")
    private String time;
    @Desc("活动描述")
    private String desc;
    @Desc("活动 奖励物品数组  GoodsVO 数组")
    private GoodsVO [] goodsVOs;
    private int openLevel;

    @Desc("活动场景坐标 格式  map_10,863,1026")
    private String sceneCoordinates ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public GoodsVO[] getGoodsVOs() {
        return goodsVOs;
    }

    public void setGoodsVOs(GoodsVO[] goodsVOs) {
        this.goodsVOs = goodsVOs;
    }

    public int getOpenLevel() {
        return openLevel;
    }

    public void setOpenLevel(int openLevel) {
        this.openLevel = openLevel;
    }

    public String getSceneCoordinates() {
        return sceneCoordinates;
    }

    public void setSceneCoordinates(String sceneCoordinates) {
        this.sceneCoordinates = sceneCoordinates;
    }
}
