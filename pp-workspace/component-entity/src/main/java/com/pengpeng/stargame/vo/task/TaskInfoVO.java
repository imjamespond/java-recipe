package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-7-19
 * Time: 下午5:07
 */
@Desc("任务面板VO 会广播")
@EventAnnotation(name="task.update",desc="任务更改广播")
public class TaskInfoVO {
    @Desc("是否有新任务出现 0无  1有")
    private int hasnew;
    @Desc("正在进行的章节Id")
    private String chaptersId;
    @Desc("正在进行的章节名字")
    private String chaptersName;
    @Desc("任务列表  TaskVO 数组")
    private TaskVO [] taskVOs;
    @Desc("完美领取 增加的 百分比")
    private int addP;
    @Desc("完美领取 需要的达人币")
    private int needGold;
    public int getHasnew() {
        return hasnew;
    }

    public void setHasnew(int hasnew) {
        this.hasnew = hasnew;
    }

    public TaskVO[] getTaskVOs() {
        return taskVOs;
    }

    public void setTaskVOs(TaskVO[] taskVOs) {
        this.taskVOs = taskVOs;
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

    public int getAddP() {
        return addP;
    }

    public void setAddP(int addP) {
        this.addP = addP;
    }

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }
}
