package com.riambsoft.core.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

public class DataSourceManager implements DataSourceProvider {

	private List<DataSourceProvider> providers;

	private static DataSourceManager instance;

	private DataSourceManager() {
		super();
		providers = Collections
				.synchronizedList(new ArrayList<DataSourceProvider>());
	}

	public static DataSourceManager getInstance() {
		if (instance == null) {
			instance = new DataSourceManager();
		}
		return instance;
	}

	public void addDataSourceProvider(DataSourceProvider provider) {
		synchronized (providers) {
			providers.add(provider);
		}
	}

	public void removeDataSourceProvider(DataSourceProvider provider) {
		synchronized (providers) {
			providers.remove(provider);
		}
	}

	public void destroy() {
		synchronized (providers) {
			providers.clear();
		}
	}

	public DataSource getDataSource(DataSourceConfig config)
			throws DataSourceException {
		int i = 10;
		while (i > 0) {
			if (providers.size() < 1) {
				try {
					i--;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new DataSourceException(e);
				}
			} else {
				for (DataSourceProvider provider : providers) {
					return provider.getDataSource(config);
				}
			}
		}
		// logger.error("未找到数据源服务实例");
		throw new DataSourceException("未找到数据源服务实例");
	}

}
