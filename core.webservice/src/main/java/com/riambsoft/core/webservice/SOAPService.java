package com.riambsoft.core.webservice;

import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;
import com.riambsoft.framework.core.webservice.SOAPServiceAbstract;

public class SOAPService extends SOAPServiceAbstract {

	private Log logger = LogFactory.getLog(SOAPService.class);

	public SOAPService(Object service) {
		super(service);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

}
