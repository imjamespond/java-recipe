package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12上午11:10
 */
@Desc("活跃度面板信息")
public class ActiveVO {
    @Desc("简介")
    private String title;
    @Desc("活跃度列表")
    private ActiveItemVO[] items;

    @Desc("领取状态:key:活跃度值,value=是否已领取;例如25活跃度")
    private ActiveAwardVO[] awards;
    @Desc("今日活跃度")
    private int dayActive;
    @Desc("完成数量")
    private int finished;

    public ActiveItemVO[] getItems() {
        return items;
    }

    public void setItems(ActiveItemVO[] items) {
        this.items = items;
    }

    public ActiveAwardVO[] getAwards() {
        return awards;
    }

    public void setAwards(ActiveAwardVO[] awards) {
        this.awards = awards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDayActive() {
        return dayActive;
    }

    public void setDayActive(int dayActive) {
        this.dayActive = dayActive;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
