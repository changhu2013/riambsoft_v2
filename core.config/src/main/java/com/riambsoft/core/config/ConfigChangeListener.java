package com.riambsoft.core.config;

import javax.management.AttributeList;

public interface ConfigChangeListener {

	public void onInit(Config config) throws ConfigException;

	public void onDestroy(Config config) throws ConfigException;

	public void onAttributeChange(Config config, String attribute, Object value)
			throws ConfigException;

	public void onAttributesChange(Config config, AttributeList attributes)
			throws ConfigException;

	public void onDoAction(Config config, String action, Object[] params)
			throws ConfigException;
}
