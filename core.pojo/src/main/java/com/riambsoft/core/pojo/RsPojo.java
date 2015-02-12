package com.riambsoft.core.pojo;

import java.io.Serializable;

/**
 * 系统所有持久化对象的基类, 每个持久化对象都具有一个系统唯一的ID
 */
public class RsPojo implements Serializable {

	private static final long serialVersionUID = -4980523789179783865L;

	/**
	 * ID,该ID为系统唯一,通过该ID可确定唯一的一个业务对象
	 */
	private String id;

	public RsPojo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id == null || "".equals(id.trim())) {
			throw new RuntimeException("ID不能为空");
		}
		this.id = id;
	}

	public String toString() {
		return "RSObject ID:" + getId();
	}

	public boolean equals(Object obj) {
		if (obj != null && RsPojo.class.isAssignableFrom(obj.getClass())) {
			String id = ((RsPojo) obj).getId();
			if (id != null && id.equals(getId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
