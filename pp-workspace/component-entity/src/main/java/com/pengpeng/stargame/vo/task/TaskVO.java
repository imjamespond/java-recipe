package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午2:32
 */
@Desc("任务对象")
public class TaskVO {
    @Desc("任务id")
    private String id;
    @Desc("任务类型")
    private int type;
    @Desc("任务名称")
    private String name;
    @Desc("任务状态:0未完成,1:已完成")
    private int state;
    @Desc("详细说明")
    private String memo;
    @Desc("任务图标")
    private String icon;
    @Desc("链接数据")
    private String link;
    @Desc("链接描述")
    private String linkDesc;

    @Desc("使用立即完成功能，需要消耗的达人币，该字段为0，表示无此功能")
    private int gold;
    @Desc("是否是新手任务  1 是新手任务  0 不是")
    private int newTask;
    @Desc("显示状态 0没有 1新任务  2任务数据有更改")
    private int newstate;

    @Desc("任务奖励")
    private TaskRewardVO[] rewards;
    @Desc("任务条件")
    private TaskConditionVO[] conditions;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public TaskRewardVO[] getRewards() {
        return rewards;
    }

    public void setRewards(TaskRewardVO[] rewards) {
        this.rewards = rewards;
    }

    public TaskConditionVO[] getConditions() {
        return conditions;
    }

    public void setConditions(TaskConditionVO[] conditions) {
        this.conditions = conditions;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public int getNewTask() {
        return newTask;
    }

    public void setNewTask(int newTask) {
        this.newTask = newTask;
    }

    public int getNewstate() {
        return newstate;
    }

    public void setNewstate(int newstate) {
        this.newstate = newstate;
    }
}
