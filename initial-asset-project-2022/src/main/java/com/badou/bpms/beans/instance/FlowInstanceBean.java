package com.badou.bpms.beans.instance;
/**
 * 流程实例
 * @author xiangsf 2013-3-29
 *
 */
public class FlowInstanceBean {

	private String id;
	
	private String flowId;
	
	private String flowName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the flowId
	 */
	public String getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	/**
	 * @return the flowName
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * @param flowName the flowName to set
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	
	
}
