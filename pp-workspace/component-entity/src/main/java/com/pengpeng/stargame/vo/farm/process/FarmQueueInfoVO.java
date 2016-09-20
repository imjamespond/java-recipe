package com.pengpeng.stargame.vo.farm.process;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午3:13
 */
@Desc("农场加工 队列信息VO")
public class FarmQueueInfoVO {
    @Desc("已经开了的队列格子数量")
    private int queueNum;
    @Desc("所有生成队列 总时间，秒数，如果是0 表示没有生成队列")
    private int allTime;
    @Desc("开通一个格子 需要的 达人币数量")
    private int gold;
    @Desc("生产队列列表 FarmOneQueueVO数组")
    private FarmOneQueueVO [] farmOneQueueVOs;
    @Desc("加速功能剩余时间 ，如果为0，表示玩家没有开启加速，倒计时结束，需要发请求")
    private long speedTime;

    public int getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(int queueNum) {
        this.queueNum = queueNum;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public FarmOneQueueVO[] getFarmOneQueueVOs() {
        return farmOneQueueVOs;
    }

    public void setFarmOneQueueVOs(FarmOneQueueVO[] farmOneQueueVOs) {
        this.farmOneQueueVOs = farmOneQueueVOs;
    }

    public long getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(long speedTime) {
        this.speedTime = speedTime;
    }
}
