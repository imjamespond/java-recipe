package com.tongyi.exception;

public class UniqueFieldException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6630474290655825839L;

	public UniqueFieldException() {
		super(0);
	}

	public UniqueFieldException(int code) {
		super(code);
	}
	public UniqueFieldException(String message) {
		super(message);
	}

	public UniqueFieldException(Throwable cause) {
		super(cause);
	}

	public UniqueFieldException(String message, Throwable cause) {
		super(message, cause);
	}

}
