package com.bdc.api.rest;
/**
 * 接口返回值 状态码(code message status)
 * <ul>
 * <li>NORMAL 正常 0</li>
 * <li>CALLS_FREQUENT 调用过于频繁 100</li>
 * <li>PARAMETER_MISSING 参数缺失 101</li>
 * <li>PARAMETER_EMPTY 参数值为空 102</li>
 * <li>PARAMETER_RESOLUTION_ERROR 参数值解析错误 103</li>
 * <li>OTHER_EXCEPTION 其他异常 104</li>
 * <li>OUTTER_TIME 超时 105</li>
 * <li>OUTTER_AUTHORITY 没有权限 106</li>
 * <li>OUTTER_START 接口未启动或者外网系统未启动 107</li>
 * <li>OUTTER_SYSNULL 系统不存在 108</li>
 * <li>OUTTER_INTERNULL 接口不存在 109</li>
 * <li>OUTTER_TOKENNULL token值失效或者为空 110</li>
 * <li>OUTTER_CASNULL CAS获取token值失败，发生异常 111</li>
 * <li>OUTTER_DUBBORERROR dubbo接口调用发生未知异常 112</li>
 * <li>OUTTER_IPADDRESSMISS 外部系统的ip地址不允许访问 113</li>
 * </ul>
 */

public enum StatusEnum {
	/**
	 * NORMAL
	 * 正常
	 * 0
	 */
	NORMAL("NORMAL","正常",0),

	/**
	 * CALLS_FREQUENT
	 * 调用过于频繁
	 * 100
	 */
	CALLS_FREQUENT("CALLS_FREQUENT","调用过于频繁",100),

	/**
	 * PARAMETER_MISSING
	 * 参数缺失
	 * 101
	 */
	PARAMETER_MISSING("PARAMETER_MISSING","参数缺失",101),

	/**
	 * PARAMETER_EMPTY
	 * 参数值为空
	 * 102
	 */
	PARAMETER_EMPTY("PARAMETER_EMPTY","参数值为空",102),

	/**
	 * PARAMETER_RESOLUTION_ERROR
	 * 参数值解析错误
	 * 103
	 */
	PARAMETER_RESOLUTION_ERROR("PARAMETER_RESOLUTION_ERROR","参数值解析错误",103),

	/**
	 * OTHER_EXCEPTION
	 * 其他异常
	 * 104
	 */
	OTHER_EXCEPTION("OTHER_EXCEPTION","其他异常",104),

	/**
	 * OUTTER_TIME
	 * 超时
	 * 105
	 */
	OUTTER_TIME("OUTTER_TIME","超时",105),

	/**
	 * OUTTER_AUTHORITY
	 * 没有权限
	 * 106
	 */
	OUTTER_AUTHORITY("OUTTER_AUTHORITY","没有权限",106),

	/**
	 * OUTTER_START
	 * 接口未启动或者外网系统未启动
	 * 107
	 */
	OUTTER_START("OUTTER_START","请求接口未启动或者外网系统未启动",107),

	/**
	 * OUTTER_NULL
	 * 系统不存在
	 * 108
	 */
	OUTTER_SYSNULL("OUTTER_SYSNULL","企业应用不存在",108),

	/**
	 * OUTTER_NULL
	 * 接口不存在
	 * 109
	 */
	OUTTER_INTERNULL("OUTTER_INTERNULL","请求接口不存在",109),

	/**
	 * token值失效或者为空
	 */
	OUTTER_TOKENNULL("OUTTER_TOKENNULL","token值失效或者为空",110),

	/**
	 * CAS获取token值失败，发生异常
	 */
	OUTTER_CASNULL("OUTTER_CASNULL","获取token值失败，系统发生异常",111),

	/**
	 * dubbo接口调用发生未知异常
	 */
	OUTTER_DUBBORERROR("OUTTER_DUBBORERROR","dubbo接口调用发生未知异常",112),

	/**
	 * 外部系统的ip地址不允许访问
	 */
	OUTTER_IPADDRESSMISS("OUTTER_IPADDRESSMISS","不在外网系统的ip地址允许访问范围内",113);
	private String code;
	private String message;
	private Integer status;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:41:24
	 * @todo 有参构造函数
	 * @param code
	 * @param message
	 * @param status
	 */
	private StatusEnum (String code, String message, Integer status){
		this.code = code;
		this.message = message;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Integer getStatus() {
		return status;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:41:45
	 * @todo 根据status值返回对应的枚举
	 * @param status
	 * @return 枚举描述
	 */
	public static StatusEnum getMessageByStatus(Integer status){

		StatusEnum statusEnum = null;

		switch(status){
		case 0:
			statusEnum = NORMAL;
			break;
		case 100:
			statusEnum = CALLS_FREQUENT;
			break;
		case 101:
			statusEnum = PARAMETER_MISSING;
			break;
		case 102:
			statusEnum = PARAMETER_EMPTY;
			break;
		case 103:
			statusEnum = PARAMETER_RESOLUTION_ERROR;
			break;
		case 104:
			statusEnum = OTHER_EXCEPTION;
			break;
		case 105:
			statusEnum = OUTTER_TIME;
			break;
		case 106:
			statusEnum = OUTTER_AUTHORITY;
			break;
		case 107:
			statusEnum = OUTTER_START;
			break;
		case 108:
			statusEnum = OUTTER_SYSNULL;
			break;
		case 109:
			statusEnum = OUTTER_INTERNULL;
			break;
		case 110:
			statusEnum = OUTTER_TOKENNULL;
			break;
		case 111:
			statusEnum = OUTTER_CASNULL;
			break;
		}
		return statusEnum;
	}

}
