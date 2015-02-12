package com.riambsoft.core.dao;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static BundleContext context;

	private CoreDataSourceConfig config;

	public void start(BundleContext context) throws Exception {

		Activator.context = context;

		config = CoreDataSourceConfig.getInstance();
		config.setUsername("dev9i");
		config.setPassword("pass");
		config.setUrl("jdbc:oracle:thin:@192.168.168.10:1521:zlora");

		config.init();
	}

	public void stop(BundleContext context) throws Exception {

		if (config != null) {
			config.destroy();
		}
	}

}
