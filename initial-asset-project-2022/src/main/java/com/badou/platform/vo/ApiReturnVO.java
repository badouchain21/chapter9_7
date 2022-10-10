package com.badou.platform.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:10:46
 * @todo 统一接口返回封装vo
 */
public class ApiReturnVO {
	/**
	 * 成功
	 */
	public static final String SUCCESS = "SUCCESS";
	/**
	 * 失败
	 */
	public static final String FAIL = "FAIL";
	/**
	 * 必填项为空
	 */
	public static final String POST_DATA_EMPTY = "POST_DATA_EMPTY";
	/**
	 * 缺少参数
	 */
	public static final String LACK_PARAMS = "LACK_PARAMS";
	/**
	 * Token失效
	 */
	public static final String NOT_VALIDA_TOKEN = "ERROR";
	
	
	String returnCode;
	
	String returnMsg;
	
	Object data;

	
	@JSONField(name="return_code") 
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@JSONField(name="return_msg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@JSONField(name="data") 
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
