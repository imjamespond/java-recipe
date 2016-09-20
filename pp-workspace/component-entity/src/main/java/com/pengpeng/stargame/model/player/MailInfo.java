package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:00
 */
public class MailInfo  {

    private long counter;
    private String title;
    private String content;
    private List<MailAttachment> attachments;
    private Date createDate;

    private int accepted;//1已领取

    private int integralType;//冗余属性，记录积分获取类型用到

    public MailInfo() {
        createDate = new Date();
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MailAttachment> attachments) {
        this.attachments = attachments;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
    public boolean accepted() {
        return accepted>0;
    }

    public int getIntegralType() {
        return integralType;
    }

    public void setIntegralType(int integralType) {
        this.integralType = integralType;
    }
}
