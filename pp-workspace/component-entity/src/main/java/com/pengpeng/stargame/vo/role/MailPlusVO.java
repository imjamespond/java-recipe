package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseRewardVO;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29上午11:33
 */
@Desc("邮件")
public class MailPlusVO {
    @Desc("id")
    private long id;
    @Desc("邮件类型:,0普通邮件,1系统")
    private int type;
    @Desc("邮件标题")
    private String title;
    @Desc("邮件内容")
    private String content;
    @Desc("附件")
    private BaseRewardVO[] attachmentVOList;
    @Desc("是否已领取")
    private boolean accepted;
    @Desc("创建时间")
    private long createDate;
    @Desc("剩余时间")
    private long restTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BaseRewardVO[] getAttachmentVOList() {
        return attachmentVOList;
    }

    public void setAttachmentVOList(BaseRewardVO[] attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getRestTime() {
        return restTime;
    }

    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }
}
