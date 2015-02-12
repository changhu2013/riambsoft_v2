package com.riambsoft.core.auth;

import com.riambsoft.core.pojo.User;

public interface RSAuth {

	public User validate(String userCode, String password);

}
