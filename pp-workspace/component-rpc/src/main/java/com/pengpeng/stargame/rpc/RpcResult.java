package com.pengpeng.stargame.rpc;

import java.io.Serializable;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-4-7下午5:59
 */
public class RpcResult implements Serializable {

    private String code;
    private boolean ok;
    private String msg;

    private String json;

    public RpcResult(){
    }
    public RpcResult(String code,boolean ok,String msg){
        this.code = code;
        this.ok = ok;
        this.msg = msg;
    }
    public RpcResult(String code){
        this.code = code;
        ok = true;
        msg = null;
        json = null;
    }

    public RpcResult(String code, String js) {
        this.code = code;
        this.ok = true;
        this.msg = null;
        this.json = js;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
