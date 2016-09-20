package com.tongyi.exception;

public class BaseException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7908598894636306955L;
	private int code;
	
	public BaseException(int code) {
		this.setCode(code);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
