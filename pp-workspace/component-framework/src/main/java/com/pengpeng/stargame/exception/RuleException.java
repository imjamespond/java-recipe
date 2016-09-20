package com.pengpeng.stargame.exception;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-26下午9:21
 */
public class RuleException extends GameException{
	public RuleException() {
	}

	public RuleException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleException(String message) {
		super(message);
	}
}
