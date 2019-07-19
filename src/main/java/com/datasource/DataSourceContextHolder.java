package com.datasource;

/**
 * @title DataSourceContextHolder.java
 * @description 持有数据库标识的线程类
 * @time 2019/07/17 16:29:14
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

	/**
	 * @Description: 设置数据源类型
	 */
	public static void setDataSourceType(String dataSourceType) {
		CONTEXT_HOLDER.set(dataSourceType);
	}

	/**
	 * @Description: 获取数据源类型
	 */
	public static String getDataSourceType() {
		return CONTEXT_HOLDER.get();
	}

	/**
	 * @Description: 清除数据源类型
	 */
	public static void clearDataSourceType() {
		CONTEXT_HOLDER.remove();
	}
}
