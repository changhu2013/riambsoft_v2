package com.riambsoft.core.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.RoleDao;
import com.riambsoft.core.pojo.Role;
import com.riambsoft.core.pojo.User;

public class RoleDaoImpl implements RoleDao {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Role getById(String id) throws DaoException {
		return null;
	}

	public void delById(String id) throws DaoException {

	}

	public void saveRole(Role role) throws DaoException {

	}

	public List<Role> getRoles(User user) throws DaoException {
		return null;
	}
	
	public List<Role> getAllRoles() throws DaoException{
		return null;
	}

}
