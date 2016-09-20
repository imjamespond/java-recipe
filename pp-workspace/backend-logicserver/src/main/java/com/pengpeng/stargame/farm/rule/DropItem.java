package com.pengpeng.stargame.farm.rule;

/**
 * User: mql
 * Date: 13-9-12
 * Time: 下午3:36
 */
public class DropItem {
    private String itemId;
    private int num;
    private int probability;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
