package com.riambsoft.core.log;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;

public class LogEvent extends Event {

	public static final String LOG_TOPIC = "LogEvent";

	public static final String LOG_LEVEL = "LogLevel";

	public static final String LOG_NAME = "LogName";

	public static final String LOG_MESSAGE = "LogMessage";

	public static final String LOG_THROW = "LogThrow";

	public LogEvent(Log.LogLevel type, String name, Object message) {
		this(LOG_TOPIC, createProperties(type, name, message, null));
	}

	public LogEvent(Log.LogLevel type, String name, Object message, Throwable t) {
		this(LOG_TOPIC, createProperties(type, name, message, t));
	}

	private static Map<String, Object> createProperties(Log.LogLevel level,
			String name, Object message, Throwable t) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(LOG_LEVEL, level);
		map.put(LOG_NAME, name);
		map.put(LOG_MESSAGE, message);
		if (t != null) {
			map.put(LOG_THROW, t);
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private LogEvent(String topic, Map properties) {
		super(LOG_TOPIC, properties);
	}

	@SuppressWarnings("rawtypes")
	private LogEvent(String topic, Dictionary properties) {
		super(LOG_TOPIC, properties);
	}

}
