package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("邮件请求")
public class MailReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("邮件id")
    private long mailId;
    @Desc("邮件类型 0为普通 1为系统公告")
    private int type;
    @Desc("邮件标题")
    private String title;
    @Desc("邮件内容")
    private String content;
    @Desc("邮件附件")
    private String attachments;
    @Desc("游戏币")
    private int gameCoin;
    @Desc("达人币")
    private int goldCoin;
    @Desc("农场经验")
    private int farmExp;
    @Desc("积分途径 后台用")
    private int integralType;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getIntegralType() {
        return integralType;
    }

    public void setIntegralType(int integralType) {
        this.integralType = integralType;
    }
}
