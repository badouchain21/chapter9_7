package com.badou.project.moduledemo.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.project.moduledemo.ModuleConstants;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.tools.common.util.bean.BeanUtils;

/**
 * 页面表单form
 * <p>
 * 视图模型对象，一般地由实体对象转换而来
 * <p>
 * 数据视图展示时包装，比如时间格式化，字段状态使用字典库名称等
 * 
 * @author xiangsf 2013-1-18
 * 
 */
public class Fun3DemoForm extends BaseStrutsEntityForm<Fun3DemoEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6227752468574948125L;

	/**
	 * 父节点对象
	 */
	protected Fun3DemoForm parent;
	
	protected String pid;
	
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * 子对象集合
	 * <p>
	 * 这里有附带一些关联策略，若不符合实际应用，可重写覆盖该属性及方法
	 */
	protected List<Fun3DemoForm> childrens = new ArrayList<Fun3DemoForm>();
	/**
	 * 层级
	 */
	protected Integer treeLevel;
	/**
	 * 节点类型
	 */
	protected String treeType;
	/**
	 * 全路径
	 */
	protected String fullPath;
	/**
	 * 全路径名称
	 */
	protected String fullName;
	/**
	 * 所属系统ID
	 */
	private String sysId;
	/**
	 * 绑定访问系统ID
	 */
	private String belongSysId;
	/**
	 * URL路径
	 */
	private String url;
	/**
	 * 小图标路径
	 */
	private String smallIcon;
	/**
	 * 大图标路径
	 */
	private String bigIcon;
	/**
	 * 目标窗口
	 * <p>
	 * 值参考{@link #MenuConstants}中定义,默认值MenuConstants.MENU_TARGETWIN_NEW新窗口
	 */
	private String targetWin = ModuleConstants.MENU_TARGETWIN_NEW;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 子对象
	 */
	private List<Fun3DemoChildForm> children = new ArrayList<Fun3DemoChildForm>();

	/**
	 * 优先级
	 */
	private Double priority = 0d;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
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
	 * 更新者ID
	 */
	private String updator;
	/**
	 * 更新者名称
	 */
	private String updatorName;
	/**
	 * 更新时间
	 */
	private Date updateTime = new Date();

	@Override
	public void setEntityToFormProperties(Fun3DemoEntity instance) {
		// 这里要自己写属性，因为使用apach的BeanUtil会进行子集懒加载，而这是我们不需要的
		this.id = instance.getId();
		this.code = instance.getCode();
		this.name = instance.getName();
		this.createTime = instance.getCreateTime();
		this.updateTime = instance.getUpdateTime();
		this.creator = instance.getCreator();
		this.creatorName = instance.getCreatorName();
		this.updator = instance.getUpdator();
		this.updatorName = instance.getUpdatorName();
		this.belongSysId = instance.getBelongSysId();
		this.bigIcon = instance.getBigIcon();
		this.fullName = instance.getFullName();
		this.fullPath = instance.getFullPath();
		this.priority = instance.getPriority();
		this.remark = instance.getRemark();
		this.smallIcon = instance.getSmallIcon();
		this.sysId = instance.getSysId();
		this.targetWin = instance.getTargetWin();
		this.treeLevel = instance.getTreeLevel();
		this.treeType = instance.getTreeType();
		this.url = instance.getUrl();
		if (!instance.getFunctions().isEmpty()) {
			Fun3DemoChildForm cf = null;
			for (Fun3DemoChildEntity c : instance.getFunctions()) {
				cf = new Fun3DemoChildForm();
				BeanUtils.copyProperties(cf, c);
				cf.setId(c.getId());
				children.add(cf);
			}
		}
	}

	@Override
	public void setFormToEntityProperties(Fun3DemoEntity instance) {
		super.setFormToEntityProperties(instance);

	}

	public List<Fun3DemoChildForm> getChildren() {
		return children;
	}

	public void setChildren(List<Fun3DemoChildForm> children) {
		this.children = children;
	}

	public Fun3DemoForm getParent() {
		return parent;
	}

	public void setParent(Fun3DemoForm parent) {
		this.parent = parent;
	}

	public List<Fun3DemoForm> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Fun3DemoForm> childrens) {
		this.childrens = childrens;
	}

	public Integer getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getBelongSysId() {
		return belongSysId;
	}

	public void setBelongSysId(String belongSysId) {
		this.belongSysId = belongSysId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
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

	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Fun3DemoForm [parent=" + parent + ", childrens=" + childrens
				+ ", treeLevel=" + treeLevel + ", treeType=" + treeType
				+ ", fullPath=" + fullPath + ", fullName=" + fullName
				+ ", sysId=" + sysId + ", belongSysId=" + belongSysId
				+ ", url=" + url + ", smallIcon=" + smallIcon + ", bigIcon="
				+ bigIcon + ", targetWin=" + targetWin + ", remark=" + remark
				+ ", children=" + children + ", priority=" + priority
				+ ", code=" + code + ", name=" + name + ", creator=" + creator
				+ ", creatorName=" + creatorName + ", createTime=" + createTime
				+ ", updator=" + updator + ", updatorName=" + updatorName
				+ ", updateTime=" + updateTime + ", id=" + id + "]";
	}

}
