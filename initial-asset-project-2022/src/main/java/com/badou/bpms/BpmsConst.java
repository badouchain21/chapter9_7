package com.badou.bpms;
/**
 * 流程在业务上需要用到的常量
 * @author xiangsf 2013-3-4
 *
 */
public final class BpmsConst {
	/**
	 * 业务类型常量，订单处理 常量
	 */
	public static final String DDCL = "DDCL";
	/**
	 * 订单处理，销售员修订，环节标识
	 */
	public static final String DDCL_HANDLE_RESIVE = "revise";
	
	/**
	 * 订单流程，直接发货流程标识
	 */
	public static final String DDCL_FLOW_DIRECT = "direct";
	
	/**
	 * 订单流程，预付订金流程标识
	 */
	public static final String DDCL_FLOW_DEPOSIT = "deposit";
	
	
	public static final int BO_STATUS_PREPARE = 0;
	
	/**
	 * 业务对象正常流转状态
	 */
	public static final int BO_STATUS_NORMAL = 1;
	
	/**
	 * 业务对象已归档
	 */
	public static final int BO_STATUS_ARCHIVE = 2;
	
	/**
	 * 业务对象退回状态
	 */
	public static final int BO_STATUS_BACK = 3;
	
	/**
	 * 业务对象挂起状态
	 */
	public static final int BO_STATUS_PAUSE = 4;
}
