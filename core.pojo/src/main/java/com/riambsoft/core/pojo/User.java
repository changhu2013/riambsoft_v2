package com.riambsoft.core.pojo;

/**
 * 用户类
 * 
 * TODO : 这里只列举了几个关键属性，还需完善其他属性
 */
public class User extends RsPojo {

	private static final long serialVersionUID = -4850555850098629774L;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 密码,注意这里是加密后的密文
	 */
	private String password;

	/**
	 * 描述
	 */
	private String remark;

	public User() {
		super();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
