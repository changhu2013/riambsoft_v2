package com.riambsoft.core.dao;

import java.util.List;

import com.riambsoft.core.pojo.Role;
import com.riambsoft.core.pojo.RsMethod;
import com.riambsoft.core.pojo.User;

public interface RsMethodDao {

	public String getId(String controller, String service, String method);

	public RsMethod getById(String id) throws DaoException;
	
	public void delById(String id) throws DaoException;

	public void saveRsMethod(RsMethod rsmethod) throws DaoException;

	public List<RsMethod> getRsMethods(Role role) throws DaoException;
	
	public List<RsMethod> getRsMethods(User user) throws DaoException;

	public List<RsMethod> getRsMethods(User user, String controller)
			throws DaoException;
}
