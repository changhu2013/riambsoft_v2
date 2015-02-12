package com.riambsoft.core.service;

import java.util.List;
import java.util.Map;

import com.riambsoft.core.RSMethod;
import com.riambsoft.core.RSParam;
import com.riambsoft.core.RSResult;
import com.riambsoft.core.dao.StoreDao;
import com.riambsoft.core.pojo.User;
import com.riambsoft.core.web.RSParamUser;

public class StoreService {

	private StoreDao storeDao;

	public StoreService(StoreDao storeDao) {
		super();
		this.storeDao = storeDao;
	}
	
	@RSMethod
	@RSResult
	public Map<String, ?> read(@RSParam("params") Map<String, ?> params, @RSParamUser User user) {
		
		StoreData data = new StoreData(params);
		Integer start = data.getStart();
		Integer limit = data.getLimit();
		String sortField = data.getSortField();
		String sortDir = data.getSortDir();
		String groupField = data.getGroupBy();
		String groupDir = data.getGroupDir();
		
		List<?> list = storeDao.read(user, start, limit, sortField, sortDir, groupField,
				groupDir, params);
		data.setData(list);
		
		int total = storeDao.getCount(user, params);
		data.setTotal(total);
		
		return data.getDataMap();
	}
}
