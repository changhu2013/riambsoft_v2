package com.riambsoft.core.dao;

import java.util.List;

import com.riambsoft.core.pojo.RsApp;
import com.riambsoft.core.pojo.User;

public interface RsAppDao {

	public RsApp getById(String id);

	public void delById(String id);
	
	public List<RsApp> getRsApps(User user);

}
