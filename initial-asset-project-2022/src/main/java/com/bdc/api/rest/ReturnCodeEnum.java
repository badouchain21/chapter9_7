package com.bdc.api.rest;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:36:08
 * @todo 请求返回状态枚举类
 */
public enum ReturnCodeEnum {
	SUCCESS("SUCCESS","请求成功"),
	FAIL("FAIL","请求失败"),
	POST_DATA_EMPTY("POST_DATA_EMPTY","必要字段为空"),
	LACK_PARAMS("LACK_PARAMS","缺少参数"),
	APPID_NOT_EXIST("APPID_NOT_EXIST","开发者账号不存在");

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:37:21
	 * @todo 有参构造函数
	 * @param returnCode
	 * @param returnMsg
	 */
	ReturnCodeEnum(String returnCode,String returnMsg){
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}


	private String returnCode;

	private String returnMsg;

	public String getReturnCode() {
		return returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}
}
