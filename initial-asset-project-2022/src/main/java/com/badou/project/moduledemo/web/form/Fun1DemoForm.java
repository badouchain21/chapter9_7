package com.badou.project.moduledemo.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.model.Fun1DemoEntity;
import com.badou.tools.common.util.bean.BeanUtils;

/**
 * 页面表单form
 * <p>
 * 视图模型对象，一般地由实体对象转换而来
 * <p>
 * 数据视图展示时包装，比如时间格式化，字段状态使用字典库名称等
 * 
 * @author xiangsf 2013-1-18
 * 
 */
public class Fun1DemoForm extends BaseStrutsEntityForm<Fun1DemoEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227752468574948125L;
	/**
	 * 优先级
	 */
	protected Double priority = 0d;
	/**
	 * 编码
	 */
	protected String code;
	/**
	 * 名称
	 */
	protected String name;
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
	 * 更新者ID
	 */
	protected String updator;
	/**
	 * 更新者名称
	 */
	protected String updatorName;
	/**
	 * 更新时间
	 */
	protected Date updateTime = new Date();
	/**
	 * 字段1
	 */
	private String fieldName1;
	/**
	 * 字段2
	 */
	private Integer fieldName2 = 0;
	/**
	 * 字段3
	 */
	private Double fieldName3 = 0.00D;
	/**
	 * 字段4
	 */
	private Long fieldName4 = 0L;
	/**
	 * 字段5
	 */
	private Date fieldName5 = new Date();
	/**
	 * 字段6
	 */
	private String fieldName6;
	/**
	 * 子对象
	 */
	private List<Fun1DemoChildForm> children = new ArrayList<Fun1DemoChildForm>();

	@Override
	public void setEntityToFormProperties(Fun1DemoEntity instance) {
		super.setEntityToFormProperties(instance);
		if (!instance.getFun1DemoChilds().isEmpty()) {
			Fun1DemoChildForm cf = null;
			for (Fun1DemoChildEntity c : instance.getFun1DemoChilds()) {
				cf = new Fun1DemoChildForm();
				BeanUtils.copyProperties(cf, c);
				cf.setId(c.getId());
				children.add(cf);
			}
		}
	}

	@Override
	public void setFormToEntityProperties(Fun1DemoEntity instance) {
		instance.setCode(code);
		instance.setName(name);
		instance.setFieldName1(this.getFieldName1());
		instance.setFieldName2(this.getFieldName2());
		instance.setFieldName3(this.getFieldName3());
		instance.setFieldName4(this.getFieldName4());
		instance.setFieldName5(this.getFieldName5());
		instance.setPriority(this.getPriority());
	}

	public List<Fun1DemoChildForm> getChildren() {
		return children;
	}

	public void setChildren(List<Fun1DemoChildForm> children) {
		this.children = children;
	}

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFieldName1() {
		return fieldName1;
	}

	public void setFieldName1(String fieldName1) {
		this.fieldName1 = fieldName1;
	}

	public Integer getFieldName2() {
		return fieldName2;
	}

	public void setFieldName2(Integer fieldName2) {
		this.fieldName2 = fieldName2;
	}

	public Double getFieldName3() {
		return fieldName3;
	}

	public void setFieldName3(Double fieldName3) {
		this.fieldName3 = fieldName3;
	}

	public Long getFieldName4() {
		return fieldName4;
	}

	public void setFieldName4(Long fieldName4) {
		this.fieldName4 = fieldName4;
	}

	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	public Date getFieldName5() {
		return fieldName5;
	}

	public void setFieldName5(Date fieldName5) {
		this.fieldName5 = fieldName5;
	}

	public String getFieldName6() {
		return fieldName6;
	}

	public void setFieldName6(String fieldName6) {
		this.fieldName6 = fieldName6;
	}

	@Override
	public String toString() {
		return "Fun1DemoForm [id=" + id + ", priority=" + priority + ", code="
				+ code + ", name=" + name + ", creator=" + creator
				+ ", creatorName=" + creatorName + ", createTime=" + createTime
				+ ", updator=" + updator + ", updatorName=" + updatorName
				+ ", updateTime=" + updateTime + ", fieldName1=" + fieldName1
				+ ", fieldName2=" + fieldName2 + ", fieldName3=" + fieldName3
				+ ", fieldName4=" + fieldName4 + ", fieldName5=" + fieldName5
				+ ", fieldName6=" + fieldName6 + "]";
	}

}
