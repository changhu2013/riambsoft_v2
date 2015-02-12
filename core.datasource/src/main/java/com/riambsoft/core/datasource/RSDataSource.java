package com.riambsoft.core.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.AttributeList;
import javax.sql.DataSource;

import com.riambsoft.core.config.Config;
import com.riambsoft.core.config.ConfigChangeListener;
import com.riambsoft.core.config.ConfigException;

public class RSDataSource implements DataSource {

	private DataSource dataSource = null;
	
	public RSDataSource(final DataSourceConfig dataSourceConfig) {
		super();
		dataSourceConfig.addChangeListener(new ConfigChangeListener() {

			public void onInit(Config config) throws ConfigException {
			}

			public void onDestroy(Config config) throws ConfigException {
			}

			public void onAttributeChange(Config config, String attribute,
					Object value) throws ConfigException {
			}

			public void onAttributesChange(Config config,
					AttributeList attributes) throws ConfigException {
				try {
					dataSource = DataSourceManager.getInstance()
							.getDataSource(dataSourceConfig);
				} catch (DataSourceException e) {
					e.printStackTrace();
				}
			}

			public void onDoAction(Config config, String action, Object[] params)
					throws ConfigException {
			}
		});
		try {
			this.dataSource = DataSourceManager.getInstance().getDataSource(
					dataSourceConfig);
		} catch (DataSourceException e) {
			e.printStackTrace();
		}
	}

	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return dataSource.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return dataSource.isWrapperFor(iface);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return dataSource.getConnection(username, password);
	}

}
