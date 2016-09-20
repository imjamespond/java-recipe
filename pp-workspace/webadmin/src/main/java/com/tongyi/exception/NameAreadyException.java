/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tongyi.exception;

/**
 *
 * @author Administrator
 */
public class NameAreadyException extends ServiceException{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2081696264132576400L;

	public NameAreadyException(String message, Throwable cause) {
    }

    public NameAreadyException(Throwable cause) {
    }

    public NameAreadyException(String message) {
    }

	public NameAreadyException(int code) {
		super(code);
	}
    public NameAreadyException() {
    	super(0);
    }

}
