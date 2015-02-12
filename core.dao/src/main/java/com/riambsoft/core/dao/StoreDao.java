package com.riambsoft.core.dao;

import java.util.List;
import java.util.Map;

import com.riambsoft.core.pojo.User;

public interface StoreDao {
	
	public List<?> read(User user, Integer start, Integer limit, String sortField,
			String sortDir, String groupField, String groupDir,
			Map<String, ?> params);

	public Integer getCount(User user, Map<String, ?> params);
	
}