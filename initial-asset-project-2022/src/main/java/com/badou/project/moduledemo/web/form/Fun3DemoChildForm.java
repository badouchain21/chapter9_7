package com.badou.project.moduledemo.web.form;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.brms.util.InstanceFactory;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
/**
 * 页面表单form
 * <p>视图模型对象，一般地由实体对象转换而来 
 * <p>数据视图展示时包装，比如时间格式化，字段状态使用字典库名称等
 * @author xiangsf 2013-1-18
 *
 */
public class Fun3DemoChildForm extends BaseStrutsEntityForm<Fun3DemoChildEntity> {
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
	 * 页面类型
	 * <p>值参考{@link #MenuConstants}中定义
	 * <P>目前定义四种form,list,view,ref_link，可在页面上自定义输入
	 */
	private String pageType;
	/**
	 * js方法
	 */
	private String jsFunction;
	/**
	 * 图标位置
	 */
	private String iconAddr;
	/** 
	 * 目标窗口
	 * <p>值参考{@link #MenuConstants}中定义,默认值MenuConstants.MENU_TARGETWIN_NEW新窗口
	 */
	private String targetWin;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 主表ID
	 */

	private String menuId;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:53:10
	 * @todo 无参构造
	 */
	public Fun3DemoChildForm(){}
	
	@Override
	public void setFormToEntityProperties(Fun3DemoChildEntity instance) {
		super.setFormToEntityProperties(instance);
		//设置主表对象
		Fun3DemoEntity entity = InstanceFactory.get(Fun3DemoEntity.class, menuId);
		instance.setMenu(entity);
		entity.getFunctions().add(instance);
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

	@JSONField(format="yyyy-MM-dd hh:mm:ss")
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

	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getPageType() {
		return pageType;
	}


	public void setPageType(String pageType) {
		this.pageType = pageType;
	}


	public String getJsFunction() {
		return jsFunction;
	}


	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}


	public String getIconAddr() {
		return iconAddr;
	}


	public void setIconAddr(String iconAddr) {
		this.iconAddr = iconAddr;
	}


	public String getTargetWin() {
		return targetWin;
	}


	public void setTargetWin(String targetWin) {
		this.targetWin = targetWin;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
