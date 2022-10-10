package com.badou.project.moduledemo.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.tools.vendors.annotation.DefaultOrderBy;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:12:04
 * @todo demo6实体类
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN6_CUSTOMER")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
@DefaultOrderBy(clause="priority ,updateTime desc, createTime desc") //默认排序，该排序只针对单表有效
@Where(clause = "FLG_DELETED=0")
public class Fun6DemoEntity extends AppBaseEntity {
	/*
	 * 由于从父类继承了一些默认字段，所以子类不用写这些字段
	 */

	private static final long serialVersionUID = -7359514693076337079L;

	/**
	 * 客户名字
	 */
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String name;

	/**
	 * 手机号
	 */
	@Column(name = "PHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String phone;

	/**
	 * 性别
	 */
	@Column(name = "SEX", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private int sex;

	/**
	 * 折扣
	 */
	@Column(name = "DISCOUNT", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private BigDecimal discount;
	/**
	 * 备注
	 */
	@Column(name = "REMARK", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String remark;

	
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
