package com.riambsoft.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装Ext的Store参数
 */
public class StoreData {

	//Store元数据
	public class StoreMetaData {

		private boolean loadMetaData;

		private Map paramNames;

		private Map data;

		public StoreMetaData() {
			this.data = new HashMap();
			this.initDefaultProperty(this.data);
		}

		public StoreMetaData(Map data) {
			this.data = data == null ? new HashMap() : data;
			this.initDefaultProperty(this.data);
		}

		private void initDefaultProperty(Map data) {
			String loadMetaData = (String) data.get("loadMetaData");
			this.loadMetaData = "true".equals(loadMetaData) ? true : false;
			this.setParamNames((Map) data.get("paramNames"));
			this.setSortInfo((Map) data.get("sortInfo"));
			this.setGroupInfo((Map) data.get("groupInfo"));
			this.setRoot((String) data.get("root"));
			this.setIdProperty((String) data.get("idProperty"));
			this.setTotalProperty((String) data.get("totalProperty"));
			this.setSuccessProperty((String) data.get("successProperty"));
			this.setMessageProperty((String) data.get("messageProperty"));
			this.setFields((List) data.get("fields"));
		}

		/**
		 * 是否加载metaData
		 * 
		 * @return
		 */
		public boolean isLoad() {
			return this.loadMetaData;
		}

		/**
		 * 设置参数名字
		 * 
		 * @param {@link Map} paramsNames
		 */
		public void setParamNames(Map paramNames) {
			paramNames = paramNames == null ? new HashMap() : paramNames;
			String start = (String) paramNames.get("start");
			if (start == null || "".equals(start)) {
				paramNames.put("start", "start");
			}
			String limit = (String) paramNames.get("limit");
			if (limit == null || "".equals(limit)) {
				paramNames.put("limit", "limit");
			}
			String sort = (String) paramNames.get("sort");
			if (sort == null || "".equals(sort)) {
				paramNames.put("sort", "sort");
			}
			String dir = (String) paramNames.get("dir");
			if (dir == null || "".equals(dir)) {
				paramNames.put("dir", "dir");
			}
			this.paramNames = paramNames;
		}

		/**
		 * 设置排序信息
		 * 
		 * @param sortInfo
		 */
		public void setSortInfo(Map sortInfo) {
			sortInfo = sortInfo == null ? new HashMap() : sortInfo;
			String sortProperty = (String) this.paramNames.get("sort");
			String dirProperty = (String) this.paramNames.get("dir");
			if (sortInfo.get(sortProperty) != null
					&& sortInfo.get(dirProperty) != null) {
				this.data.put("sortInfo", sortInfo);
			}
		}

		/**
		 * 设置分组信息
		 * 
		 * @param groupInfo
		 */
		public void setGroupInfo(Map groupInfo) {
			groupInfo = groupInfo == null ? new HashMap() : groupInfo;
			String groupBy = (String) groupInfo.get("groupBy");
			String groupDir = (String) groupInfo.get("groupDir");
			if (groupBy != null && groupDir != null) {
				this.data.put("groupInfo", groupInfo);
			}
		}

		/**
		 * 将字段 field 进行 ASC 排序
		 * 
		 * @param field
		 */
		public void setSortFieldASC(String field) {
			String sortProperty = (String) this.paramNames.get("sort");
			String dirProperty = (String) this.paramNames.get("dir");

			Map sortInfo = new HashMap();
			sortInfo.put(sortProperty, field);
			sortInfo.put(dirProperty, "ASC");
			this.data.put("sortInfo", sortInfo);
		}

		/**
		 * 将字段 field 进行 DESC 排序
		 * 
		 * @param field
		 */
		public void setSortFieldDESC(String field) {
			String sortProperty = (String) this.paramNames.get("sort");
			String dirProperty = (String) this.paramNames.get("dir");

			Map sortInfo = new HashMap();
			sortInfo.put(sortProperty, field);
			sortInfo.put(dirProperty, "DESC");
			this.data.put("sortInfo", sortInfo);
		}

		public void setRoot(String root) {
			this.data.put("root", root == null || "".equals(root) ? "data"
					: root);
		}

		public String getRoot() {
			return (String) this.data.get("root");
		}

		public String getSummaryDataProperty() {
			return "summaryData";
		}

		public String getGroupSummaryDataProperty() {
			return "groupSummaryData";
		}

		public void setIdProperty(String idProperty) {
			this.data.put("idProperty",
					idProperty == null || "".equals(idProperty) ? "id"
							: idProperty);
		}

		public String getIdProperty() {
			return (String) this.data.get("idProperty");
		}

		public void setTotalProperty(String totalProperty) {
			this.data.put("totalProperty",
					totalProperty == null || "".equals(totalProperty) ? "total"
							: totalProperty);
		}

		public String getTotalProperty() {
			return (String) this.data.get("totalProperty");
		}

		public void setSuccessProperty(String successProperty) {
			this.data
					.put("successProperty",
							successProperty == null
									|| "".equals(successProperty) ? "success"
									: successProperty);
		}

		public String getSuccessProperty() {
			return (String) this.data.get("successProperty");
		}

		public void setMessageProperty(String messageProperty) {
			this.data
					.put("messageProperty",
							messageProperty == null
									|| "".equals(messageProperty) ? "message"
									: messageProperty);
		}

		public String getMessageProperty() {
			return (String) this.data.get("messageProperty");
		}

		public void setFields(List fields) {
			this.data.put("fields", fields == null ? new ArrayList() : fields);
		}

		public List getFields() {
			return (List) this.data.get("fields");
		}

		/**
		 * 设置查询头字段
		 * 
		 * @param fields
		 */
		public void setQueryField(List fields) {
			this.data.put("queryFields", fields == null ? new ArrayList()
					: fields);
		}

		/**
		 * 获取排序字段名称<br/>
		 * 如果没有排序字段则返回null。<br/>
		 * 
		 * @return {@link String} sortField
		 */
		public String getSortField() {
			Map sortInfo = (Map) this.data.get("sortInfo");
			if (sortInfo != null) {
				String sortProperty = (String) this.paramNames.get("sort");
				String sort = (String) sortInfo.get(sortProperty);
				return sort == null || "".equals(sort) ? null : sort;
			} else {
				return null;
			}
		}

		/**
		 * 获取排序方式,返回值为 ASC 或 DESC 。
		 * 
		 * @return {@link String} dir
		 */
		public String getSortDir() {
			Map sortInfo = (Map) this.data.get("sortInfo");
			if (sortInfo != null) {
				String dirProperty = (String) this.paramNames.get("dir");
				String dir = (String) sortInfo.get(dirProperty);
				if (dir != null
						&& ("ASC".equals(dir.toUpperCase()) || "DESC"
								.equals(dir.toUpperCase()))) {
					return dir;
				} else {
					return "ASC";
				}
			} else {
				return "ASC";
			}
		}

		/**
		 * 获取分组字段名称
		 * 
		 * @return
		 */
		public String getGroupBy() {
			Map groupInfo = (Map) this.data.get("groupInfo");
			if (groupInfo != null) {
				String groupBy = (String) groupInfo.get("groupBy");
				return groupBy == null || "".equals(groupBy) ? null : groupBy;
			} else {
				return null;
			}
		}

		/**
		 * 获取分组排序类型
		 * 
		 * @return
		 */
		public String getGroupDir() {
			Map groupInfo = (Map) this.data.get("groupInfo");
			if (groupInfo != null) {
				String dir = (String) groupInfo.get("groupDir");
				if (dir != null
						&& ("ASC".equals(dir.toUpperCase()) || "DESC"
								.equals(dir.toUpperCase()))) {
					return dir;
				} else {
					return "ASC";
				}
			} else {
				return "ASC";
			}
		}

		/**
		 * 获取要查询数据开始位置
		 * 
		 * @return
		 */
		public Integer getStart() {
			String startProperty = (String) this.paramNames.get("start");
			Object start = this.data.get(startProperty);
			return start == null ? new Integer(0) : Integer.valueOf("" + start);
		}

		/**
		 * 设置要查询数据开始位置
		 * 
		 */
		public void setStart(Object start) {
			String startProperty = (String) this.paramNames.get("start");
			if (start == null) {
				this.data.remove(startProperty);
			} else {
				this.data.put(startProperty, start);
			}
		}

		/**
		 * 获取要查询数据条数限制数，如果返回为null则表示不限制。
		 * 
		 * @return
		 */
		public Integer getLimit() {
			String limitProperty = (String) this.paramNames.get("limit");
			Object limit = this.data.get(limitProperty);
			return limit == null ? null : Integer.valueOf("" + limit);
		}

		/**
		 * 设置要查询数据条数限制数，如果返回为null则表示不限制。
		 * 
		 */
		public void setLimit(Object limit) {
			String limitProperty = (String) this.paramNames.get("limit");
			if (limit == null) {
				this.data.remove(limitProperty);
			} else {
				this.data.put(limitProperty, limit);
			}
		}

		/**
		 * 根据键值获取参数
		 * 
		 * @param key
		 * @return
		 */
		public Object get(String key) {
			return this.data.get(key);
		}

		/**
		 * 获取 metaData 数据Map
		 * 
		 * @return
		 */
		public Map getDataMap() {
			this.data.remove("loadMetaData");
			this.data.remove("paramNames");

			return this.data;
		}
	}

	//数据
	
	private Map data;

	private StoreMetaData metaData;

	/**
	 * 构造方法
	 * 
	 * @param data
	 *            数据
	 */
	public StoreData(Map data) {
		this.data = data == null ? new HashMap() : data;
		Map metaData = (Map) this.data.get("metaData");
		if (metaData != null) {
			this.setMetaData(new StoreMetaData(metaData));
		}
	}

	/**
	 * 获取metaData
	 * 
	 * @return {@link StoreMetaData}
	 */
	public StoreMetaData getMetaData() {
		return this.metaData;
	}

	/**
	 * 获取数据List <br/>
	 * read 操作 StoreData 中不包含数据，则返回null <br/>
	 * create update destroy 操作 StoreData 中包含要操作的数据，通过该方法获取这些数据 <br/>
	 * 
	 * @return
	 */
	public List getData() {
		return (List) this.data.get(this.metaData.getRoot());
	}

	/**
	 * 根据键值获取data中数据。
	 * 
	 * @param key
	 * @return {@link Object} object
	 */
	public Object get(String key) {
		return this.data.get(key);
	}

	/**
	 * 获取排序字段名称<br/>
	 * 如果没有排序字段则返回null。<br/>
	 * 
	 * @return {@link String} sortField
	 */
	public String getSortField() {
		return this.metaData.getSortField();
	}

	/**
	 * 获取排序方式,返回值为 ASC 或 DESC 。
	 * 
	 * @return {@link String} dir
	 */
	public String getSortDir() {
		return this.metaData.getSortDir();
	}

	/**
	 * 
	 * @return
	 */
	public String getGroupBy() {
		return this.metaData.getGroupBy();
	}

	/**
	 * 
	 * @return
	 */
	public String getGroupDir() {
		return this.metaData.getGroupDir();
	}

	/**
	 * 获取排序信息
	 * 
	 * @return {@link String} sortInfo
	 */
	public String getSortInfo() {
		String sortInfo = null;
		String groupBy = this.getGroupBy();
		String field = this.getSortField();
		if (groupBy != null && !groupBy.equals(field)) {
			String groupDir = this.getGroupDir();
			sortInfo = groupBy + " " + groupDir;
		}
		if (field != null) {
			return (sortInfo != null ? sortInfo + ", " : "") + field + " "
					+ this.getSortDir();
		} else {
			return null;
		}
	}

	/**
	 * 获取要查询数据开始位置
	 * 
	 * @return
	 */
	public Integer getStart() {
		return this.metaData.getStart();
	}

	/**
	 * 设置要查询数据开始位置
	 * 
	 */
	public void setStart(Object start) {
		this.metaData.setStart(start);
	}

	/**
	 * 获取要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 * @return
	 */
	public Integer getLimit() {
		return this.metaData.getLimit();
	}

	/**
	 * 设置要查询数据条数限制数，如果返回为null则表示不限制。
	 * 
	 */
	public void setLimit(Object limit) {
		this.metaData.setLimit(limit);
	}

	/**
	 * 设置metaData
	 * 
	 * @param metaData
	 */
	public void setMetaData(StoreMetaData metaData) {
		this.metaData = metaData;
	}

	/**
	 * 设置数据List
	 * 
	 * @param data
	 */
	public void setData(List data) {
		this.data.put(this.metaData.getRoot(), data);
	}

	/**
	 * 设置合计数据Map
	 * 
	 * @param summaryData
	 */
	public void setSummaryData(Map summaryData) {
		this.data.put(this.metaData.getSummaryDataProperty(), summaryData);
	}

	/**
	 * 获取合计数据Map
	 * 
	 * @param summaryData
	 */
	public Map getSummaryData() {
		return (Map) this.data.get(this.metaData.getSummaryDataProperty());
	}

	/**
	 * 设置分组合计数据
	 * 
	 * @param groupSummaryData
	 */
	public void setGroupSummaryData(Map groupSummaryData) {
		this.data.put(this.metaData.getGroupSummaryDataProperty(),
				groupSummaryData);
	}

	/**
	 * 设置数据总数
	 * 
	 * @param total
	 */
	public void setTotal(int total) {
		this.data.put(this.metaData.getTotalProperty(), new Integer(total));
	}

	/**
	 * 设置成功标记
	 */
	public void setSuccess() {
		this.data
				.put(this.metaData.getSuccessProperty(), Boolean.valueOf(true));
	}

	/**
	 * 设置失败标记
	 */
	public void setFailure() {
		this.data.put(this.metaData.getSuccessProperty(),
				Boolean.valueOf(false));
	}

	/**
	 * 设置信息内容
	 */
	public void setMessage(String message) {
		this.data.put(this.metaData.getMessageProperty(), message);
	}

	/**
	 * 设置metaData fields
	 * 
	 * @param fields
	 */
	public void setMetaDataFields(List fields) {
		this.metaData.setFields(fields);
	}

	/**
	 * 设置查询头字段
	 * 
	 * @param fields
	 */
	public void setMetaDataQeuryFields(List fields) {
		this.metaData.setQueryField(fields);
	}

	/**
	 * 将字段 field 进行 ASC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldASC(String field) {
		this.metaData.setSortFieldASC(field);
	}

	/**
	 * 将字段 field 进行 DESC 排序
	 * 
	 * @param field
	 */
	public void setSortFieldDESC(String field) {
		this.metaData.setSortFieldDESC(field);
	}

	/**
	 * 返回数据Map,该Map中包含所有要返回的数据。<br/>
	 * 其中包括元数据metaData。
	 * 
	 * @return {@link Map} data
	 */
	public Map<String, Object> getDataMap() {
		Object data = this.data.get(this.metaData.getRoot());
		if (data == null) {
			this.setData(new ArrayList());
		}
		if (this.metaData.isLoad() != true) {
			this.data.remove("metaData");
		} else {
			this.data.put("metaData", this.metaData.getDataMap());
		}
		return this.data;
	}

}
