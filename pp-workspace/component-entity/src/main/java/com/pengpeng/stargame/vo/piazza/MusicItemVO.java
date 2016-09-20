package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-4上午10:45
 */
@Desc("音乐盒音乐")
public class MusicItemVO {
    @Desc("榜单(音乐盒)id")
    private Integer bid;
    @Desc("歌曲id")
    private Integer id;
    @Desc("歌曲标题")
    private String title;
    @Desc("歌曲地址")
    private String url;
    @Desc("歌手名称")
    private String singerName;

    @Desc("票数")
    private int votes;

    private int year;
    private int issue;

    /**
     * IN乐榜榜单Id
     */
//    private Integer songListId;
    /**
     * 歌曲ID
     */
//    private Integer songId;
    /**
     * 歌曲名称
     */
//    private String songName;
    /**
     * In乐榜榜单歌曲记录ID
     */
//    private Integer songListItemId;
    /**
     * 歌手ID
     */
//    private Integer singerId;
    /**
     * 歌手名称
     */
//    private String singerName;

    public MusicItemVO() {
    }

    public MusicItemVO(Integer bid,Integer id, String title, String url,String singerName) {
        this.bid = bid;
        this.id = id;
        this.title = title;
        this.url = url;
        this.singerName= singerName;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }
}
