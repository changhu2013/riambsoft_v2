package com.riambsoft.core.log;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class LogFactory {

	private static BundleContext bundleContext;

	private static EventAdmin eventAdmin;

	private static ServiceRegistration serviceReg;

	private LogFactory() {
		super();
	}

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static Log getLog(String name) {
		if (bundleContext == null) {
			int i = 5;
			do {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				Log log = getLog(name);
				if (log != null) {
					return log;
				}
			} while (bundleContext == null && (i--) > 0);
			return null;
		} else {
			return new Log(name, getEventAdmin(bundleContext));
		}
	}

	public static void init(BundleContext context) throws LogException {
		bundleContext = context;
		LogFactoryConfig config = LogFactoryConfig.getInstance();
		setLogHandler(config.getHandler());
	}

	private static EventAdmin getEventAdmin(BundleContext context) {
		if (eventAdmin == null) {
			ServiceReference ref = context.getServiceReference(EventAdmin.class
					.getName());
			eventAdmin = (EventAdmin) context.getService(ref);
		}
		return eventAdmin;
	}

	// 修改日志Handler
	public static void setLogHandler(String logHandler) throws LogException {

		// 如果bundleContext为空表示为初始化
		if (bundleContext == null) {
			return;
		}

		// 设置新的Handler服务
		LogHandler handler = null;
		try {
			Class<?> handlerClass = Activator.getBundleContext().getBundle()
					.loadClass(logHandler);
			handler = (LogHandler) handlerClass.newInstance();
		} catch (Exception e) {
			throw new LogException(e);
		}

		Dictionary<String, String> dict = new Hashtable<String, String>();
		dict.put(EventConstants.EVENT_TOPIC, LogEvent.LOG_TOPIC);

		if (handler != null) {
			if (serviceReg != null) {

				// 销毁原有Handler
				ServiceReference oldRef = serviceReg.getReference();
				if (oldRef != null) {
					LogHandler oldHandler = (LogHandler) bundleContext
							.getService(oldRef);
					if (oldHandler != null) {
						oldHandler.destroy();
					}
				}

				// 卸载原有Handler
				serviceReg.unregister();
				serviceReg = null;
			}

			// 初始化Handler
			handler.init();
			serviceReg = bundleContext.registerService(
					EventHandler.class.getName(), handler, dict);

			if (serviceReg == null) {
				throw new LogException("serviceReg is null");
			}

		} else {
			throw new LogException("handler is null");
		}
	}

	public static void destroy() {
		// 如果bundleContext为空表示未初始化
		if (bundleContext == null) {
			return;
		}

		if (serviceReg != null) {
			serviceReg.unregister();
			serviceReg = null;
		}

		bundleContext = null;
	}
}
