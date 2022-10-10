package com.bdc.api.intermanage.tokenmanage.web.form;

import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.bdc.api.intermanage.tokenmanage.model.TokenManageEntity;

public class TokenManageForm extends BaseStrutsEntityForm<TokenManageEntity>{

	/**
	 *
	 */
	private static final long serialVersionUID = 7791414010407534067L;
	/**
	 * appId
	 */
	private String appId;
	/**
	 * appKey
	 */
	private String appkey;
	/**
	 * token
	 */
	private String token;
	/**
	 * 起始时间
	 */
	protected Date createTime;
	/**
	 * 结束时间
	 */
	protected Date endTime;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
