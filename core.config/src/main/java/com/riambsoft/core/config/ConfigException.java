package com.riambsoft.core.config;

public class ConfigException extends Exception {

	private static final long serialVersionUID = 3688674968186067586L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

}
