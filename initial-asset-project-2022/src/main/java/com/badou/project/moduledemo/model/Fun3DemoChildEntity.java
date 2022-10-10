package com.badou.project.moduledemo.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppEntityAllField;
/**
 * 菜单下功能点定义
 * @author badousoft.com service@badousoft.com
 * @date 2012-11-8 上午09:45:52
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN3_CHILD")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Where(clause="flg_deleted = 0")
public class Fun3DemoChildEntity extends AppEntityAllField {

	/** 
	 * @Description TODO
	 * @Fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 3089756153320926903L;

	/**
	 * 所属菜单
	 */
	@JoinColumn(name = "MENU_ID", referencedColumnName = "ID")
	@ManyToOne
	private Fun3DemoEntity menu;
	
	
	@Column(name = "MENU_ID", unique = false, nullable = true, insertable = false, updatable = false)
	protected String menuId;
	
	/**
	 * 页面类型
	 * <p>值参考{@link #MenuConstants}中定义
	 * <P>目前定义四种form,list,view,ref_link，可在页面上自定义输入
	 */
	@Column(name = "PAGE_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	private String pageType;
	/**
	 * js方法
	 */
	@Column(name = "JS_FUNCTION", unique = false, nullable = true, insertable = true, updatable = true, length = 64)
	private String jsFunction;
	/**
	 * 图标位置
	 */
	@Column(name = "ICON_ADDR", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String iconAddr;
	/** 
	 * 目标窗口
	 * <p>值参考{@link #MenuConstants}中定义,默认值MenuConstants.MENU_TARGETWIN_NEW新窗口
	 */
	@Column(name = "TARGET_WIN", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	private String targetWin;
	/**
	 * 备注
	 */
	@Column(name = "REMARK", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	private String remark;
	public String getMenuId() {
		return menuId;
	}

	public void setMenu(Fun3DemoEntity menu) {
		this.menu = menu;
	}
	public String getPageType() {
		return pageType;
	}
	public Fun3DemoEntity getMenu() {
		return menu;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
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

}
