package com.riambsoft.core.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public abstract class LogHandler implements EventHandler {

	private static final String TEMPLATE = "[%s]\t%tY/%tm/%td %tH:%tM:%tS %tN %s %s\n";

	public abstract void init();

	public abstract void destroy();

	public abstract void log(String message);

	public void handleEvent(Event event) {
		if (event.getTopic().equals(LogEvent.LOG_TOPIC)) {

			LogFactoryConfig config = LogFactoryConfig.getInstance();

			Log.LogLevel level = (Log.LogLevel) event
					.getProperty(LogEvent.LOG_LEVEL);
			String name = (String) event.getProperty(LogEvent.LOG_NAME);
			Object message = event.getProperty(LogEvent.LOG_MESSAGE);
			Throwable t = (Throwable) event.getProperty(LogEvent.LOG_THROW);

			switch (level) {
			case TRACE: {
				if (config.getTrace()) {
					log("TRACE", name, getMessage(message, t));
				}
				break;
			}
			case DEBUG: {
				if (config.getDebug()) {
					log("DEBUG", name, getMessage(message, t));
				}
				break;
			}
			case INFO: {
				if (config.getInfo()) {
					log("INFO", name, getMessage(message, t));
				}
				break;
			}
			case WARN: {
				if (config.getWarn()) {
					log("WARN", name, getMessage(message, t));
				}
				break;
			}
			case ERROR: {
				if (config.getError()) {
					log("ERROR", name, getMessage(message, t));
				}
				break;
			}
			case FATAL: {
				if (config.getFatal()) {
					log("FATAL", name, getMessage(message, t));
				}
				break;
			}
			}
		}
	}

	private String getMessage(Object message, Throwable t) {
		if (t == null) {
			return String.valueOf(message);
		} else {

			final ByteBuffer buffer = ByteBuffer.allocate(1024 * 30);
			OutputStream out = new OutputStream() {
				public void write(int b) throws IOException {
					buffer.put((byte) b);
				}
			};
			t.printStackTrace(new PrintStream(out, true));

			String temp = String.valueOf(message) + "\n";
			if (buffer.hasArray()) {
				temp += new String(Arrays.copyOf(buffer.array(),
						buffer.position()));
			} else {
				int idx = 0;
				byte[] bytes = new byte[buffer.position()];
				buffer.flip();
				while (buffer.hasRemaining()) {
					bytes[idx++] = buffer.get();
				}
				temp += new String(bytes);
			}
			return temp;
		}
	}

	private void log(String level, String name, String message) {
		Date date = new Date();
		String temp = String.format(TEMPLATE, level, date, date, date, date,
				date, date, date, name, String.valueOf(message));
		log(temp);
	}
}
