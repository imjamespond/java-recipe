package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.Indexable;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午3:14
 */
public class StarGift implements Indexable<String> {
    private String pid;  //赠送的玩家Id
    private String id;   //礼物的I的
    private String words; // 赠言
    private int num;//数量
    private int eventStart;//是否是活动期间赠送
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getEventStart() {
        return eventStart;
    }

    public void setEventStart(int eventStart) {
        this.eventStart = eventStart;
    }

    @Override
    public String getKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setKey(String key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
