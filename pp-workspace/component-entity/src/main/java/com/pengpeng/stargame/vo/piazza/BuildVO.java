package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 上午11:54
 */
@Desc("单个建筑VO")
public class BuildVO {
    @Desc("建筑名称")
    private String name;
    @Desc("建筑的类型")
    private int type;
    @Desc("建筑的类型")
    private int level;
    @Desc("主城等级")
    private int levelRequirement;
    @Desc("升级经费")
    private int funds;
    @Desc("建筑描述")
    private String memo;
    @Desc("图标")
    private String icon;
    @Desc("升级效果")
    private String levelEffect;
    @Desc("最高等级")
    private int maxLevel;


    public BuildVO() {

    }

    public BuildVO(int type, int level) {
        this.type = type;
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelEffect() {
        return levelEffect;
    }

    public void setLevelEffect(String levelEffect) {
        this.levelEffect = levelEffect;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public void setLevelRequirement(int mainLevel) {
        this.levelRequirement = mainLevel;
    }

    public int getFunds() {
        return funds;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
}
