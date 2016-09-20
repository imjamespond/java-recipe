package com.tongyi.exception;

public class BeanAreadyException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5647994376669081637L;

	public BeanAreadyException() {
		super(0);
	}
	public BeanAreadyException(int code) {
		super(code);
	}

	public BeanAreadyException(String message) {
		super(message);
	}

	public BeanAreadyException(Throwable cause) {
		super(cause);
	}

	public BeanAreadyException(String message, Throwable cause) {
		super(message, cause);
	}

}
