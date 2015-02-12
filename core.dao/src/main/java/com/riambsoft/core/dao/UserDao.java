package com.riambsoft.core.dao;

import com.riambsoft.core.pojo.User;

public interface UserDao extends StoreDao {

	public User getById(String id) throws DaoException;

	public User getByCode(String userCode) throws DaoException;

	public void delById(String id) throws DaoException;

	public void saveUser(User user) throws DaoException;

}
