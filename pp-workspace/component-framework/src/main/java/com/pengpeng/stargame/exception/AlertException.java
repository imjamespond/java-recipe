package com.pengpeng.stargame.exception;

/**
 * @author mql
 *
 * Mar 15, 2011  8:06:32 PM
 * 描述：提示以异常形式抛出
 */
public class AlertException extends GameException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4831630845843293509L;

	public AlertException() {
		super();

	}

	public AlertException(String message, Throwable cause) {
		super(message, cause);

	}

	public AlertException(Throwable cause) {
		super(cause);

	}

	public AlertException(String message) {
	    super(message);
    }
}
