package com.badou.project.moduledemo.web.form;

import java.math.BigDecimal;
import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.project.moduledemo.model.Fun7DemoEntity;
 
/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:18:46
 * @todo demo7form表单
 */
public class Fun7DemoForm extends BaseStrutsEntityForm<Fun7DemoEntity> {

	// 不用设置id属性 因为父类有了

	private static final long serialVersionUID = 9031001628411759084L;
	/**
	 * 名字
	 */
	private String name;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 创建人名字
	 */
	private String creatorName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人名字
	 */
	private String updatorName;
	/**
	 * 供应商编码
	 */
	private String providerCode;
	/**
	 * 供应商名字
	 */
	private String providerName;

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * 
	 * 描述：获得供应商名字 
	 * 0 腾讯 1阿里 2百度
	 * 
	 * @return 供应商名字
	 *
	 */
	public String getProviderName() {
		return DictionaryLib.getItemNameByItemCode("PROVIDER", providerCode);
	}

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

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
