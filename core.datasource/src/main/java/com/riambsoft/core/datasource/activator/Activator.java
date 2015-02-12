package com.riambsoft.core.datasource.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;

import com.riambsoft.core.datasource.DataSourceManager;
import com.riambsoft.core.datasource.DataSourceProvider;

public class Activator implements BundleActivator, ServiceListener,
		SynchronousBundleListener {

	private BundleContext context;

	public void start(BundleContext context) throws Exception {

		context.addServiceListener(this);
		context.addBundleListener(this);

		this.context = context;
	}

	// 当该Bundle关闭时,销毁数据源管理器
	public void stop(BundleContext context) throws Exception {
		DataSourceManager.getInstance().destroy();
	}

	// 监听Bundle状态变化事件,将该Bundle启动之前,由其他Bundle提供的数据源服务纳入管理器
	public void bundleChanged(BundleEvent event) {
		if (event.getBundle().equals(context.getBundle())) {
			int type = event.getType();
			switch (type) {
			case BundleEvent.STARTED: {
				this.addDataSourceProvider();
				break;
			}
			}
		}
	}

	// 将已经创建的数据源服务纳入管理器
	private void addDataSourceProvider() {
		DataSourceManager manager = DataSourceManager.getInstance();
		try {
			ServiceReference[] references = this.context.getServiceReferences(
					DataSourceProvider.class.getName(), null);
			for (ServiceReference reference : references) {
				Object service = this.context.getService(reference);
				if (isDataSourceProviderService(service)) {
					manager.addDataSourceProvider((DataSourceProvider) service);
				}
			}
		} catch (InvalidSyntaxException e) {
		}
	}

	// 监听服务变化事件,如果有数据源服务状态发生变化则更新数据源服务管理器
	public void serviceChanged(ServiceEvent event) {
		Object service = this.context.getService(event.getServiceReference());
		if (isDataSourceProviderService(service)) {
			DataSourceManager manager = DataSourceManager.getInstance();
			DataSourceProvider provider = (DataSourceProvider) service;
			int type = event.getType();
			switch (type) {
			case ServiceEvent.MODIFIED: {
				manager.removeDataSourceProvider(provider);
				manager.addDataSourceProvider(provider);
				break;
			}
			case ServiceEvent.REGISTERED: {
				manager.addDataSourceProvider(provider);
				break;
			}
			case ServiceEvent.UNREGISTERING: {
				manager.removeDataSourceProvider(provider);
				break;
			}
			}
		}
	}

	// 判断服务是否为数据源服务实例
	private boolean isDataSourceProviderService(Object service) {
		return DataSourceProvider.class.isAssignableFrom(service.getClass());
	}

}
