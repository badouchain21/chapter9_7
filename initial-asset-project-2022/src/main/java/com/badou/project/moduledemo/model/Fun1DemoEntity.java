package com.badou.project.moduledemo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppEntityAllField;
import com.badou.tools.vendors.annotation.DefaultOrderBy;
/**
 * 应用DEMO实体对象
 * <p>该实体对象一般来讲，映射数据库表
 * <br/>命名规则：应用功能名称+Entity
 * <br/>按业务实际情况继承<package>com.shengdai.base.support.hibernate</package>包下抽象实体类
 * <ul>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityAllField</li>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityOnlyId</li>
 * <li>com.shengdai.base.support.hibernate.used.AppBaseEntity</li>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityWithOperationInfo</li>
 * <li>com.shengdai.base.support.hibernate.BaseHibernateEntity<br>
 * </ul>
 * @author xiangsf 2013-1-18
 * 注解：http://www.cnblogs.com/hongten/archive/2011/07/20/2111773.html
 * http://docs.jboss.org/hibernate/annotations/3.4/reference/zh_cn/html_single/?qqdrsign=07b64
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN1")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
@DefaultOrderBy(clause="priority ,updateTime desc, createTime desc") //默认排序，该排序只针对单表有效
@Where(clause = "FLG_DELETED=0")
public class Fun1DemoEntity extends AppEntityAllField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1025769340713738752L;
	/**
	 * 字段1
	 */
	@Column(name = "field_name_1", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String fieldName1;
	/**
	 * 字段2
	 */
	@Column(name = "field_name_2", unique = false, nullable = true, insertable = true, updatable = true, length = 22, scale = 0)
	private Integer fieldName2 = 0;
	/**
	 * 字段3
	 */
	@Column(name = "field_name_3", unique = false, nullable = true, insertable = true, updatable = true, length = 22, scale = 2)
	private Double fieldName3 = 0.00D;
	/**
	 * 字段4
	 */
	@Column(name = "field_name_4", unique = false, nullable = true, insertable = true, updatable = true, length = 22, scale = 0)
	private Long fieldName4 = 0L;
	/**
	 * 字段5http://blog.csdn.net/notonlyforshe/article/details/7343971
	 */
	@Column(name = "field_name_5", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fieldName5 = new Date();
	/**
	 * 字段6
	 */
	@Column(name = "field_name_6", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	private String fieldName6;
	
	/** 
	 * 功能点集合
	 */
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "fun1Demo")
	@Where(clause = "FLG_DELETED=0")
	private Set<Fun1DemoChildEntity> fun1DemoChilds = new HashSet<Fun1DemoChildEntity>();

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

	public Set<Fun1DemoChildEntity> getFun1DemoChilds() {
		return fun1DemoChilds;
	}

	public void setFun1DemoChilds(Set<Fun1DemoChildEntity> fun1DemoChilds) {
		this.fun1DemoChilds = fun1DemoChilds;
	}
	
	
}
