package com.pengpeng.stargame.vo.role;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午4:07
 */
public class TimeReq {
    private String pid;
    private int uid;
    private long time;

    public TimeReq() {
    }

    public TimeReq(String pid, int uid, long time) {
        this.pid = pid;
        this.uid = uid;
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
