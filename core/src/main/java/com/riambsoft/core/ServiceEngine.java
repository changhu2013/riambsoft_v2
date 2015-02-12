package com.riambsoft.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;
import com.riambsoft.framework.core.ServiceEngineInterface;
import com.riambsoft.framework.core.ServiceException;
import com.riambsoft.framework.core.Variable;
import com.riambsoft.framework.core.VariablePool;

public class ServiceEngine implements ServiceEngineInterface {

	private Log logger;

	public Variable invoke(Object service, String methodName, VariablePool pool) {

		if (logger == null) {
			logger = LogFactory.getLog(ServiceEngine.class);
		}

		logger.debug("执行 " + service.getClass().getName() + " 的业务方法 "
				+ methodName);

		return invokeObjectService(service, methodName, pool);
	}

	private Variable invokeObjectService(Object service, String methodName,
			VariablePool pool) {

		Method method = getServiceMethod(service.getClass(), methodName);

		if (method != null) {

			// 执行Before方法
			beforeInvokeObjectService(method, service, pool);

			// 获取参数值
			Object[] args = getMethodArgs(method, pool);

			Object value = null;

			try {
				value = method.invoke(service, args);
			} catch (Exception e) {
				if (e instanceof InvocationTargetException) {
					Throwable t = ((InvocationTargetException) e)
							.getTargetException();
					if (t instanceof ServiceException) {
						ServiceException temp = (ServiceException) t;

						String status = temp.getStatus();
						String message = temp.getMessage();

						throw new ServiceException(status, message, t);
					} else {
						throw new ServiceException(
								ServiceException.RSMETHOD_INVOKE_ERROR,
								t.getMessage(), t);
					}
				} else {
					throw new ServiceException(
							ServiceException.RSMETHOD_INVOKE_ERROR,
							e.getMessage(), e);
				}
			}

			Variable result = null;
			if (method.isAnnotationPresent(RSResult.class)) {
				RSResult rsResult = ((RSResult) method
						.getAnnotation(RSResult.class));
				String name = rsResult.value();

				result = pool.add(name, value);
			} else {

				result = new Variable("void", Void.class, null);
			}

			// 执行After方法
			afterInvokeObjectService(method, service, pool);

			// 返回当前方法的结果
			return result;
		} else {
			throw new ServiceException(
					ServiceException.RSMETHOD_NOTFOUND_ERROR, "未能在业务服务"
							+ service.getClass() + "中找到业务方法" + methodName);
		}
	}

	/**
	 * 获取参数值
	 * 
	 * @param method
	 * @param pool
	 * @return
	 */
	public Object[] getMethodArgs(Method method, VariablePool pool) {

		Class<?>[] types = method.getParameterTypes();
		Object[] args = new Object[types.length];
		int idx = 0;
		Annotation[][] ann = method.getParameterAnnotations();
		for (Annotation[] an : ann) {
			for (Annotation a : an) {
				if (a.annotationType().equals(RSParam.class)) {
					RSParam p = (RSParam) a;
					args[idx] = pool.getValue(p.value(), types[idx]);
					idx++;
				}
			}
		}
		return args;
	}

	private void beforeInvokeObjectService(Method method, Object service,
			VariablePool pool) {
		if (method.isAnnotationPresent(RSBeforeMethod.class)) {
			RSBeforeMethod beforeMethods = method
					.getAnnotation(RSBeforeMethod.class);
			String[] methodNames = beforeMethods.value();

			for (String methodName : methodNames) {
				logger.debug("执行前序业务方法" + methodName);
				invokeObjectService(service, methodName, pool);
			}
		}
	};

	private void afterInvokeObjectService(Method method, Object service,
			VariablePool pool) {
		if (method.isAnnotationPresent(RSAfterMethod.class)) {
			RSAfterMethod afterMethods = method
					.getAnnotation(RSAfterMethod.class);
			String[] methodNames = afterMethods.value();
			for (String methodName : methodNames) {
				logger.debug("执行后序业务方法" + methodName);
				invokeObjectService(service, methodName, pool);
			}
		}
	};

	private Method getServiceMethod(Class<?> clazz, String methodName) {
		if (Object.class.equals(clazz)) {
			return null;
		}
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)
					&& method.isAnnotationPresent(RSMethod.class)) {
				return method;
			}
		}

		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> inters : interfaces) {
			Method method = getServiceMethod(inters, methodName);
			if (method != null) {
				return method;
			}
		}

		return getServiceMethod(clazz.getSuperclass(), methodName);
	}

}
