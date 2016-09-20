package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-8-6
 * Time: 上午10:32
 */
@Desc("任务章节奖励 会广播")
@EventAnnotation(name="task.chaptersReward",desc="任务章节奖励广播")
public class ChaptersRewardVO {
    @Desc("章节Id")
    private String chaptersId;
    @Desc("章节名字")
    private String chaptersName;
    @Desc("奖励 TaskRewardVO 数组 ")
    private TaskRewardVO[] rewards;


    public TaskRewardVO[] getRewards() {
        return rewards;
    }

    public void setRewards(TaskRewardVO[] rewards) {
        this.rewards = rewards;
    }

    public String getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(String chaptersId) {
        this.chaptersId = chaptersId;
    }

    public String getChaptersName() {
        return chaptersName;
    }

    public void setChaptersName(String chaptersName) {
        this.chaptersName = chaptersName;
    }
}
