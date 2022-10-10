package com.badou.project.moduledemo.web.form;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.project.moduledemo.model.Fun2DemoEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:18:30
 * @todo demo2form表单
 */
public class Fun2DemoForm extends BaseStrutsEntityForm<Fun2DemoEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227752468574948125L;

	/**
	 * 创建者ID
	 */
	protected String creator;
	/**
	 * 创建者名称
	 */
	protected String creatorName;
	/**
	 * 创建时间
	 */
	protected Date createTime = new Date();
	
	/**
	 * 逻辑删除标识
	 */
	protected Integer flgDeleted = SystemBoolean.NO.getKey();
	/**
	 * 字段1
	 */
	private String fieldName1;
	/**
	 * 字段2
	 */
	private String fieldName2;
	
	/**
	 * 字段3
	 */
	private String fieldName3;
	
	/**
	 * 字段4
	 */
	private String fieldName4;
	
	/**
	 * 字段5
	 */
	private String fieldName5;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getFlgDeleted() {
		return flgDeleted;
	}

	public void setFlgDeleted(Integer flgDeleted) {
		this.flgDeleted = flgDeleted;
	}

	public String getFieldName1() {
		return fieldName1;
	}

	public void setFieldName1(String fieldName1) {
		this.fieldName1 = fieldName1;
	}

	public String getFieldName2() {
		return fieldName2;
	}

	public void setFieldName2(String fieldName2) {
		this.fieldName2 = fieldName2;
	}

	public String getFieldName3() {
		return fieldName3;
	}

	public void setFieldName3(String fieldName3) {
		this.fieldName3 = fieldName3;
	}

	public String getFieldName4() {
		return fieldName4;
	}

	public void setFieldName4(String fieldName4) {
		this.fieldName4 = fieldName4;
	}

	public String getFieldName5() {
		return fieldName5;
	}

	public void setFieldName5(String fieldName5) {
		this.fieldName5 = fieldName5;
	}

	@Override
	public void setFormToEntityProperties(Fun2DemoEntity instance) {
		//可重写该方法，以实现保存内容的正确性
		//比如字典库转换，数组拆分存储等。
		//super.setEntityProperties(instance);
		instance.setFieldName1(this.getFieldName1());
		instance.setFieldName2(this.getFieldName2());
		instance.setFieldName3(this.getFieldName3());
		instance.setFieldName4(this.getFieldName4());
		instance.setFieldName5(this.getFieldName5());
	}

	@Override
	public String toString() {
		return "Fun2DemoForm [id=" + id + ", creator=" + creator
				+ ", creatorName=" + creatorName + ", createTime=" + createTime
				+ ", flgDeleted=" + flgDeleted + ", fieldName1=" + fieldName1
				+ ", fieldName2=" + fieldName2 + ", fieldName3=" + fieldName3
				+ ", fieldName4=" + fieldName4 + ", fieldName5=" + fieldName5
				+ "]";
	}

}
