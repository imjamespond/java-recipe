package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-13上午11:45
 */
@Entity
@Table(name="sg_rule_active")
public class ActiveRule extends BaseEntity<Integer> {
    //活跃度类型
    @Id
    @Column
    private int type;
    //标题
    @Column
    private String title;
    //最大进度
    @Column
    private int finishMax;
    //活跃值
    @Column
    private int active;

    //最大活跃值
    @Column
    private int activeMax;
    //是否显示  1显示  0不显示
    @Column
    private int display;

    //关联任务Id
    @Column
    private String taskId;
    //排序 值  ，升序排列
    @Column
    private int sort;

    @Override
    public Integer getId() {
        return type;
    }

    @Override
    public void setId(Integer id) {
        this.type = id;
    }

    @Override
    public Integer getKey() {
        return type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFinishMax() {
        return finishMax;
    }

    public void setFinishMax(int finishMax) {
        this.finishMax = finishMax;
    }

    public int getActiveMax() {
        return activeMax;
    }

    public void setActiveMax(int activeMax) {
        this.activeMax = activeMax;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
