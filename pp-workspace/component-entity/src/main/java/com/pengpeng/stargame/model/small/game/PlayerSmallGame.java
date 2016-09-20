package com.pengpeng.stargame.model.small.game;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerSmallGame extends BaseEntity<String> {
    private String pid;
    private Date lastLoginTime;
    private long verifyTime;
    private Map<Integer,Integer> freeTime;
    private Map<Integer,Integer> goldTime;
    private Map<Integer,Integer> score;
    private Map<Integer,Integer> scoreWeek;
    private Map<Integer,Date> buyMap;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Map<Integer, Integer> getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(Map<Integer, Integer> freeTime) {
        this.freeTime = freeTime;
    }

    public Map<Integer, Integer> getGoldTime() {
        return goldTime;
    }

    public void setGoldTime(Map<Integer, Integer> goldTime) {
        this.goldTime = goldTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Map<Integer, Integer> getScore() {
        return score;
    }

    public void setScore(Map<Integer, Integer> score) {
        this.score = score;
    }

    public Map<Integer, Integer> getScoreWeek() {
        return scoreWeek;
    }

    public void setScoreWeek(Map<Integer, Integer> scoreWeek) {
        this.scoreWeek = scoreWeek;
    }

    public Map<Integer, Date> getBuyMap() {
        return buyMap;
    }

    public void setBuyMap(Map<Integer, Date> buyMap) {
        this.buyMap = buyMap;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }
}
