package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 活跃度积分
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12下午12:10
 */
@Desc("活跃度积分领取")
public class ActiveAwardVO {
    @Desc("活跃度")
    private int active;
    @Desc("积分")
    private int score;
    @Desc("状态:0未完成,1已完成,2已领取")
    private int status;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
