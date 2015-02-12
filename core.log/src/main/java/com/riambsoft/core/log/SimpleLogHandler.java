package com.riambsoft.core.log;

public class SimpleLogHandler extends LogHandler {

	public void init() {
	}

	public void destroy() {
	}

	public void log(String message) {
		System.out.print(message);
	}
}
