package com.riambsoft.core.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileBufferLogHandler extends LogHandler {

	private ByteBuffer buffer;
	private FileLogConfig config;
	private File file;
	private int times;

	public FileBufferLogHandler() {
		super();
	}

	public void init() {
		config = FileLogConfig.getInstance();
		config.init();
		//设置内存中的缓存为32K
		buffer = ByteBuffer.allocate(32768);
		file = config.getNewFile();
		times = 0;
	}

	public void destroy() {
		if (config != null) {
			config.destroy();
		}
	}

	public void log(String message) {
		byte[] temp = message.getBytes();
		if (buffer.position() + temp.length < buffer.limit()) {
			buffer.put(temp);
		} else {
			doWriterToFile();
			buffer.put(temp);
		}
	}

	@SuppressWarnings("resource")
	private synchronized void doWriterToFile() {
		// 根据日志文件的最大值和缓存大小计算何时创建新日志文件
		if (times >= config.getMaxSize() * 32) {
			file = config.getNewFile();
			times = 0;
		}
		// 将缓存中内容写入文件并记录写入次数times++
		try {
			FileChannel channel = new FileOutputStream(file, true).getChannel();
			buffer.flip();
			channel.write(buffer);
			channel.close();
			buffer.clear();
			times++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
