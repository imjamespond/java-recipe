package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-26
 * Time: 下午5:18
 */
@Desc("家族单个成员VO")
public class FamilyMemberVO {
    @Desc("玩家Pid")
    private String pid;
    @Desc("玩家名字")
    private String name;
    @Desc("个人总贡献")
    private int  contribution;
    @Desc("家族福利")
    private  int welfare;
    @Desc("家族身份  1明星   2明星助理  3超级粉丝  4粉丝")
    private  int  identity;
    @Desc("玩家职业")
    private int profession;
    @Desc("玩家是否在线  0不在线   1在线")
    private  int lineStatus;
    @Desc("个人日贡献")
    private int  dayContribution;
    @Desc("个人周贡献")
    private int  weekContribution;
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getWelfare() {
        return welfare;
    }

    public void setWelfare(int welfare) {
        this.welfare = welfare;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public int getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(int lineStatus) {
        this.lineStatus = lineStatus;
    }

    public int getDayContribution() {
        return dayContribution;
    }

    public void setDayContribution(int dayContribution) {
        this.dayContribution = dayContribution;
    }

    public int getWeekContribution() {
        return weekContribution;
    }

    public void setWeekContribution(int weekContribution) {
        this.weekContribution = weekContribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
