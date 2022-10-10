package com.badou.bpms.beans.instance;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:38:22
 * @todo 路由实例
 */
public class RouteInstanceBean implements Comparable<RouteInstanceBean>{

	private String id;
	
	private String routeId;
	
	private String routeName;
	
	private String remark;
	
	private int priority = 0;
	
	private String flgAttribute;
	
	private int isSave;
	
	private int flowSubmitFormNoValid;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:37:36
	 * @todo 无参构造器
	 */
	public RouteInstanceBean(){}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:37:54
	 * @todo 有参构造器
	 */
	public RouteInstanceBean(String id, String routeId, String routeName,
			String remark, int priority, String flgAttribute, int isSave, int flowSubmitFormNoValid) {
		super();
		this.id = id;
		this.routeId = routeId;
		this.routeName = routeName;
		this.remark = remark;
		this.priority = priority;
		this.flgAttribute = flgAttribute;
		this.isSave = isSave;
		this.flowSubmitFormNoValid = flowSubmitFormNoValid;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:38:06
	 * @todo 路由实例对象
	 */
	public RouteInstanceBean(String routeId, String routeName, int priority, String flgAttribute) {
		super();
		this.routeId = routeId;
		this.routeName = routeName;
		this.priority = priority;
		this.flgAttribute = flgAttribute;
	}

	public String getFlgAttribute() {
		return flgAttribute;
	}

	public void setFlgAttribute(String flgAttribute) {
		this.flgAttribute = flgAttribute;
	}

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
	 * @return the routeId
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the routeName
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * @param routeName the routeName to set
	 */
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public int getIsSave() {
		return isSave;
	}

	public void setIsSave(int isSave) {
		this.isSave = isSave;
	}

	public int getFlowSubmitFormNoValid() {
		return flowSubmitFormNoValid;
	}

	public void setFlowSubmitFormNoValid(int flowSubmitFormNoValid) {
		this.flowSubmitFormNoValid = flowSubmitFormNoValid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + priority;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((routeId == null) ? 0 : routeId.hashCode());
		result = prime * result
				+ ((routeName == null) ? 0 : routeName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		RouteInstanceBean other = (RouteInstanceBean) obj;
		if (id == null) {
			if (other.id != null){
				return false;
			}
		} else if (!id.equals(other.id)){
			return false;
		}
		if (priority != other.priority){
			return false;
		}
		if (remark == null) {
			if (other.remark != null){
				return false;
			}
		} else if (!remark.equals(other.remark)) {
			return false;
		}
		if (routeId == null) {
			if (other.routeId != null){
				return false;
			}
		} else if (!routeId.equals(other.routeId)){
			return false;
		}
		if (routeName == null) {
			if (other.routeName != null){
				return false;
			}
		} else if (!routeName.equals(other.routeName)){
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RouteInstanceBean [id=");
		builder.append(id);
		builder.append(", routeId=");
		builder.append(routeId);
		builder.append(", routeName=");
		builder.append(routeName);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", priority=");
		builder.append(priority);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(RouteInstanceBean o) {
		return o.getPriority() - this.priority;
	}
}
