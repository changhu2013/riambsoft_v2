package com.riambsoft.core.log;

import com.riambsoft.core.config.Config;
import com.riambsoft.core.config.RSGetter;
import com.riambsoft.core.config.RSSetter;

public class LogFactoryConfig extends Config {

	private static LogFactoryConfig instance;

	private boolean trace = true;

	private boolean debug = true;

	private boolean info = true;

	private boolean warn = true;

	private boolean error = true;

	private boolean fatal = true;

	private String handler = "com.riambsoft.core.log.SimpleLogHandler";

	private LogFactoryConfig() {
		super();
	}

	@RSSetter("1_trace")
	public void setTrace(boolean value) {
		trace = value;
	}

	@RSGetter("1_trace")
	public boolean getTrace() {
		return trace;
	}

	@RSSetter("2_debug")
	public void setDebug(boolean value) {
		debug = value;
	}

	@RSGetter("2_debug")
	public boolean getDebug() {
		return debug;
	}

	@RSSetter("3_info")
	public void setInfo(boolean value) {
		info = value;
	}

	@RSGetter("3_info")
	public boolean getInfo() {
		return info;
	}

	@RSSetter("4_warn")
	public void setWarn(boolean value) {
		warn = value;
	}

	@RSGetter("4_warn")
	public boolean getWarn() {
		return warn;
	}

	@RSSetter("5_error")
	public void setError(boolean value) {
		error = value;
	}

	@RSGetter("5_error")
	public boolean getError() {
		return error;
	}

	@RSSetter("6_fatal")
	public void setFatal(boolean value) {
		fatal = value;
	}

	@RSGetter("6_fatal")
	public boolean getFatal() {
		return fatal;
	}

	@RSGetter("7_handler")
	public String getHandler() {
		return handler;
	}

	@RSSetter("7_handler")
	public void setHandler(String handler) {
		if (handler != null && !handler.trim().equals(this.handler)) {
			try {
				LogFactory.setLogHandler(handler.trim());
				this.handler = handler.trim();
			} catch (LogException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static LogFactoryConfig getInstance() {
		if (instance == null) {
			instance = new LogFactoryConfig();
			instance.init();
		}
		return instance;
	}
}
