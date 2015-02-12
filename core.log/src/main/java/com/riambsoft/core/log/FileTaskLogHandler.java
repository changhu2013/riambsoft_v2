package com.riambsoft.core.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FileTaskLogHandler extends LogHandler {

	private FileLogConfig config;
	private File file;

	private Timer timer;
	private List<ByteBuffer> messages;
	private long fileSize;

	public FileTaskLogHandler() {
		super();
	}

	public void init() {
		config = FileLogConfig.getInstance();
		config.init();
		file = config.getNewFile();
		timer = new Timer();
		messages = new ArrayList<ByteBuffer>();
		fileSize = 0L;
	}

	public void destroy() {
		if (config != null) {
			config.destroy();
		}
	}

	public void log(String message) {
		ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		synchronized (messages) {
			messages.add(buffer);
		}
		// 达到最大文件大小,立刻保存一次
		if (fileSize + buffer.limit() >= 1024 * 1024 * config.getMaxSize()) {
			doWriterToFile(file);
			file = config.getNewFile();
			fileSize = 0L;
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
		} else {
			// 未达到最大文件大小,则启动定时任务保存
			fileSize += buffer.limit();
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					doWriterToFile(file);
				}
			}, 1000);
		}
	}

	@SuppressWarnings("resource")
	private synchronized void doWriterToFile(File file) {
		try {
			FileChannel channel = new FileOutputStream(file, true).getChannel();
			for (Iterator<ByteBuffer> iter = messages.iterator(); iter
					.hasNext();) {
				channel.write(iter.next());
			}
			messages.clear();
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
