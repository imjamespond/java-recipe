package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 下午3:41
 */
@Desc("掉下来钱的VO")
public class MoneyPickInfoVO {
    @Desc("唯一Id  捡钱的时候需要传给后台")
    private String id;
    @Desc("面额")
    private int money;
    @Desc("位置")
    private String position;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
