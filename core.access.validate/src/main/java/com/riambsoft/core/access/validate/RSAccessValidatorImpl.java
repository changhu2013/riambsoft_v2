package com.riambsoft.core.access.validate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.riambsoft.core.access.RSAccessValidator;
import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.RsMethodDao;
import com.riambsoft.core.pojo.RsMethod;
import com.riambsoft.core.pojo.User;
import com.riambsoft.framework.core.web.access.RSAccessException;

public class RSAccessValidatorImpl extends RSAccessValidator {

	private RsMethodDao rsMethodDao;

	private Map<String, RsMethod> cache = new HashMap<String, RsMethod>();

	public void setRsMethodDao(RsMethodDao rsMethodDao) {
		this.rsMethodDao = rsMethodDao;
	}

	public boolean isAccessControl(String controllerName, String serviceName,
			String methodName) throws RSAccessException {

		String id = rsMethodDao.getId(controllerName, serviceName, methodName);

		RsMethod rsMethod = null;
		if (cache.containsKey(id)) {
			rsMethod = cache.get(id);
		} else {
			try {
				rsMethod = rsMethodDao.getById(id);
				cache.put(id, rsMethod);
			} catch (DaoException e) {
			}
		}
		if (rsMethod != null) {
			return rsMethod.isControl();
		} else {
			return false;
		}
	}

	public boolean doValidate(User user, String controllerName,
			String serviceName, String methodName) throws RSAccessException {

		if (rsMethodDao == null) {
			throw new RSAccessException("未找到权限持久化服务[rightDao]");
		}

		String id = rsMethodDao.getId(controllerName, serviceName, methodName);
		try {
			List<RsMethod> rights = rsMethodDao.getRsMethods(user,
					controllerName);
			for (Iterator<RsMethod> iter = rights.iterator(); iter.hasNext();) {
				RsMethod rsMethod = iter.next();
				if (rsMethod != null && id.equals(rsMethod.getId())) {
					return true;
				}
			}
		} catch (DaoException e) {
		}

		return false;
	}

	public Map<String, Boolean> doGetAccessControlMessage(User user,
			String controllerName) throws RSAccessException {

		if (rsMethodDao == null) {
			throw new RSAccessException("未找到权限持久化服务[rightDao]");
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		try {
			List<RsMethod> rights = rsMethodDao.getRsMethods(user,
					controllerName);
			for (Iterator<RsMethod> iter = rights.iterator(); iter.hasNext();) {
				RsMethod rsMethod = iter.next();
				String key = rsMethod.getService() + "_" + rsMethod.getMethod();
				result.put(key, rsMethod.isControl());
			}

		} catch (DaoException e) {
		}

		return result;
	}
}
