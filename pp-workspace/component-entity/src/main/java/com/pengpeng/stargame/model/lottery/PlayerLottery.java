package com.pengpeng.stargame.model.lottery;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.vo.BaseRewardVO;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerLottery extends BaseEntity<String> {
    private String pid;
    private int num;
    private Date refreshTime;
    private int rouletteNum;//轮盘免费次数
    private int rouletteExp;//经验为10加一次免费
    private Date rRefreshTime;//刷新时间
    private BaseRewardVO rReward;//奖品
    private int rouletteSpeaker;//奖品广播
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getRouletteNum() {
        return rouletteNum;
    }

    public void setRouletteNum(int rouletteNum) {
        this.rouletteNum = rouletteNum;
    }

    public int getRouletteExp() {
        return rouletteExp;
    }

    public void setRouletteExp(int rouletteExp) {
        this.rouletteExp = rouletteExp;
    }

    public Date getrRefreshTime() {
        return rRefreshTime;
    }

    public void setrRefreshTime(Date rRefreshTime) {
        this.rRefreshTime = rRefreshTime;
    }

    public BaseRewardVO getrReward() {
        return rReward;
    }

    public int getRouletteSpeaker() {
        return rouletteSpeaker;
    }

    public void setRouletteSpeaker(int rouletteSpeaker) {
        this.rouletteSpeaker = rouletteSpeaker;
    }

    public void setrReward(BaseRewardVO rReward) {
        this.rReward = rReward;
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

    public void deNum(int num){
        this.num-=num;
    }
}
