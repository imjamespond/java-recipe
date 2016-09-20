package com.pengpeng.stargame.model.task;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * 用户任务数据
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午2:42
 */
public class TaskPlayer extends BaseEntity<String> {
    private String pid;
    private Set<String> finishs;
    private Set<String> familyFinishs;
    private HashMap<String,OneTask> accepts;
    private Date nextTime;
    private String chaptersId;//玩家正在进行的章节


    public TaskPlayer(){
        finishs = new HashSet<String>();
        familyFinishs = new HashSet<String>();
        accepts = new HashMap<String,OneTask>();

    }

    public boolean isHasTask(String taskId){
        Collection<OneTask> oneTaskList= accepts.values();

        for(OneTask oneTask:oneTaskList){
            if(oneTask.getTaskId().equals(taskId)){
                return true;
            }
        }
        return false;
    }

    public OneTask getOneTask(String taskId){
        return accepts.get(taskId);
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public void removeTask(String  taskId){
        accepts.remove(taskId);
    }
    public TaskPlayer(String index) {
        finishs = new HashSet<String>();
        accepts = new HashMap<String,OneTask>();
        pid = index;
        nextTime=new Date();
        familyFinishs=new HashSet<String>();
    }
    public void addOneTask(OneTask oneTask){
        this.accepts.put(oneTask.getTaskId(),oneTask);
    }

    public Collection<OneTask> getAllTasks(){
        return accepts.values();

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = id;
    }

    @Override
    public String getKey() {
        return pid;
    }


    public boolean isFinished(String taskId){
        if(familyFinishs.contains(taskId)){
            return true;
        }
        if(finishs.contains(taskId)){
            return true;
        }
        return false;
    }

    public void addFinished(String taskId){
        finishs.add(taskId);
    }



    public void addFamilyFinishs(String taskId){
        familyFinishs.add(taskId);
    }

    public  void clearFamilyFinishTask(){
        familyFinishs.clear();
    }


    public Set<String> getFinished(){
        return finishs;
    }

    public String getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(String chaptersId) {
        this.chaptersId = chaptersId;
    }
}
