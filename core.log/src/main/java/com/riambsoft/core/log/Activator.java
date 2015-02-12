package com.riambsoft.core.log;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;
	
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		LogFactoryConfig.getInstance();
		LogFactory.init(context);
	}

	public void stop(BundleContext context) throws Exception {
		LogFactory.destroy();
	}

	public static BundleContext getBundleContext(){
		return bundleContext;
	}
}
