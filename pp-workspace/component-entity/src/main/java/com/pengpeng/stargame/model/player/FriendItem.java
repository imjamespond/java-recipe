package com.pengpeng.stargame.model.player;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-27下午3:53
 */
public class FriendItem {

    private String fid;//好友id

    private Date createTime;// 创建时间
    //只做排序用(农场等级),没实际意义
    private int sort;
    //只做排序用(农场农场经验),没实际意义
    private int sort1;
    //只做排序用登陆时间
    private int sort2;
    //只做排序置顶
    private int sort3;
    public FriendItem(){

    }
    public FriendItem(String fId, Date date) {
        this.fid = fId;
        this.createTime = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort1() {
        return sort1;
    }

    public void setSort1(int sort1) {
        this.sort1 = sort1;
    }

    public int getSort2() {
        return sort2;
    }

    public void setSort2(int sort2) {
        this.sort2 = sort2;
    }

    public int getSort3() {
        return sort3;
    }

    public void setSort3(int sort3) {
        this.sort3 = sort3;
    }
}
