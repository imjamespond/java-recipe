package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-3-5
 * Time: 下午4:11
 */
@Desc("视频直播活动面板VO")
public class VideoPanelVO {
    @Desc("天线数量")
    public int num;
    @Desc("当前页数")
    public int pageNo;
    @Desc("最大页数")
    private int maxPage;
    @Desc("视频直播数据")
    private VideoVO[] videoVOs;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public VideoVO[] getVideoVOs() {
        return videoVOs;
    }

    public void setVideoVOs(VideoVO[] videoVOs) {
        this.videoVOs = videoVOs;
    }
}
