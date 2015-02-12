package com.riambsoft.core.config;

public interface ConfigPersistence {

	public void writeConfigAttribute(String name, String attribute, Object value)
			throws ConfigPersistenceException;

	public Object readConfigAttribute(String name, String attribute)
			throws ConfigPersistenceException;
}
