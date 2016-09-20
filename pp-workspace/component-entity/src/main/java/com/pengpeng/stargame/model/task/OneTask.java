package com.pengpeng.stargame.model.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * User: mql
 * Date: 13-7-19
 * Time: 上午11:40
 */
public class OneTask {
    private String taskId;
    //0进行中 1完成
    private int status;

    private List<TaskConditionInfo> taskConditionInfoList;


    public OneTask(){
           }

    public OneTask(String taskId){
        this.taskId=taskId;
        status =0;
        taskConditionInfoList = new ArrayList<TaskConditionInfo>();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TaskConditionInfo> getTaskConditionInfoList() {
        return taskConditionInfoList;
    }

    public void setTaskConditionInfoList(List<TaskConditionInfo> taskConditionInfoList) {
        this.taskConditionInfoList = taskConditionInfoList;
    }

    public void addConditon(TaskConditionInfo taskConditionInfo){
        this.taskConditionInfoList.add(taskConditionInfo);
    }
}
