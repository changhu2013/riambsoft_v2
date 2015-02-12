package com.riambsoft.core.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.riambsoft.core.RSParam;
import com.riambsoft.core.ServiceEngine;
import com.riambsoft.framework.core.VariablePool;
import com.riambsoft.framework.core.web.HttpServletVariablePool;
import com.riambsoft.framework.core.web.ServiceController;

public class WebServiceEngine extends ServiceEngine {

	@Override
	public Object[] getMethodArgs(Method method, VariablePool pool) {

		Class<?>[] types = method.getParameterTypes();
		Object[] args = new Object[types.length];
		int idx = 0;
		Annotation[][] ann = method.getParameterAnnotations();
		for (Annotation[] an : ann) {
			for (Annotation a : an) {
				Class<? extends Annotation> at = a.annotationType();
				if (at.equals(RSParam.class)) {
					RSParam p = (RSParam) a;

					args[idx] = pool.getValue(p.value(), types[idx]);
					idx++;
				} else if (at.equals(RSParamHttpSession.class)) {

					args[idx] = pool.getValue(
							HttpServletVariablePool.PARAMETER_HTTP_SESSION,
							types[idx]);
					idx++;
				} else if (at.equals(RSParamHttpServletRequest.class)) {

					args[idx] = pool.getValue(
							HttpServletVariablePool.PARAMETER_HTTP_REQUEST,
							types[idx]);
					idx++;
				} else if (at.equals(RSParamHttpServletResponse.class)) {

					args[idx] = pool.getValue(
							HttpServletVariablePool.PARAMETER_HTTP_RESPONSE,
							types[idx]);
					idx++;
				} else if (at.equals(RSParamUser.class)) {

					args[idx] = pool.getValue(
							ServiceController.HTTP_SESSION_KEY_FOR_USER,
							types[idx]);
					idx++;
				}
			}
		}
		return args;
	}

}
