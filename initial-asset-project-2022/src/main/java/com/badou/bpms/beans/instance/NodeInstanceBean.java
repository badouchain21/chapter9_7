package com.badou.bpms.beans.instance;
/**
 * 环节实例
 * @author xiangsf 2013-3-29
 *
 */
public class NodeInstanceBean {

	private String id;
	
	private String nodeId;
	
	private String nodeName;
	
	private int handleWay;
	
	private int priority;

	private boolean flgFullScreen = false;
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
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the handleWay
	 */
	public int getHandleWay() {
		return handleWay;
	}

	/**
	 * @param handleWay the handleWay to set
	 */
	public void setHandleWay(int handleWay) {
		this.handleWay = handleWay;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the flgFullScreen
	 */
	public boolean isFlgFullScreen() {
		return flgFullScreen;
	}

	/**
	 * @param flgFullScreen the flgFullScreen to set
	 */
	public void setFlgFullScreen(boolean flgFullScreen) {
		this.flgFullScreen = flgFullScreen;
	}
	
	
}
