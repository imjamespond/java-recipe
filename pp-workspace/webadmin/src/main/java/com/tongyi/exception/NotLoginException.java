package com.tongyi.exception;

public class NotLoginException extends BaseException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7615423165298164011L;

	public NotLoginException() {
		super(0);
    }

	public NotLoginException(int code) {
		super(code);
	}
    public NotLoginException(String message) {
	super(message);
    }

    public NotLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoginException(Throwable cause) {
        super(cause);
    }

}
