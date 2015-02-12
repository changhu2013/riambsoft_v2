package com.riambsoft.core.log;

import java.io.Serializable;

import org.osgi.service.event.EventAdmin;

public class Log implements Serializable {

	private static final long serialVersionUID = 8166112072523037063L;

	private String name;

	private EventAdmin eventAdmin;

	public enum LogLevel {
		TRACE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAL(6);

		private int id;

		LogLevel(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public Log(String name, EventAdmin eventAdmin) {
		super();
		this.name = name;
		this.eventAdmin = eventAdmin;
	}

	public void trace(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.TRACE, name, message));
	}

	public void trace(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.TRACE, name, message, t));
	}

	public void debug(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.DEBUG, name, message));
	}

	public void debug(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.DEBUG, name, message, t));
	}

	public void info(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.INFO, name, message));
	}

	public void info(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.INFO, name, message, t));
	}

	public void warn(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.WARN, name, message));
	}

	public void warn(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.WARN, name, message, t));
	}

	public void error(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.ERROR, name, message));
	}

	public void error(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.ERROR, name, message, t));
	}

	public void fatal(Object message) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.FATAL, name, message));
	}

	public void fatal(Object message, Throwable t) {
		eventAdmin.postEvent(new LogEvent(Log.LogLevel.FATAL, name, message, t));
	}

}
