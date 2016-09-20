package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-8-2
 * Time: 上午10:32
 */
@Desc("章节信息VO")
public class ChaptersInfoVO {

    @Desc("正在进行的章节Id")
    private String chaptersId;
    @Desc("正在进行的章节名字")
    private String chaptersName;
    @Desc("任务列表  TaskVO 数组")
    private TaskVO [] taskVOs;

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
}
