package com.riambsoft.core.log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.riambsoft.core.config.Config;
import com.riambsoft.core.config.RSDesc;
import com.riambsoft.core.config.RSGetter;
import com.riambsoft.core.config.RSSetter;

public class FileLogConfig extends Config {

	private static FileLogConfig instance;

	/**
	 * 日志存放目录，初始目录为操作系统中用户目录\framework
	 */
	private String directory;

	/**
	 * 日志文件最大值, 单位为MB
	 */
	private int maxSize = 10;

	@RSGetter("directory")
	public String getDirectory() {
		if (directory == null) {
			String dir = System.getProperty("user.home") + "\\framework";
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			setDirectory(dir);
		}
		return directory;
	}

	@RSSetter("directory")
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@RSGetter("max_size")
	@RSDesc("Unit:MB")
	public int getMaxSize() {
		return maxSize;
	}

	@RSSetter("max_size")
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public File getNewFile() {
		String dir = getDirectory();
		File file = new File(dir + "\\framework.log");
		if (file.exists()) {
			Date date = new Date();		
			String temp = String.format("%tY%tm%td-%tH%tM%tS-%tN.log", date,
					date, date, date, date, date, date);
			file.renameTo(new File(dir + "\\" + temp));
			file = new File(dir + "\\framework.log");
		}
		try {
			file.createNewFile();
		} catch (IOException e) {}
		return file;
	}

	public static FileLogConfig getInstance() {
		if (instance == null) {
			instance = new FileLogConfig();
		}
		return instance;
	}
}
