package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12上午11:10
 */
@Desc("活跃度面板信息")
public class ActiveItemVO {
    @Desc("活跃度类型")
    private int type;
    @Desc("活跃度标题")
    private String title;
    @Desc("完成进度")
    private int finish;
    @Desc("任务总数")
    private int finishMax;
    @Desc("当前活跃度")
    private int activeNum;
    @Desc("最大活跃度")
    private int activeMax;


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

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getFinishMax() {
        return finishMax;
    }

    public void setFinishMax(int finishMax) {
        this.finishMax = finishMax;
    }

    public int getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(int activeNum) {
        this.activeNum = activeNum;
    }

    public int getActiveMax() {
        return activeMax;
    }

    public void setActiveMax(int activeMax) {
        this.activeMax = activeMax;
    }

}
