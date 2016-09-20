package com.tongyi.exception;

public class NotFoundBeanException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8623786604885185955L;

	public NotFoundBeanException() {
		super(0);
	}

	public NotFoundBeanException(int code) {
		super(code);
	}
	public NotFoundBeanException(String message) {
		super(message);
	}

	public NotFoundBeanException(Throwable cause) {
		super(cause);
	}

	public NotFoundBeanException(String message, Throwable cause) {
		super(message, cause);
	}

}
