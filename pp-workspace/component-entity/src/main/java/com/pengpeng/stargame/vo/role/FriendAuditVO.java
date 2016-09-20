package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-2下午10:02
 */
@Desc("申请好友提醒")
@EventAnnotation(name="event.friend.audit",desc="好友导入审核")
public class FriendAuditVO {
    @Desc("好友id")
    private String pid;
    @Desc("名称")
    private String name;
    @Desc("用户主站头像")
    private String portrait ;

    @Desc("状态:0待审核,1审核通过")
    private int status;
    public FriendAuditVO() {
    }

    public FriendAuditVO(String pid, String name,String portrait) {
        this.pid = pid;
        this.name = name;
        this.status = 0;
        this.portrait=portrait;
    }

    public FriendAuditVO(String pid, String name,int status,String portrait) {
        this.pid = pid;
        this.name = name;
        this.status = status;
        this.portrait=portrait;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
