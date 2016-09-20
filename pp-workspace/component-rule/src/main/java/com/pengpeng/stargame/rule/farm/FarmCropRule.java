package com.pengpeng.stargame.rule.farm;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 农场 作物 数据
 * User: mql
 * Date: 13-4-25
 * Time: 下午3:05
 */
@Entity
@Table(name = "sg_rule_farm_crop")
public class FarmCropRule extends BaseEntity<String>{
    @Id
    private String itemsId;
    @Column
    private String name;
    @Column
    private int type;
    @Column
    private int subType;
    @Column
    private int recyclingPrice;
    @Column
    private int gamePrice;
    @Column
    private int goldPrice;
    @Column
    private int farmLevel;
    @Column
    private int overlay;
    @Column
    private String des;
    @Column
    private String icon;

    @Override
    public String getId() {
        return itemsId;
    }

    @Override
    public void setId(String id) {
       this.itemsId=id;
    }

    @Override
    public String getKey() {
        return itemsId;
    }

    public String getItemsId() {
        return itemsId;
    }

    public void setItemsId(String itemsId) {
        this.itemsId = itemsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getRecyclingPrice() {
        return recyclingPrice;
    }

    public void setRecyclingPrice(int recyclingPrice) {
        this.recyclingPrice = recyclingPrice;
    }

    public int getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(int gamePrice) {
        this.gamePrice = gamePrice;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
