package com.riambsoft.core.config.persistent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import com.riambsoft.core.config.ConfigPersistence;
import com.riambsoft.core.config.ConfigPersistenceException;
import com.riambsoft.core.util.RSUtil;

public class FileConfigPersistence implements ConfigPersistence {

	public void writeConfigAttribute(String name, String attribute, Object value)
			throws ConfigPersistenceException {
		ClassLoader loader = RSUtil.getFrameworkClassLoader();
		URL url = loader.getResource(".");
		File file = new File(url.getFile() + getConfigFileName(name));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Properties result = new Properties();
		try {
			InputStream input = new FileInputStream(file);
			result.load(input);
			input.close();
			result.setProperty(attribute, value.toString());
			OutputStream output = new FileOutputStream(file);
			result.store(output, "www.riambsoft.com");
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			throw new ConfigPersistenceException(e);
		} catch (IOException e) {
			throw new ConfigPersistenceException(e);
		}
	}

	public Object readConfigAttribute(String name, String attribute)
			throws ConfigPersistenceException {
		ClassLoader loader = RSUtil.getFrameworkClassLoader();
		URL url = loader.getResource(".");
		File file = new File(url.getFile() + getConfigFileName(name));
		if (file.exists()) {
			Properties result = new Properties();
			try {
				InputStream input = new FileInputStream(file);
				result.load(input);
				input.close();
				return result.get(attribute);
			} catch (FileNotFoundException e) {
				throw new ConfigPersistenceException(e);
			} catch (IOException e) {
				throw new ConfigPersistenceException(e);
			}
		}
		return null;
	}

	private String getConfigFileName(String name) {
		return "config/" + name.replaceAll("\\.", "_") + ".properties";
	}
}
