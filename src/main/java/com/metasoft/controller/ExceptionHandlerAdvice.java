package com.metasoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.metadataServices.interfaces.MDServiceError;
import com.metasoft.model.PrivilegeError;
import com.metasoft.service.LocalizationService;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler{
	
	@Autowired
	private LocalizationService service;

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ExceptionHandler(DataSharingMgrError.class)
    public @ResponseBody String handleAuthException(DataSharingMgrError ex) {
		ex.printStackTrace();
		String localMsg = formatErrorMsg(ex.getMessage(),new String[]{});
    	return localMsg;
    }
	private String formatErrorMsg(String message,String[] errorParam) {
		String errorMsg = message.replaceAll("\\s+",".");
		String localMsg = service.getLocalString(errorMsg,errorParam);
		if (errorMsg.equals(localMsg)) {
			localMsg = message;
		}
		return localMsg;
	}
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ExceptionHandler(PrivilegeError.class)
    public @ResponseBody String handlePrivilegeError(PrivilegeError ex) {
		String localMsg = formatErrorMsg(ex.getMessage(),new String[]{});
    	return localMsg;
    }
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ExceptionHandler(MDServiceError.class)
    public @ResponseBody String handlePrivilegeError(MDServiceError ex) {
		String localMsg = formatErrorMsg(ex.getMessage(),new String[]{});
    	return localMsg;
    }
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleException(Exception ex) {
		ex.printStackTrace();	
		return ex.toString();
	}
}