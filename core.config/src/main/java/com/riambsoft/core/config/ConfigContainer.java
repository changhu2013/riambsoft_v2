package com.riambsoft.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.AttributeList;

public class ConfigContainer {

	private static ConfigContainer instance;

	private Map<String, Object> configs;

	private List<ConfigManager> managers;

	private ConfigContainer() {
		super();
		configs = Collections.synchronizedMap(new HashMap<String, Object>());
		managers = Collections.synchronizedList(new ArrayList<ConfigManager>());
	}

	public static ConfigContainer getInstance() {
		if (instance == null) {
			instance = new ConfigContainer();
			instance.init();
		}
		return instance;
	}

	// 初始化基础配置服务
		public void init() {
			ConfigContainerConfig config = ConfigContainerConfig.getInstance();
			config.addChangeListener(new ConfigChangeListener() {

				public void onInit(Config config) throws ConfigException {
				}

				public void onDestroy(Config config) throws ConfigException {
				}

				public void onAttributeChange(Config config, String attribute,
						Object value) throws ConfigException {
				}

				public void onAttributesChange(Config config,
						AttributeList attributes) throws ConfigException {
					synchronized (configs) {
						for (Iterator<String> iter = configs.keySet().iterator(); iter
								.hasNext();) {
							String name = iter.next();
							Object cfg = configs.get(name);
							if (!config.equals(cfg)) {
								if (Config.class.isAssignableFrom(cfg.getClass())) {
									((Config) cfg).setConfigPersistence(null);
								} else {
									// TODO:尚未支持Object类型的配置
								}
							}
						}
					}
				}

				public void onDoAction(Config config, String action, Object[] params)
						throws ConfigException {
				}

			});
		}
	
	public void addConfig(Config config) {
		String name = config.getClass().getName();
		synchronized (configs) {
			configs.put(name, config);
		}
		synchronized (managers) {
			for (ConfigManager manager : managers) {
				manager.addConfig(name, config);
			}
		}
	}

	public void addConfig(String name, Object config) {
		synchronized (configs) {
			configs.put(name, config);
		}
		synchronized (managers) {
			for (ConfigManager manager : managers) {
				manager.addConfig(name, config);
			}
		}
	}

	public void removeConfig(Config config) {
		String name = config.getClass().getName();
		synchronized (managers) {
			for (ConfigManager manager : managers) {
				manager.removeConfig(name);
			}
		}
		synchronized (configs) {
			configs.remove(name);
		}
	}

	public void removeConfig(String name) {
		synchronized (managers) {
			for (ConfigManager manager : managers) {
				manager.removeConfig(name);
			}
		}
		synchronized (configs) {
			configs.remove(name);
		}
	}

	public void addManager(ConfigManager manager) {
		synchronized (configs) {
			for (Iterator<String> iter = configs.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				Object config = configs.get(name);
				manager.addConfig(name, config);
			}
		}
		synchronized (managers) {
			managers.add(manager);
		}
	}

	public void removeManager(ConfigManager manager) {
		synchronized (configs) {
			for (Iterator<String> iter = configs.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				manager.removeConfig(name);
			}
		}
		synchronized (managers) {
			managers.remove(manager);
		}
	}

}
