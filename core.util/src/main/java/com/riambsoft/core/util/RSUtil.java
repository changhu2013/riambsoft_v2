package com.riambsoft.core.util;

import java.util.UUID;

import com.riambsoft.framework.core.util.FrameworkUtil;

public class RSUtil {

	/**
	 * 获取框架类加载器
	 * 
	 * @return
	 */
	public static ClassLoader getFrameworkClassLoader() {
		return FrameworkUtil.class.getClassLoader();
	}

	/**
	 * 获取唯一的一个键值
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID id = UUID.randomUUID();
		return "RSO:" + id.toString();
	}

}
