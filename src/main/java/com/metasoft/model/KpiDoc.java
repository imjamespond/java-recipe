package com.metasoft.model;

public class KpiDoc {
	
	protected String ind_disname; //指标名称
	protected String ind_bussinessBelong; //业务归属
	protected String ind_businessDefine; //业务定义
	protected String ind_descr; //口径描述
	protected String ind_unit;  //单位
	
	protected String resPath; //路径
	
	public KpiDoc(String ind_disname, String ind_bussinessBelong, String ind_bussinessDefine, 
			String ind_descr, String ind_unit, String resPath) {
		this.ind_disname = ind_disname;
		this.ind_bussinessBelong = ind_bussinessBelong;
		this.ind_businessDefine = ind_bussinessDefine;
		this.ind_descr = ind_descr;
		this.ind_unit = ind_unit;
		
		this.resPath = resPath;
	}
	
	public String toString() {
		return "业务定义:" + ind_businessDefine + " " 
				+ "业务归属:" + ind_bussinessBelong + " "
				+ "口径描述:" + ind_descr + " "
				+ "单位:" + ind_unit;
	}
}
