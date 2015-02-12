package com.riambsoft.core.datasource;

public class DataSourceException extends Exception {

	private static final long serialVersionUID = 4706530422592568513L;

	public DataSourceException() {
		super();
	}

	public DataSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSourceException(String message) {
		super(message);
	}

	public DataSourceException(Throwable cause) {
		super(cause);
	}

}
