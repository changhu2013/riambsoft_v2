package com.riambsoft.core.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.riambsoft.core.RSMethod;
import com.riambsoft.core.access.RSAccessManager;
import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;
import com.riambsoft.framework.core.web.ServiceController;
import com.riambsoft.framework.core.web.ServiceControllerManager;

public class RSService {

	private Log logger = LogFactory.getLog(RSService.class);

	private String controllerName;

	private String serviceName;

	private Object service;

	private static final String CONTROL = "CONTROL";

	private static final String METHOD = "METHOD";

	private static final String DESC = "DESC";

	public RSService(Object service) {
		super();
		this.service = service;
	}

	/**
	 * 注册RS服务
	 */
	public void init() {
		if (service == null) {
			logger.debug("RSService is null");
			return;
		}

		logger.debug("RSService init");
		// 注册服务开始
		Class<?> clazz = service.getClass();
		controllerName = getRSControllerName(clazz);
		if (controllerName == null) {
			logger.debug("RSService controllerName is null");
			return;
		}

		serviceName = getRSServiceName(clazz);

		if (serviceName == null) {
			serviceName = clazz.getSimpleName().toLowerCase();
		}
		logger.debug("RSService serviceName " + serviceName);
		ServiceController controller = ServiceControllerManager.getInstance()
				.getServiceController(controllerName);

		if (controller != null) {
			controller.addService(serviceName, service);
			logger.info("注册RS服务:" + clazz.getName());
		}

		// 获取该服务所有的业务方法,判断该业务方法是否进行访问控制,并将其信息持久化到服务器
		List<Map<String, String>> ms = getRSMethodName(clazz);
		RSAccessManager manager = RSAccessManager.getInstacne();
		if (ms != null && ms.size() > 0) {
			for (Map<String, String> m : ms) {

				String method = m.get(METHOD);
				boolean control = "true".equals(m.get(CONTROL));
				String desc = m.get(DESC);
				
				manager.sync(controllerName, serviceName, method, control, desc);
			}
		}
	}

	public void destroy() {
		logger.debug("RSService destroy");
		if (service != null && controllerName != null) {
			ServiceController controller = ServiceControllerManager
					.getInstance().getServiceController(controllerName);
			if (controller != null) {
				controller.removeService(serviceName, service);
			}
		}
	}

	private String getRSControllerName(Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		if (clazz.isAnnotationPresent(RSController.class)) {
			RSController rsc = (RSController) clazz
					.getAnnotation(RSController.class);
			return rsc.value();
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> inters : interfaces) {
			String name = getRSControllerName(inters);
			if (name != null) {
				return name;
			}
		}
		return getRSControllerName(clazz.getSuperclass());
	}

	private String getRSServiceName(Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		if (clazz.isAnnotationPresent(RSServiceName.class)) {
			RSServiceName rsn = (RSServiceName) clazz
					.getAnnotation(RSServiceName.class);
			return rsn.value();
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> inters : interfaces) {
			String name = getRSServiceName(inters);
			if (name != null) {
				return name;
			}
		}
		return getRSServiceName(clazz.getSuperclass());
	}

	private List<Map<String, String>> getRSMethodName(Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		if (clazz.isAnnotationPresent(RSController.class)) {
			Method[] ms = clazz.getMethods();
			List<Map<String, String>> ps = new ArrayList<Map<String, String>>();
			for (Method m : ms) {
				if (m.isAnnotationPresent(RSMethod.class)) {
					Map<String, String> p = new HashMap<String, String>();
					p.put(METHOD, m.getName());
					RSMethod rsm = m.getAnnotation(RSMethod.class);
					if (rsm.accessControl()) {
						String desc = rsm.accessDesc();
						p.put(CONTROL, "true");
						p.put(DESC, desc);
					} else {
						p.put(CONTROL, "false");
					}
					ps.add(p);
				}
			}
			return ps;
		}
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> inters : interfaces) {
			List<Map<String, String>> ps = getRSMethodName(inters);
			if (ps != null) {
				return ps;
			}
		}
		return getRSMethodName(clazz.getSuperclass());
	}

}
