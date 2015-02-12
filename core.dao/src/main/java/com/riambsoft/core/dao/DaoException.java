package com.riambsoft.core.dao;

public class DaoException extends Exception {

	private static final long serialVersionUID = 6503497913849520337L;

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
