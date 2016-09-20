package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-2-24
 * Time: 下午6:05
 */
@Desc("视频直播活动统计")
public class VideoReq {
    @Desc("家族Id")
    private String fId;
    @Desc("道具Id")
    private String itemId;
    @Desc("当前页数")
    private int pageNo;
    @Desc("每页多少")
    private int size;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
