package com.riambsoft.core.dao;

import com.riambsoft.core.datasource.DataSourceConfig;

public class CoreDataSourceConfig extends DataSourceConfig {

	private static CoreDataSourceConfig config;

	private CoreDataSourceConfig() {
		super();
	}

	public static synchronized CoreDataSourceConfig getInstance() {
		if (config == null) {
			config = new CoreDataSourceConfig();
		}
		return config;
	}

}
