package com.pengpeng.stargame.model.task;

/**
 * User: mql
 * Date: 13-7-19
 * Time: 下午12:14
 */
public class TaskConditionInfo {
    //料件类型
    private int type;
    //关联的id
    private String id;
    //条件值
    private int num;
    //自己的数值
    private int mNum;
    //条件值
    private String tempData;

    public void incrMnum(int num){
        this.mNum+=num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getmNum() {
        return mNum;
    }

    public void setmNum(int mNum) {
        this.mNum = mNum;
    }

    public String getTempData() {
        return tempData;
    }

    public void setTempData(String tempData) {
        this.tempData = tempData;
    }
}
