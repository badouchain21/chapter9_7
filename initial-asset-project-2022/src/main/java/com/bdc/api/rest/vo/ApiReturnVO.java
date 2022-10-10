package com.bdc.api.rest.vo;

import java.io.Serializable;


/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:40:28
 * @todo 请求返回vo类
 */
public class ApiReturnVO implements Serializable{
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


	private String returnCode;

	private String returnMsg;

	private Object data;


	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
