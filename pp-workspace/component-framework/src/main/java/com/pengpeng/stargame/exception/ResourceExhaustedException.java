package com.pengpeng.stargame.exception;

public class ResourceExhaustedException extends GameException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8875747084193039451L;

	public ResourceExhaustedException() {
	}

	public ResourceExhaustedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceExhaustedException(String message) {
		super(message);
	}

	public ResourceExhaustedException(Throwable cause) {
		super(cause);
	}

}
