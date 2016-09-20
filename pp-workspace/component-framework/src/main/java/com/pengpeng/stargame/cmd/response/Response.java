package com.pengpeng.stargame.cmd.response;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Response {
	private String code;
	private boolean ok;
	private String msg;
	
	private Object data;
	
	public Response(){
		ok = true;
		msg = null;
	}
	public Response(String msg){
		this.ok = false;
		this.msg = msg;
	}
	
	public Response(boolean ok,String msg){
		this.ok = ok;
		this.msg = msg;
	}
	public Response(Object data){
		ok=true;
		this.data = data;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

    public static Response newOK(){
        return new Response(true,"");
    }
    public static Response newOK(String msg){
        return new Response(true,msg);
    }
    public static Response newError(String msg){
        return new Response(false,msg);
    }
    public static Response newObject(Object obj){
        return new Response(obj);
    }
    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
    }
}
