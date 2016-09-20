package com.pengpeng.stargame.exception;

public class GameException extends Exception {

	private String key;
	private Object[] value;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2750784361649434676L;

	public GameException(String key, Object[] value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] getValue() {
		return value;
	}

	public void setValue(Object[] value) {
		this.value = value;
	}

	public GameException() {
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

	
}
