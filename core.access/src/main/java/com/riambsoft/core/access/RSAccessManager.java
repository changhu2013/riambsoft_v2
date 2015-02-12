package com.riambsoft.core.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RSAccessManager {

	private static final String CONTROLLER = "controller";

	private static final String SERVICE = "service";

	private static final String METHOD = "method";

	private static final String CONTROL = "control";

	private static final String DESC = "desc";

	private static RSAccessManager manager;

	private RSAccessPersistence service;

	private List<Map<String, String>> cache;

	private RSAccessManager() {
		super();
		cache = new ArrayList<Map<String, String>>();
	}

	public static synchronized RSAccessManager getInstacne() {
		if (manager == null) {
			manager = new RSAccessManager();
		}
		return manager;
	}

	public void setRSAccessPersistenceService(RSAccessPersistence service) {
		this.service = service;
		clearCache();
	}

	public void clearCache() {
		if (this.service != null) {
			synchronized (cache) {
				if (cache.size() > 0) {
					for (Iterator<Map<String, String>> iter = cache.iterator(); iter
							.hasNext();) {
						Map<String, String> temp = iter.next();
						String c = temp.get(CONTROLLER);
						String s = temp.get(SERVICE);
						String m = temp.get(METHOD);
						boolean cl = "true".equals(temp.get(CONTROL));
						String d = temp.get(DESC);

						try {
							service.save(c, s, m, cl, d);
						} catch (RSAccessException e) {
							// TODO : 持久化时抛出异常处理
							e.printStackTrace();
						}
					}
				}
				cache.clear();
			}
		}
	}

	public void sync(String controllerName, String serviceName,
			String methodName, boolean control, String desc) {
		if (service != null) {
			try {
				service.save(controllerName, serviceName, methodName, control,
						desc);
			} catch (RSAccessException e) {
				// TODO : 持久化时抛出异常处理
				e.printStackTrace();
			}
		} else {
			Map<String, String> temp = new HashMap<String, String>();

			temp.put(CONTROLLER, controllerName);
			temp.put(SERVICE, serviceName);
			temp.put(METHOD, methodName);
			temp.put(CONTROL, control ? "true" : "false");
			temp.put(DESC, desc);

			cache.add(temp);
		}
	}
}
