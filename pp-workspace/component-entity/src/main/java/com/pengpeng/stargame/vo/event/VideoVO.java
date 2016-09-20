package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-5
 * Time: 下午4:13
 */
@Desc("单个视频VO")
public class VideoVO {
    @Desc("单个视频VO")
    private Integer id;
    @Desc("明星id")
    private Integer starId;//明星id
    @Desc("明星名称")
    private String starName;//明星名称
    @Desc("明星名称")
    private String title;//标题
    @Desc("开放时间")
    private Long openingTimeL;//开放时间
    @Desc("直播时间")
    private Long liveTimeL;//直播时间
    @Desc("视频封面")
    private String coverUrl;//视频封面
    @Desc("/视频直播路径")
    private String videoLiveUrl;//视频直播路径
    @Desc("粉丝达人用户购买验证码价格")
    private Integer istarUserPrice;//粉丝达人用户购买验证码价格
    @Desc("是否需要验证吗")
    private Integer needCaptcha;//是否需要验证吗
    @Desc("当前用户是否购买验证码")
    private boolean buy;//当前用户是否购买验证码
    @Desc("视频类型 1：专访  2：活动")
    private int liveType;
    @Desc("视频关键字")
    private String keyWord;
    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStarId() {
        return this.starId;
    }

    public void setStarId(Integer starId) {
        this.starId = starId;
    }

    public String getStarName() {
        return this.starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getVideoLiveUrl() {
        return this.videoLiveUrl;
    }

    public void setVideoLiveUrl(String videoLiveUrl) {
        this.videoLiveUrl = videoLiveUrl;
    }



    public Integer getIstarUserPrice() {
        return istarUserPrice;
    }

    public void setIstarUserPrice(Integer istarUserPrice) {
        this.istarUserPrice = istarUserPrice;
    }





    public Integer getNeedCaptcha() {
        return needCaptcha;
    }

    public void setNeedCaptcha(Integer needCaptcha) {
        this.needCaptcha = needCaptcha;
    }

    public Long getOpeningTimeL() {
        return openingTimeL;
    }

    public void setOpeningTimeL(Long openingTimeL) {
        this.openingTimeL = openingTimeL;
    }

    public Long getLiveTimeL() {
        return liveTimeL;
    }

    public void setLiveTimeL(Long liveTimeL) {
        this.liveTimeL = liveTimeL;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}


