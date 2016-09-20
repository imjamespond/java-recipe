package com.tongyi.exception;

public class ServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -98684024166287911L;

	public ServiceException() {
		super(0);
	}

	public ServiceException(int code) {
		super(code);
	}
	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
