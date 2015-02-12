package com.riambsoft.core.log;

public class LogException extends Exception {

	private static final long serialVersionUID = 4197798981997480099L;

	public LogException() {
		super();
	}

	public LogException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogException(String message) {
		super(message);
	}

	public LogException(Throwable cause) {
		super(cause);
	}

}
