package com.riambsoft.core.config;

public class ConfigPersistenceException extends RuntimeException {

	private static final long serialVersionUID = 914053830311201295L;

	public ConfigPersistenceException() {
		super();
	}

	public ConfigPersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigPersistenceException(String message) {
		super(message);
	}

	public ConfigPersistenceException(Throwable cause) {
		super(cause);
	}

}
