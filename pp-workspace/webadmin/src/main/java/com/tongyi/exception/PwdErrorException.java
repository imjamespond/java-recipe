package com.tongyi.exception;

public class PwdErrorException extends BaseException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7615423165298164011L;

	public PwdErrorException() {
		super(0);
    }

	public PwdErrorException(int code) {
		super(code);
	}
    public PwdErrorException(String message) {
	super(message);
    }

    public PwdErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PwdErrorException(Throwable cause) {
        super(cause);
    }

}
