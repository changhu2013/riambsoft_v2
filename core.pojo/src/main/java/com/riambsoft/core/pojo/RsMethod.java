package com.riambsoft.core.pojo;

/**
 * 权限
 */
public class RsMethod extends RsPojo {

	private static final long serialVersionUID = 8583155379853801449L;

	/**
	 * 控制器名称
	 */
	private String controller;

	/**
	 * 服务名称
	 */
	private String service;

	/**
	 * 方法名称
	 */
	private String method;

	/**
	 * 是否受访问控制
	 */
	private boolean control;

	/**
	 * 描述
	 */
	private String remark;

	public RsMethod() {
		super();
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isControl() {
		return control;
	}

	public void setControl(boolean control) {
		this.control = control;
	}

	public String getRemark() {
		return remark == null ? "" : remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
