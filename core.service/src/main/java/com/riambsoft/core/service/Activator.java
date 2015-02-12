package com.riambsoft.core.service;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
    	System.out.println("启动core.service");
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("关闭core.service");
    }

}
