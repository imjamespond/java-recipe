package com.pengpeng.stargame.rule.farm;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;
import com.pengpeng.stargame.model.Player;
import com.pengpeng.stargame.rule.IItemBuyRule;
import com.pengpeng.stargame.rule.IItemSaleRule;
import com.pengpeng.stargame.rule.IItemUseRule;

import javax.persistence.*;

/**
 * 农场种子
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-6下午12:17
 */
@Entity
@Table(name = "sg_rule_farm_plant")
public class FarmItemRule extends BaseEntity<String>{
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
    private int growthTime;
    @Column
    private String output;
    @Column
    private int production;
    @Column
    private String harvestEditor;
    @Column
    private int cropsReward;
    @Column
    private int expReward;
    @Column
    private int overlay;
    @Column
    private String icon;
    @Column
    private String growthImage;
    @Column
    private String matureImage;
    @Column
    private int seedsExp;

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

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public String getHarvestEditor() {
        return harvestEditor;
    }

    public void setHarvestEditor(String harvestEditor) {
        this.harvestEditor = harvestEditor;
    }

    public int getCropsReward() {
        return cropsReward;
    }

    public void setCropsReward(int cropsReward) {
        this.cropsReward = cropsReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGrowthImage() {
        return growthImage;
    }

    public void setGrowthImage(String growthImage) {
        this.growthImage = growthImage;
    }

    public String getMatureImage() {
        return matureImage;
    }

    public void setMatureImage(String matureImage) {
        this.matureImage = matureImage;
    }

    public int getSeedsExp() {
        return seedsExp;
    }

    public void setSeedsExp(int seedsExp) {
        this.seedsExp = seedsExp;
    }

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
}
