package com.badou.project.moduledemo.model;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppEntityWithFlgDeleted;
import com.badou.tools.vendors.annotation.DefaultOrderBy;
/**
 * 应用DEMO实体对象,示例2
 * <p>该实体对象一般来讲，映射数据库表
 * <br/>命名规则：应用功能名称+Entity
 * <br/>按业务实际情况继承<package>com.shengdai.base.support.hibernate</package>包下抽象实体类
 * <ul>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityAllField</li>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityOnlyId</li>
 * <li>com.shengdai.base.support.hibernate.used.AppBaseEntity</li>
 * <li>com.shengdai.base.support.hibernate.used.AppEntityWithCreator</li>
 * <li>com.shengdai.base.support.hibernate.BaseHibernateEntity<br>
 * </ul>
 * @author xiangsf 2013-1-18
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN2")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
@DefaultOrderBy(clause="createTime desc") //默认排序，该排序只针对单表有效
@Where(clause = "FLG_DELETED=0")
public class Fun2DemoEntity extends AppEntityWithFlgDeleted {

	/**
	 * 
	 */
	private static final long serialVersionUID = 444904135356506239L;

	/**
	 * 字段1
	 */
	@Column(name = "field_name_1", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String fieldName1;
	/**
	 * 字段2
	 */
	@Column(name = "field_name_2", unique = false, nullable = true, insertable = true, updatable = true, length = 22, scale = 0)
	private String fieldName2;
	
	/**
	 * 字段3
	 */
	@Column(name = "field_name_3", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String fieldName3;
	
	/**
	 * 字段4
	 */
	@Column(name = "field_name_4", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	private String fieldName4;
	
	/**
	 * 字段5
	 */
	@Column(name = "field_name_5", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String fieldName5;
	
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
	public String toString() {
		return "Fun2DemoEntity [fieldName1=" + fieldName1 + ", fieldName2="
				+ fieldName2 + ", fieldName3=" + fieldName3 + ", fieldName4="
				+ fieldName4 + ", fieldName5=" + fieldName5 + "]";
	}
	
}
