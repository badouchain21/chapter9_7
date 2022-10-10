package com.badou.platform;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:26:48
 * @todo 平台常量类
 */
public class PlatformConst {
	/**
	 * 未上传
	 */
	public static final Integer UPLOAD_STATUS_UN_DO = 0;
	
	/**
	 * 上传中
	 */
	public static final Integer UPLOAD_STATUS_DOING = 1;
	
	/**
	 * 上传成功
	 */
	public static final Integer UPLOAD_STATUS_SUCCESS = 2;
	
	/**
	 * 上传失败
	 */
	public static final Integer UPLOAD_STATUS_FAIL = 3;
	
	/**
	 * 未下载
	 */
	public static final Integer DOWNLOAD_STATUS_UN_DO = 0;
	
	/**
	 * 下载中
	 */
	public static final Integer DOWNLOAD_STATUS_DOING = 1;
	
	/**
	 * 下载完成
	 */
	public static final Integer DOWNLOAD_STATUS_SUCCESS = 2;
	
	/**
	 * 下载失败
	 */
	public static final Integer DOWNLOAD_STATUS_FAIL = 3;
	
	/**
	 * 下载更新中
	 */
	public static final Integer DOWNLOAD_STATUS_UPDATE = 4;
	
	/**
	 * 下载更新完成
	 */
	public static final Integer DOWNLOAD_STATUS_FINISH = 5;
	
	/**
	 * 下载更新失败
	 */
	public static final Integer DOWNLOAD_STATUS_UPDATE_FAIL = 6;
	
	/**
	 * 操作类型：上传
	 */
	public static final Integer OPER_TYPE_UPLOAD = 0;
	
	/**
	 * 操作类型：更新
	 */
	public static final Integer OPER_TYPE_UPDATE = 0;
	
	/**
	 * 更新操作类型：仅下载
	 */
	public static final Integer UPDATE_OPER_TYPE_ONLY_DOWNLOAD = 0;
	
	/**
	 * 更新操作类型：覆盖更新
	 */
	public static final Integer UPDATE_OPER_TYPE_COVER_UPDATE = 1;
	
	/**
	 * 更新操作类型：初次下载更新
	 */
	public static final Integer UPDATE_OPER_TYPE_FIRST_COVER_UPDATE = 2;
	
	/**
	 * 资源类型：模型
	 */
	public static final Integer RESOUTCE_TYPE_MODULE = 0;
	/**
	 * 资源类型：插件
	 */
	public static final Integer RESOUTCE_TYPE_PLUGIN = 1;
	
	/**
	 * 上传状态--数据字典
	 */
	public static final String DIC_UPLOAD_STATUS = "UPLOAD_STATUS";
}
