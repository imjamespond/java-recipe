package com.metasoft.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="loading data failed")
public class LoadDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1663494498062149896L;

}
