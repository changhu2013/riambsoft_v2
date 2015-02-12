package com.riambsoft.core.auth.db;

import com.riambsoft.core.RSServiceException;
import com.riambsoft.core.auth.Constants;
import com.riambsoft.core.auth.RSAuth;
import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.UserDao;
import com.riambsoft.core.pojo.User;
import com.riambsoft.core.util.MD5Util;

public class RSAuthDataBase implements RSAuth {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User validate(String userCode, String password) {

		if (userCode == null || "".equals(userCode.trim())) {
			throw new RSServiceException(Constants.USER_NOTFOUND_ERROR,
					"用户名不能为空");
		}

		if (password == null || "".equals(password.trim())) {
			throw new RSServiceException(Constants.PASSWORD_ERROR, "密码不能为空");
		}

		User user = null;
		try {
			user = userDao.getByCode(userCode);
		} catch (DaoException e) {
			throw new RSServiceException(Constants.USER_NOTFOUND_ERROR,
					"用户名不存在", e);
		}
		if (user == null) {
			throw new RSServiceException(Constants.USER_NOTFOUND_ERROR,
					"用户名不存在");
		} else {
			String pwd = user.getPassword();
			String temp = MD5Util.md5(password);
			if (temp != null && temp.equals(pwd)) {
				// 删除密码信息,处于安全考虑
				user.setPassword(null);
				return user;
			} else {
				throw new RSServiceException(Constants.PASSWORD_ERROR, "密码错误");
			}
		}
	}
}
