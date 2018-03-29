package com.metasoft.model;

public class PrivilegeError extends Exception {
   
	private static final long serialVersionUID = 1L;

	public PrivilegeError(String errorMsg) {
        super(errorMsg);
    }

    public PrivilegeError(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }
}
