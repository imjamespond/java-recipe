package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午2:28
 */
@Desc("任务请求")
public class TaskReq extends BaseReq {
    @Desc("任务id")
    private String id;
    @Desc("1正常完成,2金币完成 3普通领取奖励 4完美领取奖励")
    private int type;
    @Desc("玩家id")
    private String pid;
    @Desc("章节Id")
    private String charpterId;

    @Desc("任务的类型")
    private int taskType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCharpterId() {
        return charpterId;
    }

    public void setCharpterId(String charpterId) {
        this.charpterId = charpterId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

}
