package com.riambsoft.core.config;

import com.riambsoft.core.config.persistent.FileConfigPersistence;

@RSDesc("Config Persistence file: classes/config/com_riambsoft_core_config_ConfigPersistentConfig.properties")
public class ConfigContainerConfig extends Config {

	private String defaultPersistenceClassName = FileConfigPersistence.class
			.getName();

	private String persistenceClassName;

	private static ConfigContainerConfig instance;

	private ConfigPersistence configPersistence;

	private ConfigContainerConfig() {
		super();
	}

	public static ConfigContainerConfig getInstance() {
		if (instance == null) {
			instance = new ConfigContainerConfig();
			instance.init();
		}
		return instance;
	}

	@RSGetter("default_persistence_class")
	public String getDefaultPersistenceClassName() {
		return defaultPersistenceClassName;
	}

	@RSGetter("persistence_class")
	public String getPersistenceClassName() {
		return persistenceClassName;
	}

	@RSSetter("persistence_class")
	public void setPersistenceClassName(String persistenceClassName) {
		this.persistenceClassName = persistenceClassName;
		this.configPersistence = null;
	}

	public ConfigPersistence getConfigPersistence() throws ConfigException {
		if (configPersistence == null) {
			String className = getPersistenceClassName();
			if (className == null || "".equals(className.trim())) {
				className = getDefaultPersistenceClassName();
			}
			Class<?> clazz = null;
			try {
				clazz = Activator.getBundleContext().getBundle()
						.loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new ConfigException(e);
			}
			if (ConfigPersistence.class.isAssignableFrom(clazz)) {
				try {
					configPersistence = (ConfigPersistence) clazz.newInstance();
				} catch (InstantiationException e) {
					throw new ConfigException(e);
				} catch (IllegalAccessException e) {
					throw new ConfigException(e);
				}
			} else {
				throw new ConfigException("未能找到配置服务持久化实现类");
			}
		}
		return configPersistence;
	}

}
