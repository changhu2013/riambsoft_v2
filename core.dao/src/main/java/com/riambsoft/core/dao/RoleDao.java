package com.riambsoft.core.dao;

import java.util.List;

import com.riambsoft.core.pojo.Role;
import com.riambsoft.core.pojo.User;

public interface RoleDao {

	public Role getById(String id) throws DaoException;

	public void delById(String id) throws DaoException;

	public void saveRole(Role role) throws DaoException;

	public List<Role> getRoles(User user) throws DaoException;

	public List<Role> getAllRoles() throws DaoException;
}
