
package com.pengpeng.stargame.vo.task;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午2:50
 */
@Desc("任务条件VO")
public class TaskConditionVO {
    @Desc("关联的id  ")
    private String id;
    @Desc("条件类型,1:npc对话,2:道具,3:农场等级,4:房间豪华度,5:时尚指数,6:商业等级")
    private int type;
    @Desc("条件值,有些条件无数值")
    private int num;
    @Desc("自己已经达到的数值")
    private int mNum;
    @Desc("任务条件的描述")
    private String des;

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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

