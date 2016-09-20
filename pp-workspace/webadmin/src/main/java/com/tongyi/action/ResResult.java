package com.tongyi.action;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-24下午5:06
 */
public class ResResult {
    private String code;
    private String message;

    public ResResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static ResResult newOk(){
        return new ResResult("ok","");
    }

    public static ResResult newFailed(){
        return new ResResult("failed","");
    }
}
