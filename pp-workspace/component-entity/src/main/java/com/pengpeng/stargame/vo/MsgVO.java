package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-28下午12:13
 */
@Desc("邮件提示VO")
@EventAnnotation(name="event.newmsg",desc="新邮件提示")
public class MsgVO {
    @Desc("消息类型:1游戏币变化,2达人币数量变化,3新邮件提示,4每日活跃度;5音乐先锋;6免费抽奖次数;7任务变化,8,农场礼物赠送,9时装礼物赠送" +
            "10连续登录  11圣诞礼包领取  12元旦礼包 13家族银行活动 14春节礼包 16新的玩家邮件 17、五一礼包 18、积分更改 19、成就有变化 20、轮盘")
    private int type;
    @Desc("数量")
    private int count;

    public MsgVO() {
    }

    public MsgVO(int count) {
        this.count = count;
    }
    public MsgVO(int type,int count) {
        this.type = type;
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
