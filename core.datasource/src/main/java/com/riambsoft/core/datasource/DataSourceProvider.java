package com.riambsoft.core.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {

	public DataSource getDataSource(DataSourceConfig config)
			throws DataSourceException;

}
