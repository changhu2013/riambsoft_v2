package com.riambsoft.core.datasource;

import com.riambsoft.core.config.Config;
import com.riambsoft.core.config.RSGetter;
import com.riambsoft.core.config.RSSetter;

public class DataSourceConfig extends Config {

	private String driverClassName = "oracle.jdbc.driver.OracleDriver";

	private String url;

	private String username;

	private String password;

	@RSGetter("driver")
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@RSGetter("url")
	public String getUrl() {
		return url;
	}

	@RSSetter("url")
	public void setUrl(String url) {
		this.url = url;
	}

	@RSGetter("user")
	public String getUsername() {
		return username;
	}

	@RSSetter("user")
	public void setUsername(String username) {
		this.username = username;
	}

	@RSGetter("password")
	public String getPassword() {
		return password;
	}

	@RSSetter("password")
	public void setPassword(String password) {
		this.password = password;
	}
}
