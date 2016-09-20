package com.pengpeng.stargame.vo.farm.process;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午12:24
 */
@Desc("农场加工请求")
public class FarmProcessReq extends BaseReq {
    @Desc("请求需要的人的队列信息")
    private String pid;
    @Desc("要加工的 Id 数组")
    private String [] processId;
    @Desc("类型 1表示食品，2表示工艺，3表示其他")
    private int type;
    @Desc("队列的唯一Id，取消队列的时候 传")
    private String id;
    @Desc("1 表示完成所有队列需要的达人币 2表示请求完成单个队列需要的达人币")
    private int goldType;


    public String[] getProcessId() {
        return processId;
    }

    public void setProcessId(String[] processId) {
        this.processId = processId;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getGoldType() {
        return goldType;
    }

    public void setGoldType(int goldType) {
        this.goldType = goldType;
    }
}
