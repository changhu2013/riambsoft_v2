package com.riambsoft.core;

import com.riambsoft.framework.core.ServiceException;

public class RSServiceException extends ServiceException {

	private static final long serialVersionUID = -1010238779866456755L;

	public RSServiceException(String status, String message, Throwable cause) {
		super(status, message, cause);
	}

	public RSServiceException(String status, String message) {
		super(status, message);
	}

	public RSServiceException(String status, Throwable cause) {
		super(status, cause);
	}

	public RSServiceException(String status) {
		super(status);
	}
	
	
}
