package com.pengpeng.stargame.vo.farm.process;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午3:19
 */
@Desc("单个队列VO")
public class FarmOneQueueVO {
    @Desc("唯一Id")
    private String id;
    @Desc("状态  1新加 2进行中 3完成")
    private int status;
    @Desc("此生产队列需要的时间 单位秒 ")
    private int time;
    @Desc("此生产队列已经进行了多少秒 当队列状态是2的时候 有值 ，当正在进行的队列，完成了，需要客户端发个 请求队列信息的消息")
    private int proceedTime;
    @Desc("生产出来的物品id  ")
    private String itmeId;
    @Desc("生成出来的 物品的图标 ")
    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getProceedTime() {
        return proceedTime;
    }

    public void setProceedTime(int proceedTime) {
        this.proceedTime = proceedTime;
    }

    public String getItmeId() {
        return itmeId;
    }

    public void setItmeId(String itmeId) {
        this.itmeId = itmeId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
