package com.badou.platform.service;

import java.io.Serializable;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.util.ModuleZipVO;
import com.badou.platform.util.PluginZipVO;
import com.badou.plugins.install.model.PluginsInstallEntity;

/**
 * 资源操作记录实体service接口
 * @ClassName IResourceOperService
 * @Description TODO(这里用一句话描述这个类的作用)
 * @version 1.0.0
 */
public interface IResourceOperService extends IBaseSpringService<ResourceOperEntity, Serializable> {

	/**
	 * 根据云中心id和版本相关信息获取数据
	 * @param platformResourceId
	 * @return 资源操作实体
	 * @author chenjiabao
	 * @param version3 
	 * @param version2 
	 * @param version1 
	 * @param versionCode 
	 * @date 2018年3月2日上午10:53:30
	 */
	ResourceOperEntity findByPlatformResourceId(String platformResourceId, String versionCode, String version1, String version2, String version3);

	/**
	 * 更新操作状态
	 * @param oper
	 * @param status
	 * @param failMsg 
	 */
	void updateStatus(ResourceOperEntity oper, Integer status, String failMsg);

	/**
	 * 获取资源id
	 * @param oper
	 * @return 资源id
	 */
	String getResourceId(ResourceOperEntity oper);

	/**
	 * 更新模型
	 * @param vo
	 * @author chenjiabao
	 * @date 2018年3月2日下午3:37:33
	 * @throws Exception if has error(直接往外抛)
	 */
	void updateModule(ModuleZipVO vo,String platformResoueceLinkId) throws Exception;

	/**
	 * 根据模型id获取最后一次上传的资源操作记录
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年3月2日 下午2:12:19
	 * @Updator bduser18
	 * @UpdateDate 2018年3月2日 下午2:12:19
	 * @UpdateDesc (更新内容描述)
	 * @param moduleId
	 * @return 资源操作实体
	 * @throws Exception if has error(直接往外抛)
	 */
	ResourceOperEntity findLatestUpload(String moduleId) throws Exception;
	
	/**
	 * 创建模型
	 * @param vo
	 * @return 模型实体对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public MdModuleEntity createModule(ModuleZipVO vo) throws Exception;

	/**
	 * 更新插件
	 * @param vo
	 * @param platformResourceId
	 * @return 插件安装实体
	 * @throws Exception if has error(直接往外抛)
	 */
	PluginsInstallEntity updatePlugin(PluginZipVO vo, String platformResourceId) throws Exception;

	/**
	 * 新增插件
	 * @param vo
	 * @return 插件安装实体
	 * @throws Exception if has error(直接往外抛)
	 */
	PluginsInstallEntity createPlugin(PluginZipVO vo) throws Exception;
}
