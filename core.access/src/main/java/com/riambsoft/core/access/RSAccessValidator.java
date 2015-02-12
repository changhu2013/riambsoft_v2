package com.riambsoft.core.access;

import java.util.Map;

import com.riambsoft.core.pojo.User;
import com.riambsoft.framework.core.web.access.RSAccessException;
import com.riambsoft.framework.core.web.access.RSAccessValidatorBase;

/**
 * 用户权限验证服务
 */
public abstract class RSAccessValidator implements RSAccessValidatorBase {

	public boolean validate(Object user, String controllerName,
			String serviceName, String methodName) throws RSAccessException {
		return doValidate((User) user, controllerName, serviceName, methodName);
	}

	public Map<String, ?> getAccessControlMessage(Object user,
			String controllerName) throws RSAccessException {
		return doGetAccessControlMessage((User) user, controllerName);
	}

	/**
	 * 验证用户是否具有访问该业务方法的权限
	 */
	public abstract boolean doValidate(User user, String controllerName,
			String serviceName, String methodName) throws RSAccessException;

	/**
	 * 获取用户对该业务服务的权限信息
	 */
	public abstract Map<String, ?> doGetAccessControlMessage(User user,
			String controllerName) throws RSAccessException;
}
