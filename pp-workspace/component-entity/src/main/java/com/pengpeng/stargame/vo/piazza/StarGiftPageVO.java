package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午2:44
 */
@Desc("明星礼面板VO")
public class StarGiftPageVO {
    @Desc("明星名字")
    private String star;
    @Desc("明星粉丝值")
    private int   fansValues;
    @Desc("起始页")
    private Integer pageNo;
    @Desc("最大页数")
    private Integer maxPage;
    @Desc("礼物信息列表  StarGiftVo 数组")
    private StarGiftVO [] giftVOs;
    @Desc("明星粉丝值排行前三 StarRankVO 数组")
    private StarRankVO [] topRank;

    @Desc("自己明星的排行 StarRankVO 数组")
    private StarRankVO [] myRank;
    @Desc("1表示送礼物掉落 粉丝值增加的 活动开始")
    private int eventStart;

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getFansValues() {
        return fansValues;
    }

    public void setFansValues(int fansValues) {
        this.fansValues = fansValues;
    }

    public StarGiftVO[] getGiftVOs() {
        return giftVOs;
    }

    public void setGiftVOs(StarGiftVO[] giftVOs) {
        this.giftVOs = giftVOs;
    }

    public StarRankVO[] getTopRank() {
        return topRank;
    }

    public void setTopRank(StarRankVO[] topRank) {
        this.topRank = topRank;
    }

    public StarRankVO[] getMyRank() {
        return myRank;
    }

    public void setMyRank(StarRankVO[] myRank) {
        this.myRank = myRank;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public int getEventStart() {
        return eventStart;
    }

    public void setEventStart(int eventStart) {
        this.eventStart = eventStart;
    }
}
