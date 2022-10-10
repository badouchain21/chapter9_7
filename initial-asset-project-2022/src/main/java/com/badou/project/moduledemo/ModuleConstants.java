package com.badou.project.moduledemo;
/**
 * 菜单相关常量
 */
public final class ModuleConstants {
	/**
	 * 菜单类型：1模块，2菜单
	 */
	public static final String MENU_TYPE_MODULE = "1";
	public static final String MENU_TYPE_MENU = "2";
	/**
	 * 菜单打开目标窗口:NEW新窗口，TOP顶层窗口，PARENT父窗口，xxx自定义
	 */
	public static final String MENU_TARGETWIN_NEW = "NEW";
	public static final String MENU_TARGETWIN_TOP = "TOP";
	public static final String MENU_TARGETWIN_PARENT = "PARENT";
	
	/**
	 * 功能点页面类型{@link #MenuFunction.pageType}
	 * <P>目前定义四种form,list,view,ref_link，可在页面上自定义输入
	 */
	public static final String MENU_PAGETYPE_FORM = "FORM";
	public static final String MENU_PAGETYPE_LIST = "LIST";
	public static final String MENU_PAGETYPE_VIEW = "VIEW";
	public static final String MENU_PAGETYPE_REFLINK = "REF_LINK";
}
