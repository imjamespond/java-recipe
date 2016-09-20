package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-21下午3:39
 */
@Desc("农场状态信息")
@EventAnnotation(name="event.farm.state",desc="农场状态")
public class FarmStateVO {
    @Desc("pid")
    private String pid;
    @Desc("农场状态:1表示收获,非1表示无状态")
    private String state;
    @Desc("码头状态:1表示可以帮助,非1表示不可以帮助")
    private String wharfState;
    @Desc("摆摊状态状态:1表示有东西出售,非1表示没有")
    private String stallState;
    public FarmStateVO() {
    }

    public FarmStateVO(String pid, String state) {
        this.pid = pid;
        this.state = state;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWharfState() {
        return wharfState;
    }

    public void setWharfState(String wharfState) {
        this.wharfState = wharfState;
    }

    public String getStallState() {
        return stallState;
    }

    public void setStallState(String stallState) {
        this.stallState = stallState;
    }
}
