package com.badou.project.moduledemo.web.form;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.brms.util.InstanceFactory;
import com.badou.project.moduledemo.model.Fun1DemoChildEntity;
import com.badou.project.moduledemo.model.Fun1DemoEntity;
/**
 * 页面表单form
 * <p>视图模型对象，一般地由实体对象转换而来 
 * <p>数据视图展示时包装，比如时间格式化，字段状态使用字典库名称等
 * @author xiangsf 2013-1-18
 *
 */
public class Fun1DemoChildForm extends BaseStrutsEntityForm<Fun1DemoChildEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227752468574948125L;
	/**
	 * 创建者ID
	 */
	private String creator;
	/**
	 * 创建者名称
	 */
	private String creatorName;
	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * 优先级
	 */
	private Double priority = 0d;
	/**
	 * 字段1
	 */
	private String fieldName1;
	
	/**
	 * 字段1
	 */
	private String fieldName2;
	/**
	 * 主表ID
	 */
	private String fun1DemoId;
	
	
	@Override
	public void setFormToEntityProperties(Fun1DemoChildEntity instance) {
		super.setFormToEntityProperties(instance);
		//设置主表对象
		Fun1DemoEntity entity = InstanceFactory.get(Fun1DemoEntity.class, fun1DemoId);
		instance.setFun1Demo(entity);
		entity.getFun1DemoChilds().add(instance);
	}

	public String getFun1DemoId() {
		return fun1DemoId;
	}

	public void setFun1DemoId(String fun1DemoId) {
		this.fun1DemoId = fun1DemoId;
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
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}
	
	
}
