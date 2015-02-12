package com.riambsoft.core;

import org.junit.Test;

import com.riambsoft.framework.core.ServiceException;

public class TestRSServiceException {

	@Test
	public void a() {
		try {
			b();
		} catch (ServiceException e) {
			System.out.println(e.getStatus() + "  " + e.getMessage());
		}
	}

	public void b() {
		RSServiceException e = new RSServiceException("SYS10000", "异常信息");
		throw e;
	}

}
