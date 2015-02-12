package com.riambsoft.core.pojo;

public class RsApp extends RsPojo {

	private static final long serialVersionUID = 6043580906036925388L;

	public enum Types {
		WEBAPP, ANDROIE, IOS 
	};
	
	/**
	 * 子系统编码
	 */
	private String system;

	/**
	 * 应用程序编码
	 */
	private String code;

	/**
	 * 应用程序名称
	 */
	private String name;

	/**
	 * 应用程序类型
	 */
	private Types type;

	/**
	 * 描述信息
	 */
	private String remark;

	public RsApp() {
		super();
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
