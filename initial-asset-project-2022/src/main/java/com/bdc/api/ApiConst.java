package com.bdc.api;

/**
 * 接口常量类
 * @author linxiaoqing
 * @date 2019年10月16日
 */
public class ApiConst {
	/**
	 * 接口所属系统
	 */
	public static String SUBORDINATE_SYSTEM = "SUBORDINATE_SYSTEM";

	/**
	 * 接口使用状态
	 * 0:启用
	 * 1：停用
	 */
	public static String INTERFACE_STATUS = "INTERFACE_STATUS";

	/**
	 * 接口交互类型
	 * 0:同步
	 * 1：异步
	 */
	public static String INTERFACE_RESPON_TYPE = "INTERFACE_RESPON_TYPE";

	/**
	 * 能否访问系统
	 * 0:是
	 * 1:否
	 */
	public static String IS_CALL_INTERFACE="IS_CALL_INTERFACE";

	/**
	 * 权限表的接口访问权限
	 * 0:开放
	 * 1:关闭
	 */
	public static String INTERFACE_PERMISSION= "INTERFACE_PERMISSION";
}
