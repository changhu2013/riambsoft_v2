package com.riambsoft.core.config.webconsole;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.riambsoft.core.config.ConfigManager;
import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;
import com.sun.jdmk.comm.AuthInfo;
import com.sun.jdmk.comm.HtmlAdaptorServer;

public class WebConsoleConfigManager implements ConfigManager {

	private MBeanServer server;

	private HtmlAdaptorServer adapter;

	private Log logger = LogFactory.getLog(getClass());

	public WebConsoleConfigManager(WebConsoleConfig config) {
		super();
		server = MBeanServerFactory.createMBeanServer();
		adapter = new HtmlAdaptorServer();

		AuthInfo authInfo = new AuthInfo();

		authInfo.setLogin(config.getCoreConfigWebconsoleLogin());
		authInfo.setPassword(config.getCoreConfigWebconsolePassword());

		adapter.addUserAuthenticationInfo(authInfo);

		adapter.setPort(config.getCoreConfigWebconsolePort());

		ObjectName adapterName;
		try {
			adapterName = new ObjectName("ConfigAgent:name=htmladapter");
			server.registerMBean(adapter, adapterName);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		}

	}

	public void addConfig(String name, Object config) {
		logger.debug("添加配置服务" + name);
		try {
			server.registerMBean(config, new ObjectName(getObjectName(name)));
		} catch (InstanceAlreadyExistsException e) {
			logger.error(e.getMessage(), e);
		} catch (MBeanRegistrationException e) {
			logger.error(e.getMessage(), e);
		} catch (NotCompliantMBeanException e) {
			logger.error(e.getMessage(), e);
		} catch (MalformedObjectNameException e) {
			logger.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void removeConfig(String name) {
		logger.debug("删除配置服务" + name);
		try {
			server.unregisterMBean(new ObjectName(getObjectName(name)));
		} catch (MBeanRegistrationException e) {
			logger.error(e.getMessage(), e);
		} catch (InstanceNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (MalformedObjectNameException e) {
			logger.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String getObjectName(String name) {
		return "Config" + ":type=" + name;
	}

	public void start() {
		logger.debug("启动配置服务管理控制台");
		adapter.start();
	}

	public void stop() {
		adapter.stop();
		logger.debug("关闭配置服务管理控制台");
	}

}
