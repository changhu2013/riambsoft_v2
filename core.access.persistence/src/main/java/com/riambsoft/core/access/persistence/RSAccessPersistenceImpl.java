package com.riambsoft.core.access.persistence;

import com.riambsoft.core.access.RSAccessException;
import com.riambsoft.core.access.RSAccessPersistence;
import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.RsMethodDao;
import com.riambsoft.core.pojo.RsMethod;

public class RSAccessPersistenceImpl implements RSAccessPersistence {

	private RsMethodDao rsMethodDao;

	public void setRsMethodDao(RsMethodDao rsMethodDao) {
		this.rsMethodDao = rsMethodDao;
	}

	public void save(String controllerName, String serviceName,
			String methodName, boolean control, String desc)
			throws RSAccessException {

		if (rsMethodDao == null) {
			throw new RSAccessException("未找到权限持久化服务[rightDao]");
		}

		String id = rsMethodDao.getId(controllerName, serviceName, methodName);

		RsMethod rsmethod = new RsMethod();
		rsmethod.setId(id);
		rsmethod.setController(controllerName);
		rsmethod.setService(serviceName);
		rsmethod.setMethod(methodName);
		rsmethod.setControl(control);
		rsmethod.setRemark(desc);

		try {
			rsMethodDao.saveRsMethod(rsmethod);
		} catch (DaoException e) {
			throw new RSAccessException(e);
		}
	}
}
