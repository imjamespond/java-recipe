package com.pengpeng.stargame.model.piazza;

/**
 * User: mql
 * Date: 13-7-1
 * Time: 上午9:44
 */
public class MoneyPick {
    private String id;
    private int money;
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
