package com.badou.project.common.tags.frame;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
/**
 * 工具条自定义标签
 * <p>读取指定菜单下的功能按钮，在视图上展示出来，并且通过传入的页面类型过滤这些功能按钮
 * @author badousoft.com service@badousoft.com
 * @date 2012-11-19 下午04:56:25
 */
public class ToolbarTag extends BodyTagSupport {
	
	protected static Logger logger = Logger.getLogger(ToolbarTag.class);
	/** 
	 * @Description TODO
	 * @Fields serialVersionUID 
	 */ 
	
	private static final long serialVersionUID = 6425330767377194602L;
	/**
	 * 菜单编码
	 */
	private String menuCode = "";
	/**
	 * 页面类型
	 */
	private String pageType = "";
	/**
	 * 页面标题后缀
	 * <p>当有pageTitleName属性且值不为空时，该属性无效
	 */
	private String suffixPageName = "";
	/**
	 * 页面标题前缀
	 * <p>当有pageTitleName属性且值不为空时，该属性无效
	 */
	private String prefixPageName = "";
	/**
	 * 页面标题全称
	 */
	private String pageTitleName = "";
	
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getSuffixPageName() {
		return suffixPageName;
	}

	public void setSuffixPageName(String suffixPageName) {
		this.suffixPageName = suffixPageName;
	}

	public String getPrefixPageName() {
		return prefixPageName;
	}

	public void setPrefixPageName(String prefixPageName) {
		this.prefixPageName = prefixPageName;
	}

	public String getPageTitleName() {
		return pageTitleName;
	}

	public void setPageTitleName(String pageTitleName) {
		this.pageTitleName = pageTitleName;
	}

	@Override
	public int doEndTag() throws JspException {
//		if(StringUtils.isEmpty(menuCode)){
//			logger.error("传入menuCode参数为空!");
//		}
//
//		List<MenuFunction> functions = null;
//		Menu menu = null;
		try {
			//TODO
			logger.error("需要确认转换之后是否还需要存在");
//			IMenuFunctionService menuFunctionService = (IMenuFunctionService)SpringHelper.getBean("menuFunctionServiceImpl");
//			if(StringUtils.isEmpty(pageTitleName)){
//				IMenuService menuService = (IMenuService)SpringHelper.getBean("menuServiceImpl");
//				menu = menuService.findByCode(menuCode);
//				pageContext.setAttribute("pageTitleName", String.format("%s%s%s", prefixPageName,menu.getName(),suffixPageName));
//			}
//			
//			functions = menuFunctionService.queryFunctions(menuCode, pageType);
//			if(logger.isDebugEnabled()){
//				logger.debug(String.format("获得的bar对象数量:%s", functions.size()));
//			}
//			pageContext.setAttribute("functions", functions);
//			pageContext.include("/pub/brms/toolbar.jsp");
		} catch (Exception e) {
			logger.error("转向工具条页面出错!", e);
		} finally{
			//functions = null;
			//menu = null;
			//TODO
			logger.error("需要确认转换之后是否还需要存在");
		}
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		
		this.menuCode = null;
		this.pageTitleName = null;
		this.pageType = null;
		this.prefixPageName = null;
		this.suffixPageName = null;
	}
	
	
}
