package com.riambsoft.core.datasource.dbcp.oracle;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.riambsoft.core.datasource.DataSourceConfig;
import com.riambsoft.core.datasource.DataSourceException;
import com.riambsoft.core.datasource.DataSourceProvider;

public class DbcpDataSourceProvider implements DataSourceProvider {

	public DbcpDataSourceProvider() {
		super();
	}

	public DataSource getDataSource(DataSourceConfig config)
			throws DataSourceException {

		String driverClassName = config.getDriverClassName();
		String userName = config.getUsername();
		String password = config.getPassword();
		String url = config.getUrl();
		
		if (driverClassName == null || "".equals(driverClassName.trim())) {
			throw new DataSourceException("数据库驱动类配置为空");
		}

		if (url == null || "".equals(url.trim())) {
			throw new DataSourceException("数据库连接URL配置为空");
		}

		if (userName == null || "".equals(userName.trim())) {
			throw new DataSourceException("数据库连接用户名配置为空");
		}

		if (password == null || "".equals(password.trim())) {
			throw new DataSourceException("数据库连接密码配置为空");
		}

		BasicDataSource ds = new DbcpBasicDataSource();

		ds.setDriverClassName(driverClassName);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setUrl(url);

		return ds;
	}
}
