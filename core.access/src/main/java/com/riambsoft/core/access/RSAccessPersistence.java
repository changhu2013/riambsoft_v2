package com.riambsoft.core.access;

/**
 * 权限控制点持久化服务接口
 */
public interface RSAccessPersistence {

	/**
	 * 保存权限控制点
	 */
	public void save(String controllerName, String serviceName,
			String methodName, boolean control, String desc)
			throws RSAccessException;

}
