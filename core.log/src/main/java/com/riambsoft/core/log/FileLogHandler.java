package com.riambsoft.core.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileLogHandler extends LogHandler {

	private FileLogConfig config;
	private File file;

	private long fileSize;

	public FileLogHandler() {
		super();
	}

	public void init() {
		config = FileLogConfig.getInstance();
		config.init();

		file = config.getNewFile();
		fileSize = 0L;
	}

	public void destroy() {
		if (config != null) {
			config.destroy();
		}
	}

	public void log(String message) {
		doWriterToFile(message);
	}

	@SuppressWarnings("resource")
	private void doWriterToFile(String message) {
		ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		if (fileSize + buffer.limit() >= config.getMaxSize() * 1024 * 1024) {
			file = config.getNewFile();
			fileSize = 0L;
		}
		try {
			FileChannel channel = new FileOutputStream(file, true).getChannel();
			fileSize += buffer.limit();
			channel.write(buffer);
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
