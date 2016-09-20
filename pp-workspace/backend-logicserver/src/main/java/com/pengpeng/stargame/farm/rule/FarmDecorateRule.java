package com.pengpeng.stargame.farm.rule;

import javax.persistence.*;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午3:17
 */
@Entity
@Table(name = "sg_rule_farm_decorate")
@PrimaryKeyJoinColumn(name="itemsId")
public class FarmDecorateRule extends BaseItemRule {
    @Column
    private String name;
    @Column
    private int decoratetype ;
    @Column
    private String image;

    @Column
    private int ratation;//旋转 0不可  1可

    @Column
    private int remove; //清除 0不可  1可

    @Column
    private int recycle; //回收 0不可  1可

    @Column
    private int move;    //移动 0不可  1可
    @Column
    private int gameCost ;
    @Column
    private int goldCost   ;
    @Column
    private int maxNum;
    @Column
    private String position; //默认的位置

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRatation() {
        return ratation;
    }

    public void setRatation(int ratation) {
        this.ratation = ratation;
    }


    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }

    public int getRecycle() {
        return recycle;
    }

    public void setRecycle(int recycle) {
        this.recycle = recycle;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDecoratetype() {
        return decoratetype;
    }

    public void setDecoratetype(int decoratetype) {
        this.decoratetype = decoratetype;
    }

    public int getGameCost() {
        return gameCost;
    }

    public void setGameCost(int gameCost) {
        this.gameCost = gameCost;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
