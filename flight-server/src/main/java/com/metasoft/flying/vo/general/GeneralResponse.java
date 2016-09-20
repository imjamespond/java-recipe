package com.metasoft.flying.vo.general;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;

@DescAnno("基本消息")
public class GeneralResponse {
	//private static Logger logger = LoggerFactory.getLogger(GeneralResponse.class);
	@DescAnno("状态")
	private boolean ok;
	@DescAnno("错误号")
	private int error;
	@DescAnno("回调序列号")
	private int serial;
	@DescAnno("时间")
	private long time;
	@DescAnno("事件代码")
	private String code;
	@DescAnno("消息")
	private String msg;
	@DescAnno("数据")
	private Object data;
	
	public GeneralResponse(){
		ok = true;
		msg = null;
		time=System.currentTimeMillis();
	}
	public GeneralResponse(String msg, int error){
		this.ok = false;
		this.error = error;
		this.msg = msg;
		this.code = "event.error";
		this.time=System.currentTimeMillis();
	}
	
	public GeneralResponse(boolean ok,String msg){
		this.ok = ok;
		this.msg = msg;
		this.time=System.currentTimeMillis();
	}
	public GeneralResponse(Object data,String code){
		ok=true;
		this.time=System.currentTimeMillis();
		this.code = code;
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

	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}

    public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public static GeneralResponse newOK(){
        return new GeneralResponse();
    }

    public static GeneralResponse newError(String msg,int error){
    	LocalizationService localService = SpringService.getBean(LocalizationService.class);  	
        return new GeneralResponse(localService.getLocalString(msg),error);
    }
    public static GeneralResponse newObject(Object obj){	
		EventAnno anno = obj.getClass().getAnnotation(EventAnno.class);
		if (anno == null) {
			return new GeneralResponse(obj,"");
		}
        return new GeneralResponse(obj,anno.name());
    }
}
