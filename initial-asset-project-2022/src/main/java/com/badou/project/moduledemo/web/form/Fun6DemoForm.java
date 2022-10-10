package com.badou.project.moduledemo.web.form;

import java.math.BigDecimal;
import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.project.moduledemo.model.Fun6DemoEntity;

/**
 * 是Fun6DemoEntity的VO类 form类一般是用来展示给用户看的
 *
 */
public class Fun6DemoForm extends BaseStrutsEntityForm<Fun6DemoEntity> {

	private static final long serialVersionUID = 5067405632405232178L;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 折扣
	 */
	private BigDecimal discount;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人名字
	 */
	private String creatorName;

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 把Entity类转换成Form类方法
	 */
	@Override
	public void setEntityToFormProperties(Fun6DemoEntity instance) {
		super.setEntityToFormProperties(instance);
	}

	/**
	 * Form转化成Entity类 属性相同时可以不用重写 如果Form中有entity没有的属性 或者需要做转换的字段 则需要重写
	 */
	@Override
	public void setFormToEntityProperties(Fun6DemoEntity instance) {
		super.setFormToEntityProperties(instance);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
