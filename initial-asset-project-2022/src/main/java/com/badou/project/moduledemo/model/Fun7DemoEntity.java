package com.badou.project.moduledemo.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:27:34
 * @todo demo7实体类
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN7_GOODS")
@Where(clause = "FLG_DELETED=0")
public class Fun7DemoEntity extends AppBaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5258820567477725785L;

	
	/**
	 * 商品名字
	 */
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String name;

	/**
	 * 价格
	 */
	@Column(name = "PRICE", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private BigDecimal price;

	/**
	 * 描述
	 */
	@Column(name = "DES", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String desc;
	/**
	 * 图片存放地址
	 */
	@Column(name = "IMG_URL", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String imgUrl;

	/**
	 * 对应数据字典名称PROVIDER 0腾讯 1阿里 2百度
	 */
	@Column(name = "PROVIDER_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String providerCode;
	
	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}



	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}


	

	@Override
	public String toString() {
		return "GoodsEntity [name=" + name + ", price=" + price + ", desc=" + desc + ", imgUrl=" + imgUrl
				+ ", providerCode=" + providerCode + ", providerValue=" +  "]";
	}

	





}
