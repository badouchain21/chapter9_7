package com.badou.bpms.beans;
/**
 * 流程定义的业务接口bean
 * <p>业务上所有需要流程相关的地方，都是获得该对象
 * <p>该bean定义将基础流程定义转换为业务需要使用的统一对象
 * @author xiangsf 2013-3-4
 *
 */
public class FlowBean {
	/**
	 * 流程ID
	 */
	private String id;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 流程名称
	 */
	private String name;
	/**
	 * 业务类型
	 */
	private String busiType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusiType() {
		return busiType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	
	
}
