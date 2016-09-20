package com.pengpeng.stargame.exception;

/**
 * 卖出/出售异常
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-27 下午2:03
 */
public class SaleException extends GameException {

	public SaleException() {
	}

	public SaleException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaleException(String message) {
		super(message);
	}
}
