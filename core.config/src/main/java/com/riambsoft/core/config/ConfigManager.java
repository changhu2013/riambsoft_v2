package com.riambsoft.core.config;

public interface ConfigManager {

	public void addConfig(String name, Object config);

	public void removeConfig(String name);

	public void start();

	public void stop();

}
