package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午11:13
 */
@Desc("活动请求")
public class EventReq extends BaseReq {
    @Desc("活动捡钱礼物时候的 唯一Id")
    private String id;
    @Desc("家族银行活动 领取 哪个存款额度的 奖励")
    private String  gameMoney;
    @Desc("家族ID")
    private String familyId;
    @Desc("放气球时候的 留言")
    private String word;
    @Desc("多少时间 单位分钟")
    private int minutes;

    @Desc("类型 视频直播活动获取列表的时候用  1表示正在直播的  2表示过往的")
    private int type;
    @Desc("视频的Id，兑换的时候用")
    private int vedeoId;
    @Desc("查询过往视频直播列表 传的页数")
    private int pageNo;

    @Desc("放气球的时候 位置")
    private String position;

    public String getGameMoney() {
        return gameMoney;
    }

    public void setGameMoney(String gameMoney) {
        this.gameMoney = gameMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVedeoId() {
        return vedeoId;
    }

    public void setVedeoId(int vedeoId) {
        this.vedeoId = vedeoId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
