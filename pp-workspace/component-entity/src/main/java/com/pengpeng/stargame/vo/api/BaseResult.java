package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-8-15
 * Time: 下午2:16
 */
public class BaseResult {
    @Desc("状态200成功   201 失败  202 玩家未创建角色")
    protected int status=200;
    @Desc("失败消息")
    protected String msg;


    public    BaseResult(int status,String msg){
        this.status=status;
        this.msg=msg;

    }

    public BaseResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
