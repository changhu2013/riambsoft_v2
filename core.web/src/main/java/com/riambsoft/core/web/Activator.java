package com.riambsoft.core.web;

import java.util.Iterator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.riambsoft.core.ServiceEngine;
import com.riambsoft.framework.core.web.ServiceController;
import com.riambsoft.framework.core.web.ServiceControllerManager;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	public void start(BundleContext context) throws Exception {

		Activator.bundleContext = context;
		ServiceControllerManager manager = ServiceControllerManager
				.getInstance();
		for (Iterator<String> iter = manager.getServiceControllerNameIterator(); iter
				.hasNext();) {
			String temp = iter.next();
			System.out.println("给控制器" + temp + "安装服务引擎");
			ServiceController controller = manager.getServiceController(temp);
			
			ServiceEngine engine = new WebServiceEngine();
			controller.setServiceEngine(engine);
		}
	}

	public void stop(BundleContext context) throws Exception {
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

}
