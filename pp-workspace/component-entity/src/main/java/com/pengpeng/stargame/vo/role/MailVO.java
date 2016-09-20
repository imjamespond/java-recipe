package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29上午11:33
 */
@Desc("邮件,公告,事件等")
@EventAnnotation(name="event.mail.new",desc="邮件消息推送")
public class MailVO {
    @Desc("id")
    private String id;
    @Desc("邮件类型:,0普通邮件,1系统公告,2事件")
    private String type;
    @Desc("消息内容")
    private String info;
    @Desc("创建时间")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
