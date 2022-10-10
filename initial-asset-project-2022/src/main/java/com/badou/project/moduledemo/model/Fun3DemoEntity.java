package com.badou.project.moduledemo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppTreeEntity;
import com.badou.project.moduledemo.ModuleConstants;
import com.badou.tools.vendors.annotation.DefaultOrderBy;

/**
 * 菜单定义
 * @author badousoft.com service@badousoft.com
 * @date 2012-11-4 下午08:59:38
 */
@javax.persistence.Entity
@Table(name = "SD_DEMO_FUN3")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@DefaultOrderBy(clause="priority ,updateTime desc, createTime desc")
public class Fun3DemoEntity extends AppTreeEntity<Fun3DemoEntity> {
	/** 
	 * @Description TODO
	 * @Fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = -3803373701007780452L;
	/** 
	 * 所属系统ID
	 */
	@Column(name = "SYS_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String sysId;
	/**
	 * 绑定访问系统ID
	 */
	@Column(name = "BELONG_SYS_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String belongSysId;
	/**
	 * URL路径
	 */
	@Column(name = "URL", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	private String url;
	/** 
	 * 小图标路径
	 */
	@Column(name = "SMALL_ICON", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String smallIcon;
	/** 
	 * 大图标路径
	 */
	@Column(name = "BIG_ICON", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	private String bigIcon;
	/** 
	 * 目标窗口
	 * <p>值参考{@link #MenuConstants}中定义,默认值MenuConstants.MENU_TARGETWIN_NEW新窗口
	 */
	@Column(name = "TARGET_WIN", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	private String targetWin = ModuleConstants.MENU_TARGETWIN_NEW;
	/** 
	 * 备注
	 */
	@Column(name = "REMARK", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	private String remark;
	
	/** 
	 * 功能点集合
	 */

	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "menu")
	@Where(clause = "FLG_DELETED=0")
	private Set<Fun3DemoChildEntity> functions = new HashSet<Fun3DemoChildEntity>();

	public String getBelongSysId() {
		return belongSysId;
	}

	public void setBelongSysId(String belongSysId) {
		this.belongSysId = belongSysId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public String getBigIcon() {
		return bigIcon;
	}

	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}

	public String getTargetWin() {
		return targetWin;
	}

	public void setTargetWin(String targetWin) {
		this.targetWin = targetWin;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<Fun3DemoChildEntity> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<Fun3DemoChildEntity> functions) {
		this.functions = functions;
	}

}
