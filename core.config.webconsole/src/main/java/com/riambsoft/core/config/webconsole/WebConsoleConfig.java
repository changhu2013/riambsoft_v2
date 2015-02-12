package com.riambsoft.core.config.webconsole;

import com.riambsoft.core.config.Config;
import com.riambsoft.core.config.RSDesc;
import com.riambsoft.core.config.RSGetter;
import com.riambsoft.core.config.RSSetter;

@RSDesc("config webconsole config")
public class WebConsoleConfig extends Config {

	private int coreConfigWebconsolePort = 8888;

	private String coreConfigWebconsoleLogin = "admin";

	private String coreConfigWebconsolePassword = "pass!@#";

	public void setCoreConfigWebconsolePort(int coreConfigWebconsolePort) {
		this.coreConfigWebconsolePort = coreConfigWebconsolePort;
	}

	@RSSetter("login")
	public void setCoreConfigWebconsoleLogin(String coreConfigWebconsoleLogin) {
		this.coreConfigWebconsoleLogin = coreConfigWebconsoleLogin;
	}

	@RSSetter("password")
	public void setCoreConfigWebconsolePassword(
			String coreConfigWebconsolePassword) {
		this.coreConfigWebconsolePassword = coreConfigWebconsolePassword;
	}

	@RSGetter("port")
	public int getCoreConfigWebconsolePort() {
		return coreConfigWebconsolePort;
	}

	@RSGetter("login")
	public String getCoreConfigWebconsoleLogin() {
		return coreConfigWebconsoleLogin;
	}

	@RSGetter("password")
	public String getCoreConfigWebconsolePassword() {
		return coreConfigWebconsolePassword;
	}
}
