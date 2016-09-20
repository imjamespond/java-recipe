package com.chitu.poker.model;

import cn.gecko.commons.data.StaticDataManager;

public class ExpType {

	private int id;
	private String code;
	private String name;
	private int operation;
	
	public static ExpType get(int id) {
		return StaticDataManager.getInstance().get(ExpType.class, id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 经验变更类型代号
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 经验变更类型说明
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 操作类型，0为减少，1为增加，2以后为自定义
	 * 
	 * @return
	 */
	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

}
