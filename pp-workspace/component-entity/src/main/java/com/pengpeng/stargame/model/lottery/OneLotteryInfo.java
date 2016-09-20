package com.pengpeng.stargame.model.lottery;

import com.pengpeng.stargame.model.Indexable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:38
 */
public class OneLotteryInfo implements Indexable<String> {
    private String pid;
    private String itemId;
    private int num;
    private int gameCoin;
    private Date date;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getKey() {
        return pid;
    }

    @Override
    public void setKey(String key) {
        pid = key;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }
}
