package com.riambsoft.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

public abstract class Config implements DynamicMBean {

	private ConfigContainer container = ConfigContainer.getInstance();

	private ConfigPersistence configPersistence;

	private Map<String, Map<String, Object>> attributes;

	private Map<String, Object> operations;

	private MBeanInfo mBeanInfo;

	private List<ConfigChangeListener> listeners = Collections
			.synchronizedList(new ArrayList<ConfigChangeListener>());

	public Config() {
		super();
		attributes = new HashMap<String, Map<String, Object>>();
		operations = new HashMap<String, Object>();
	}

	public void setConfigPersistence(ConfigPersistence configPersistence) {
		this.configPersistence = configPersistence;
	}

	public ConfigPersistence getConfigPersistence() throws ConfigException {
		if (configPersistence == null) {
			configPersistence = ConfigContainerConfig.getInstance()
					.getConfigPersistence();
		}
		return configPersistence;
	}

	public void init() {
		Class<?> clazz = this.getClass();
		if (Config.class.isAssignableFrom(clazz)) {
			List<MBeanOperationInfo> operationInfos = new ArrayList<MBeanOperationInfo>();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(RSSetter.class)) {
					RSSetter setAttr = method.getAnnotation(RSSetter.class);
					String attr = setAttr.value();
					attr = attr.replace(" ", "_");
					Map<String, Object> attrs = attributes.get(attr);
					if (attrs == null) {
						attrs = new HashMap<String, Object>();
						attributes.put(attr, attrs);
					}
					attrs.put("writable", "true");
					attrs.put("setter", method);
				} else if (method.isAnnotationPresent(RSGetter.class)) {
					RSGetter getAttr = method.getAnnotation(RSGetter.class);
					String attr = getAttr.value();
					attr = attr.replace(" ", "_");
					Map<String, Object> attrs = attributes.get(attr);
					if (attrs == null) {
						attrs = new HashMap<String, Object>();
						attributes.put(attr, attrs);
					}
					attrs.put("type", method.getReturnType().getName());
					attrs.put("readable", "true");
					attrs.put("getter", method);
					if (method.isAnnotationPresent(RSDesc.class)) {
						RSDesc desc = method.getAnnotation(RSDesc.class);
						attrs.put("description", desc.value());
					}
				} else if (method.isAnnotationPresent(RSAction.class)) {

					String name = method.getName();

					String description = "";
					if (method.isAnnotationPresent(RSDesc.class)) {
						RSDesc desc = method.getAnnotation(RSDesc.class);
						description = desc.value();
					}

					Class<?>[] types = method.getParameterTypes();
					MBeanParameterInfo[] paramInfos = new MBeanParameterInfo[types.length];

					RSAction action = method.getAnnotation(RSAction.class);
					String[] params = action.params();
					Annotation[][] anns = method.getParameterAnnotations();
					Annotation[] paramDescs = new Annotation[types.length];
					int idx = 0;
					for (Annotation[] ann : anns) {
						for (Annotation an : ann) {
							if (an.annotationType().equals(RSDesc.class)) {
								paramDescs[idx] = an;
								break;
							}
						}
						idx++;
					}

					for (int i = 0; i < types.length; i++) {
						String paramName = "param_" + i;
						if (params != null && params.length > i
								&& params[i] != null && !"".equals(params[i])) {
							paramName = params[i];
						}
						String desc = null;
						if (paramDescs[i] != null) {
							desc = ((RSDesc) paramDescs[i]).value();
						}
						paramInfos[i] = new MBeanParameterInfo(paramName,
								types[i].getName(), desc);
					}

					MBeanOperationInfo operation = new MBeanOperationInfo(name,
							description, paramInfos, method.getReturnType()
									.getName(), MBeanOperationInfo.ACTION);

					operations.put(name, method);
					operationInfos.add(operation);
				}
			}

			List<MBeanAttributeInfo> attributeInfos = new ArrayList<MBeanAttributeInfo>();
			for (Iterator<String> iter = attributes.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				Map<String, Object> attr = attributes.get(name);

				MBeanAttributeInfo attribute = new MBeanAttributeInfo(name,
						(String) attr.get("type"),
						(String) attr.get("description"), "true".equals(attr
								.get("readable")), "true".equals(attr
								.get("writable")), false);

				attributeInfos.add(attribute);
			}

			String description = "";
			if (clazz.isAnnotationPresent(RSDesc.class)) {
				RSDesc desc = (RSDesc) clazz.getAnnotation(RSDesc.class);
				description = desc.value();
			}

			mBeanInfo = new MBeanInfo(
					clazz.getName(),
					description,
					(MBeanAttributeInfo[]) (attributeInfos
							.toArray(new MBeanAttributeInfo[attributes.size()])),
					new MBeanConstructorInfo[0],
					(MBeanOperationInfo[]) (operationInfos
							.toArray(new MBeanOperationInfo[operations.size()])),
					new MBeanNotificationInfo[0]);

			container.addConfig(this);

			// 读取持久化存储的值并设置到该实例
			try {
				ConfigPersistence configPersistence = getConfigPersistence();
				for (Iterator<String> iter = attributes.keySet().iterator(); iter
						.hasNext();) {
					String attribute = iter.next();
					Object value = configPersistence.readConfigAttribute(
							getClass().getName(), attribute);
					Map<String, Object> attrs = attributes.get(attribute);
					if (value != null && "true".equals(attrs.get("writable"))) {
						Method setter = (Method) attrs.get("setter");

						value = marshal(value, setter.getParameterTypes()[0]);
						setter.invoke(this, new Object[] { value });
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeOperationsException(
						new IllegalArgumentException(e));
			}
		}

		// 执行监听着init方法
		for (ConfigChangeListener listener : listeners) {
			try {
				listener.onInit(this);
			} catch (ConfigException e) {
			}
		}
	}

	public void destroy() {
		for (ConfigChangeListener listener : listeners) {
			try {
				listener.onDestroy(this);
			} catch (ConfigException e) {
			}
		}
		container.removeConfig(this);
		synchronized (listeners) {
			listeners.clear();
		}
	}

	public void addChangeListener(ConfigChangeListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"属性名不能为空"));
		}
		Map<String, Object> attrs = attributes.get(attribute);
		if (attrs != null) {
			Method getter = (Method) attrs.get("getter");
			try {
				return getter.invoke(this, new Object[] {});
			} catch (Exception e) {
				throw new RuntimeOperationsException(
						new IllegalArgumentException("获取属性" + attribute
								+ "发生异常", e));
			}
		} else {
			throw (new AttributeNotFoundException("未能在类"
					+ this.getClass().getName() + "找到属性" + attribute));
		}

	}

	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"属性不能为空"));
		}

		String name = attribute.getName();
		Object value = attribute.getValue();
		if (name == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"属性名不能为空"));
		}
		Map<String, Object> attrs = attributes.get(name);
		if (attrs != null) {
			Method setter = (Method) attrs.get("setter");
			try {
				setter.invoke(this, new Object[] { value });
			} catch (Exception e) {
				throw new RuntimeOperationsException(
						new IllegalArgumentException("设置属性" + attribute
								+ "发生异常", e));
			}
		}

		// 执行监听着onAttributeChange方法
		for (ConfigChangeListener listener : listeners) {
			try {
				listener.onAttributeChange(this, name, value);
			} catch (ConfigException e) {
			}
		}

		// 将配置信息持久化
		try {
			getConfigPersistence().writeConfigAttribute(getClass().getName(),
					name, value);
		} catch (ConfigPersistenceException e) {
			throw new RuntimeOperationsException(
					new IllegalArgumentException(e));
		} catch (ConfigException e) {
			throw new RuntimeOperationsException(
					new IllegalArgumentException(e));
		}
	}

	public AttributeList getAttributes(String[] attributes) {
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"属性名称不能为空"));
		}
		AttributeList list = new AttributeList();
		if (attributes.length == 0) {
			return list;
		}
		for (int i = 0; i < attributes.length; i++) {
			try {
				Object value = getAttribute((String) attributes[i]);
				list.add(new Attribute(attributes[i], value));
			} catch (Exception e) {
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public AttributeList setAttributes(AttributeList attributes) {

		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"属性不能为空"));
		}
		AttributeList list = new AttributeList();
		if (attributes.isEmpty()) {
			return list;
		}
		for (Iterator i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				list.add(new Attribute(name, value));
			} catch (Exception e) {
			}
		}

		// 执行监听着onAttributesChange方法
		for (ConfigChangeListener listener : listeners) {
			try {
				listener.onAttributesChange(this, attributes);
			} catch (ConfigException e) {
			}
		}
		return list;
	}

	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {

		if (actionName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"操作名称不能为空"));
		}
		Method action = (Method) operations.get(actionName);
		if (action != null) {
			try {
				Object result = action.invoke(this, params);

				// 执行监听着onDoAction方法
				for (ConfigChangeListener listener : listeners) {
					try {
						listener.onDoAction(this, actionName, params);
					} catch (ConfigException e) {
					}
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public MBeanInfo getMBeanInfo() {
		return mBeanInfo;
	}

	private Object marshal(Object bean, Class<?> clazz) {
		if (clazz.isAssignableFrom(bean.getClass())) {
			return bean;
		}
		if (clazz.isPrimitive()) {
			return this.castPrimitive(bean, clazz);
		} else if (this.isWrapperPrimitiveClass(clazz)) {
			return this.castWrapperPrimitive(bean, clazz);
		} else if (clazz.isAssignableFrom(String.class)) {
			return bean.toString();
		} else if (clazz.isAssignableFrom(StringBuffer.class)) {
			return new StringBuffer(bean.toString());
		} else if (clazz.isAssignableFrom(StringBuilder.class)) {
			return new StringBuilder(bean.toString());
		} else {
			return null;
		}
	}

	private boolean isWrapperPrimitiveClass(Class<?> clazz) {
		if (clazz.isAssignableFrom(Integer.class)
				|| clazz.isAssignableFrom(Short.class)
				|| clazz.isAssignableFrom(Character.class)
				|| clazz.isAssignableFrom(Float.class)
				|| clazz.isAssignableFrom(Long.class)
				|| clazz.isAssignableFrom(Double.class)
				|| clazz.isAssignableFrom(Boolean.class)
				|| clazz.isAssignableFrom(Byte.class)) {
			return true;
		}
		return false;
	}

	private Object castPrimitive(Object bean, Class<?> clazz) {
		if (clazz.isAssignableFrom(Byte.TYPE)) {
			if (bean == null) {
				return new Byte((byte) 0);
			} else {
				return new Byte("" + bean);
			}
		} else if (clazz.isAssignableFrom(Short.TYPE)) {
			if (bean == null) {
				return new Short((short) 0);
			} else {
				return new Short("" + bean);
			}
		} else if (clazz.isAssignableFrom(Character.TYPE)) {
			if (bean != null && ("" + bean).length() >= 1) {
				return new Character(("" + bean).charAt(0));
			} else {
				return new Character((char) 0);
			}
		} else if (clazz.isAssignableFrom(Float.TYPE)) {
			if (bean == null) {
				return new Float((float) 0);
			} else {
				return new Float("" + bean);
			}
		} else if (clazz.isAssignableFrom(Integer.TYPE)) {
			if (bean == null) {
				return new Integer(0);
			} else {
				return new Integer("" + bean);
			}
		} else if (clazz.isAssignableFrom(Long.TYPE)) {
			if (bean == null) {
				return new Long((long) 0);
			} else {
				return new Long("" + bean);
			}
		} else if (clazz.isAssignableFrom(Double.TYPE)) {
			if (bean == null) {
				return new Double(0.0);
			} else {
				return new Double("" + bean);
			}
		} else if (clazz.isAssignableFrom(Boolean.TYPE)) {
			if (bean == null) {
				return new Boolean(false);
			} else {
				return new Boolean("" + bean);
			}
		} else {
			return bean;
		}
	}

	/**
	 * 处理基本数据类型的包装类
	 * 
	 * @param bean
	 * @param clazz
	 * @return
	 */
	private Object castWrapperPrimitive(Object bean, Class<?> clazz) {
		if (bean == null) {
			return null;
		}
		if (clazz.isAssignableFrom(Byte.class)) {
			return new Byte("" + bean);
		} else if (clazz.isAssignableFrom(Integer.class)) {
			return new Integer("" + bean);
		} else if (clazz.isAssignableFrom(Character.class)) {
			if (("" + bean).length() >= 1) {
				return new Character(("" + bean).charAt(0));
			} else {
				return null;
			}
		} else if (clazz.isAssignableFrom(Float.class)) {
			return new Float("" + bean);
		} else if (clazz.isAssignableFrom(Double.class)) {
			return new Double("" + bean);
		} else if (clazz.isAssignableFrom(Long.class)) {
			return new Long("" + bean);
		} else if (clazz.isAssignableFrom(Boolean.class)) {
			return new Boolean("" + bean);
		} else {
			return bean;
		}
	}

}
