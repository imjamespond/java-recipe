package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.piazza.container.FamilyConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 建筑升级规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27上午11:26
 */
@Entity
@Table(name="sg_rule_familybuilding")
public class FamilyBuildingRule extends BaseEntity<String>{
    //建筑编号
    @Id
    private String buildingId;
    //建筑名称
    @Column
    private String name;
    //建筑类型
    @Column
    private int type;
    //等级
    @Column
    private int level;
    //升级经费
    @Column
    private int levelFunds;
    //明星城堡等级要求
    @Column
    private int levelRequirement;
    //建筑描述
    @Column
    private String memo;
    //建筑图标
    @Column
    private String icon;
    //最高等级
    @Column
    private int maxLevel;

    //升级效果
    @Column
    private String levelEffect;

    //银行建筑,经费上限
    @Column
    private int maxFunds;
    //银行建筑 存款上限
    @Column
    private int maxSave;

    //仓库,存储上限
    @Column
    private int maxItem;
    //家族福利
    @Column
    private int boon;

    public int getBoon() {
        return boon;
    }

    public void setBoon(int boon) {
        this.boon = boon;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getLevelEffect() {
        return levelEffect;
    }

    public void setLevelEffect(String levelEffect) {
        this.levelEffect = levelEffect;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String getId() {
        return buildingId;
    }

    @Override
    public void setId(String id) {
        this.buildingId = id;
    }

    @Override
    public String getKey() {
        return buildingId;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelFunds() {
        return levelFunds;
    }

    public void setLevelFunds(int levelFunds) {
        this.levelFunds = levelFunds;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public void setLevelRequirement(int levelRequirement) {
        this.levelRequirement = levelRequirement;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMaxFunds() {
        return maxFunds;
    }

    public void setMaxFunds(int maxFunds) {
        this.maxFunds = maxFunds;
    }

    public int getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(int maxItem) {
        this.maxItem = maxItem;
    }

    public int getMaxSave() {
        return maxSave;
    }

    public void setMaxSave(int maxSave) {
        this.maxSave = maxSave;
    }

    /**
     * 是否可升级
     * @param mainLevel
     * @param gold
     * @return
     */
    public boolean canLevelUp(int mainLevel,int gold){
        if(FamilyConstant.BUILD_CASTLE==type){
            //如果是明星城堡,并且经费足够
            return gold>=this.levelFunds;
        }else{
            //不是明星城堡,并且经费足够
            //如果不是明星城堡
            if (mainLevel >=this.levelRequirement&&gold>=this.levelFunds){
                return true;
            }
        }
        return false;
    }
}
