package com.badou.platform.vo;

/**
 * 模型版本vo
 * @ClassName ModuleVersionVo
 * @Description TODO(这里用一句话描述这个类的作用)
 * @version 1.0.0
 */
public class ResourceVersionVO {
	/**
	 * 云中心资源id
	 */
	//private String platformResourceId;
	private String versionCode;
	private String version1;
	private String version2;
	private String version3;
	private Integer type;
	/**
	 * 云中心的版本id
	 */
	private String resourceVersionId;
	/**
	 * 云中心的资源id
	 */
	private String resourceId;
	/**
	 * 资源编码
	 */
	private String resourceCode;
	/**
	 * 资源名称
	 */
	private String resourceName;

	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:13:51
	 * @todo 无参构造函数
	 */
	public ResourceVersionVO() {}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:14:04
	 * @todo 有参构造函数(根据类型构造)
	 * @param versionCode
	 * @param version1
	 * @param version2
	 * @param version3
	 * @param type
	 */
	public ResourceVersionVO(String versionCode, String version1, String version2, String version3, int type) {
		this.versionCode = versionCode;
		this.version1 = version1;
		this.version2 = version2;
		this.version3 = version3;
		this.type = type;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:14:28
	 * @todo 有参构造函数(根据资源id构造)
	 * @param versionCode
	 * @param version1
	 * @param version2
	 * @param version3
	 * @param resourceId
	 */
	public ResourceVersionVO(String versionCode, String version1, String version2, String version3,String resourceId) {
		this.versionCode = versionCode;
		this.version1 = version1;
		this.version2 = version2;
		this.version3 = version3;
	}
	 
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersion1() {
		return version1;
	}
	public void setVersion1(String version1) {
		this.version1 = version1;
	}
	public String getVersion2() {
		return version2;
	}
	public void setVersion2(String version2) {
		this.version2 = version2;
	}
	public String getVersion3() {
		return version3;
	}
	public void setVersion3(String version3) {
		this.version3 = version3;
	}
	public String getResourceVersionId() {
		return resourceVersionId;
	}
	public void setResourceVersionId(String resourceVersionId) {
		this.resourceVersionId = resourceVersionId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
