package com.metasoft.model.exception;

public class RestInvokeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RestInvokeException(String errMsg) {
		super(errMsg);
	}
	
	public RestInvokeException(Exception e) {
		super(e);
	}

	public RestInvokeException(String errorMsg, Throwable cause) {
		super(errorMsg, cause);
	}

}
