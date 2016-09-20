package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:44
 */
@Desc("玩家的装扮VO")
@EventAnnotation(name="event.fashion.update",desc="玩家装扮数据更新")
public class PlayerFashionVO {
    private String pid;
    @Desc("玩家身上穿的装饰")
    private FashionVO [] fashionVOs;
    @Desc("1头顶圣诞 2头顶元旦 3音乐小宠")
    private int status;
    @Desc("称号")
    private String title;
    public PlayerFashionVO() {
    }

    public PlayerFashionVO(String pid) {
        this.pid = pid;
    }

    public FashionVO[] getFashionVOs() {
        return fashionVOs;
    }

    public void setFashionVOs(FashionVO[] fashionVOs) {
        this.fashionVOs = fashionVOs;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
