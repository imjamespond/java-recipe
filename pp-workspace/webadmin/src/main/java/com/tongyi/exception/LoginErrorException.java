package com.tongyi.exception;

public class LoginErrorException extends BaseException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7615423165298164011L;

	public LoginErrorException() {
		super(0);
    }

	public LoginErrorException(int code) {
		super(code);
	}
   public LoginErrorException(String message) {
	super(message);
    }

    public LoginErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginErrorException(Throwable cause) {
        super(cause);
    }

}
