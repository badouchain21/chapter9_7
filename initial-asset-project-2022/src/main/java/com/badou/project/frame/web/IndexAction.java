package com.badou.project.frame.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.badou.brms.base.support.BaseAction;
import com.badou.brms.beans.ResourceBean;
import com.badou.brms.iface.IBrmsOrgService;
import com.badou.brms.iface.IBrmsResourceService;
import com.badou.brms.system.security.service.IResourceService;
import com.badou.brms.system.security.service.IRoleService;
import com.badou.brms.system.security.web.MenuTreeForm;
import com.badou.designer.panel.layout.service.ILayoutService;
import com.badou.designer.panel.userlayout.service.IUserLayoutService;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:22:27
 * @todo 登录首页相关接口
 */
public class IndexAction extends BaseAction {

	public static final String FORWORD_TAB_ID_PARAMNAME = "menuId";
	@Autowired
	private IResourceService resourceService;
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	protected IBrmsOrgService brmsOrgService;

	@Autowired
	private IBrmsResourceService brmsResourceService;
	
	@Autowired
	protected IUserLayoutService userLayoutService;
	
	@Autowired
	protected ILayoutService layoutService;
	
	private String menuId; 
	private List<MenuTreeForm> menus;
	public String getMenuId() {
		return menuId;
	}

	public List<MenuTreeForm> getMenus() {
		return menus;
	}
	
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	private List<ResourceBean> menusBean;
	
	public List<ResourceBean> getMenusBean() {
		return menusBean;
	}

	public void setMenusBean(List<ResourceBean> menusBean) {
		this.menusBean = menusBean;
	}
	
	 
	/*@Action(interceptorRefs = @InterceptorRef("baseStack"))
	@DispatcherResult(name="content",location="/content.jsp")
	public String content(){ 
		//获得菜单
		ServletActionContext.getRequest().setAttribute("currentUserName", LogonCertificateHolder.getLogonCertificate().getUserName());
		
		Role role = roleService.findById(LogonCertificateHolder.getLogonCertificate().getRoleId());
		ServletActionContext.getRequest().setAttribute("indexUrl", role.getIndexUrl());
	//	role != null && StringUtils.isNotBlank(role.getIndexUrl()) ? role.getIndexUrl() : "/main.jsp");
		
		//获得菜单
		menusBean = brmsResourceService.getResourceByCurrentUser();
	  	request.setAttribute("menusCount", menusBean.size());
	  	
	  	//添加按钮权限
	    Set<Resource> set =role.getResourceSet();
	    JSONObject jo = new JSONObject();
	    jo.put("userId", LogonCertificateHolder.getLogonCertificate().getUserId());
	    String str = "";
	    for(Resource r : set){
	    	if(r.getResourceType() != null && r.getResourceType() == PermissionConst.RESOURCE_TYPE_BTN){
	    		str += r.getCode().trim() + ",";
	    	}
	    }
	    jo.put("str", str);
	    ServletActionContext.getRequest().getSession().setAttribute(PermissionConst.USER_PER, jo);
	    
	  	*//**获取系统名称**//*
		ServletActionContext.getRequest().setAttribute("SYS_LOGO_NAME", SystemSetting.getLocalBaseDefine().getSystemtitle());
		//ServletActionContext.getRequest().setAttribute("logoUrl", StringUtils.isNotBlank(DictionaryLib.getDictionaryValue(DictionaryConsts.SYS_USE_COMPANY_LOGO_URL)) ? DictionaryLib.getDictionaryValue(DictionaryConsts.SYS_USE_COMPANY_LOGO_URL) : null);
		*//**获取系统logo**//*
		ServletActionContext.getRequest().setAttribute("SYS_LOGO_IMG", SystemSetting.getLocalBaseDefine().getHomelogo());
		*//**获取系统顶部背景图片**//*
		ServletActionContext.getRequest().setAttribute("SYS_BANNER_IMG", SystemSetting.getLocalBaseDefine().getBannerbackground());
		*//**获取系统顶部背景颜色**//*
		ServletActionContext.getRequest().setAttribute("SYS_BANNER_COLOR", SystemSetting.getLocalBaseDefine().getBannerbackgroundcolor());
		*//**获取系统菜单表背景颜色**//*
		ServletActionContext.getRequest().setAttribute("SYS_MENU_COLOR", SystemSetting.getLocalBaseDefine().getMenucolor());
		// 获取1级菜单的显示个数，默认显示5个
		Optional<Integer> level1MenuNum = Optional.ofNullable(SystemSetting.getLocalBaseDefine().getLevel1MenuNum());
		ServletActionContext.getRequest().setAttribute("LEVEL_1_MENU_NUM", level1MenuNum.orElse(5));
		*//** 获取用户头像、logo**//*
		User user = InstanceFactory.get(User.class, LogonCertificateHolder.getLogonCertificate().getUserId());
		
		 *判断用户是否已经存在自定义首页 
		 
		try {
			List<UserLayoutEntity> userLayoutList = userLayoutService.findByUserId(user.getId());
			if(userLayoutList!=null && userLayoutList.size()>0&&layoutService.get(userLayoutList.get(0).getLayoutId()).getBlock().size()>0){
				ServletActionContext.getRequest().setAttribute("userOwnLayoutId",  userLayoutList.get(0).getLayoutId());
			}else{
				ServletActionContext.getRequest().setAttribute("userOwnLayoutId",  null);
			}
		} catch (Exception e) {
			ServletActionContext.getRequest().setAttribute("userOwnLayoutId",  null);
		}
		//用于判断是否首页工作台，如果是首页工作台就隐藏块的设置相关功能
		ServletActionContext.getRequest().setAttribute("isWorkBeanch",  true);
		UserCommInfo uc = user.getUserCommInfo();
		ServletActionContext.getRequest().setAttribute("userIconUrl", uc.getIcon());

		request.setAttribute("GRZX", resourceService.findResourceByCode(GlobalConsts.GRZX_MENU_CODE));
		return "content";
	}*/

 
	/*@Action(interceptorRefs = @InterceptorRef("baseStack"))
	@RedirectFullResult
	public String forward(){
		Resource menu = resourceService.findById(menuId);
		if(menu != null){
			//组装
			String url = menu.getUrl();
			if(StringUtils.isNotEmpty(url)){
				return UrlUtils.combUrl(url.trim(), FORWORD_TAB_ID_PARAMNAME, menu.getId());
			}
		}
		//没有地址，转到一个中间页面
		return EERROR;
	}*/
}
