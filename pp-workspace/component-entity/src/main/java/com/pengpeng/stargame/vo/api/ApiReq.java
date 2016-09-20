package com.pengpeng.stargame.vo.api;

import com.pengpeng.stargame.vo.role.ChargeReq;

/**
 * User: mql
 * Date: 13-8-15
 * Time: 下午2:33
 */

public class ApiReq  {

    private int uid;


    public ApiReq(){

    }
    public ApiReq(int uid){
        this.uid=uid;

    }
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
