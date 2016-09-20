package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;
import org.apache.commons.collections.list.AbstractLinkedList;

/**
 * @auther james@pengpeng.com
 * @since: 13-10-15上午10:27
 */
@Desc("消息")
public class MsgReq {

    public static final int ALL = 1;
    public static final int SINGLE = 2;
    public static final int FAMILY = 3;

    private String id;//pid 或 familyId
    private String msg;
    private int type;

    public MsgReq() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
