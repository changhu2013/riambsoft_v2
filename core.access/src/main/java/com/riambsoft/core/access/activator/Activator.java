package com.riambsoft.core.access.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;

import com.riambsoft.core.access.RSAccessManager;
import com.riambsoft.core.access.RSAccessPersistence;

public class Activator implements BundleActivator, ServiceListener,
		SynchronousBundleListener {

	private BundleContext context;

	public void start(BundleContext context) throws Exception {

		context.addServiceListener(this);
		context.addBundleListener(this);

		this.context = context;
	}

	// 当该Bundle关闭时,销毁访问控制点管理器
	public void stop(BundleContext context) throws Exception {
		RSAccessManager.getInstacne().clearCache();
	}

	// 监听Bundle状态变化事件,将该Bundle启动之前,由其他Bundle提供的数据源服务纳入管理器
	public void bundleChanged(BundleEvent event) {
		if (event.getBundle().equals(context.getBundle())) {
			int type = event.getType();
			switch (type) {
			case BundleEvent.STARTED: {
				this.setRSAccessPersistenceService();
				break;
			}
			}
		}
	}

	// 将已经创建的访问控制点持久化服务纳入管理器
	private void setRSAccessPersistenceService() {
		RSAccessManager manager = RSAccessManager.getInstacne();
		try {
			ServiceReference[] references = context.getServiceReferences(
					RSAccessPersistence.class.getName(), null);

			for (ServiceReference reference : references) {
				Object service = context.getService(reference);

				if (isRSAccessPersistenceService(service)) {
					manager.setRSAccessPersistenceService((RSAccessPersistence) service);
				}
			}
		} catch (InvalidSyntaxException e) {
		}
	}

	// 监听服务变化事件,如果有访问控制点持久化服务状态发生变化则更新管理器
	public void serviceChanged(ServiceEvent event) {
		Object service = context.getService(event.getServiceReference());
		if (isRSAccessPersistenceService(service)) {
			RSAccessManager m = RSAccessManager.getInstacne();
			RSAccessPersistence s = (RSAccessPersistence) service;
			int type = event.getType();
			switch (type) {
			case ServiceEvent.MODIFIED: {
				m.setRSAccessPersistenceService(s);
				break;
			}
			case ServiceEvent.REGISTERED: {
				m.setRSAccessPersistenceService(s);
				break;
			}
			case ServiceEvent.UNREGISTERING: {
				m.setRSAccessPersistenceService(null);
				break;
			}
			}
		}
	}

	private boolean isRSAccessPersistenceService(Object service) {
		return RSAccessPersistence.class.isAssignableFrom(service
				.getClass());
	}
}
