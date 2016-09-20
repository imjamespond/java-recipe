package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午4:57
 */
public class MailLog {
    private Boolean deleted;
    private Boolean accepted;

    public MailLog() {

    }
    public MailLog(Boolean deleted, Boolean accepted) {
        this.deleted = deleted;
        this.accepted = accepted;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
