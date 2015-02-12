package com.riambsoft.core.datasource.dbcp.oracle;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverConnectionFactory;
import org.apache.commons.dbcp.SQLNestedException;

@SuppressWarnings("deprecation")
public class DbcpBasicDataSource extends BasicDataSource {

	@SuppressWarnings({ "rawtypes"})
	protected ConnectionFactory createConnectionFactory() throws SQLException {
		// Load the JDBC driver class
		Class driverFromCCL = null;
		if (driverClassName != null) {
			try {
				try {
					if (driverClassLoader == null) {
						Class.forName(driverClassName);
					} else {
						Class.forName(driverClassName, true, driverClassLoader);
					}
				} catch (ClassNotFoundException cnfe) {
					driverFromCCL = Thread.currentThread()
							.getContextClassLoader().loadClass(driverClassName);
				}
			} catch (Throwable t) {
				String message = "Cannot load JDBC driver class '"
						+ driverClassName + "'";
				logWriter.println(message);
				t.printStackTrace(logWriter);
				throw new SQLNestedException(message, t);
			}
		}

		// Create a JDBC driver instance
		Driver driver = null;
		try {
			if (driverFromCCL == null) {
				driver = DriverManager.getDriver(url);
			} else {
				// Usage of DriverManager is not possible, as it does not
				// respect the ContextClassLoader
				driver = (Driver) driverFromCCL.newInstance();
				if (!driver.acceptsURL(url)) {
					throw new SQLException("No suitable driver", "08001");
				}
			}
		} catch (Throwable t) {
			String message = "Cannot create JDBC driver of class '"
					+ (driverClassName != null ? driverClassName : "")
					+ "' for connect URL '" + url + "'";
			logWriter.println(message);
			t.printStackTrace(logWriter);
			throw new SQLNestedException(message, t);
		}

		// Can't test without a validationQuery
		if (validationQuery == null) {
			setTestOnBorrow(false);
			setTestOnReturn(false);
			setTestWhileIdle(false);
		}

		// Set up the driver connection factory we will use
		String user = username;
		if (user != null) {
			connectionProperties.put("user", user);
		} else {
			log("DBCP DataSource configured without a 'username'");
		}

		String pwd = password;
		if (pwd != null) {
			connectionProperties.put("password", pwd);
		} else {
			log("DBCP DataSource configured without a 'password'");
		}

		ConnectionFactory driverConnectionFactory = new DriverConnectionFactory(
				driver, url, connectionProperties);
		return driverConnectionFactory;
	}

}
