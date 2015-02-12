package com.riambsoft.core.config.webconsole;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.riambsoft.core.config.ConfigContainer;
import com.riambsoft.core.config.ConfigManager;
import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;

public class Activator implements BundleActivator {

	private ConfigContainer container;

	private ConfigManager manager;

	private Log logger = LogFactory.getLog(getClass());

	public void start(BundleContext context) throws Exception {
		logger.info("启动 com.riambsoft.core.config.webconsole");
		container = ConfigContainer.getInstance();

		WebConsoleConfig config = new WebConsoleConfig();
		config.init();

		manager = new WebConsoleConfigManager(config);
		container.addManager(manager);

		manager.start();
	}

	public void stop(BundleContext context) throws Exception {
		logger.info("关闭com.riambsoft.core.config.webconsole");
		if (manager != null) {
			manager.stop();
		}
		if (container != null) {
			container.removeManager(manager);
		}
	}

}
