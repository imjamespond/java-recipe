package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-12-6
 * Time: 下午4:27
 */
@Desc("活动VO")
@EventAnnotation(name="event.event.update",desc="活动推送VO")
public class EventVO {
    @Desc("1完成了圣诞任务  2完成了元旦任务")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
