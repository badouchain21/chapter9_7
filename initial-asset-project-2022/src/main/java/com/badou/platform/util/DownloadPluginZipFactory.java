package com.badou.platform.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.util.InstanceFactory;
import com.badou.platform.PlatformConst;
import com.badou.platform.PlatformHelper;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.plugins.install.model.PluginsInstallEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:53:07
 * @todo 插件zip包下载工厂类
 */
public class DownloadPluginZipFactory extends DownloadZipFactory{
	
	private final static String ZIP_TAG_NAME = "plugin";

	private static DownloadPluginZipFactory instance = new DownloadPluginZipFactory();
	
	public static DownloadPluginZipFactory getInstance(){
		return instance;
	}

	/**
	 * 下载文件，更新资源，同时更新资源操作记录的状态
	 */
	@Override
	public void downloadFile(ResourceOperEntity oper) throws Exception {
		IResourceOperService resourceOperService = InstanceFactory.getInstance(IResourceOperService.class);
		try {
			String zipFilePath = null;
			if(StringUtils.isBlank(oper.getAttachId())){
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_DOING, null);
				
				//下载目标路径
				String targetPath = getTempPath();
				File targetFile = new File(targetPath);
				if(!targetFile.exists()){
					targetFile.mkdir();
				}
				//下载文件路径
				String filePath = targetPath + File.separator + getFileName(ZIP_TAG_NAME, oper.getVersion()) + ".zip";
				//下载文件
				Map dataMap = PlatformHelper.assemblyDefaultParam(null);
				downloadFile(oper.getUrl(), filePath, dataMap);
				
				//保存附件
				Attach attach = saveAttach(filePath, oper.getId());
				oper.setAttachId(attach.getId());
				zipFilePath = attach.getFileName();
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_SUCCESS, null);
			}else{
				Attach attach = InstanceFactory.find(Attach.class, oper.getAttachId());
				zipFilePath = attach.getFileName();
			}
			
			updateResource(zipFilePath, oper);
		} catch (Exception e) {
			resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_FAIL, e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 更新资源
	 * @param filePath
	 * @param oper
	 * @throws Exception if has error(则进行事务回滚。并记录相关操作日志)
	 */
	public void updateResource(String filePath, ResourceOperEntity oper) throws Exception{
		if(!PlatformConst.UPDATE_OPER_TYPE_ONLY_DOWNLOAD.equals(oper.getUpdateType())){
			IResourceOperService resourceOperService = InstanceFactory.getInstance(IResourceOperService.class);
			IPlatformResourceService platformResourceService = InstanceFactory.getInstance(IPlatformResourceService.class);
			try {
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_UPDATE, null);
				//获取文件流
				String tempPath = unzip(filePath, null);
				String content = readToString(tempPath + File.separator + getFileName(ZIP_TAG_NAME, oper.getVersion()) + ".json");
				PluginZipVO vo = JSONObject.parseObject(content, PluginZipVO.class);
				File jar = new File(tempPath + File.separator + vo.getPlugin().getJarName());
				if(jar.exists()){
					vo.setJarFilePath(jar.getAbsolutePath());
				}
				File xml = new File(tempPath + File.separator + vo.getPlugin().getXmlName());
				if(xml.exists()){
					vo.setXmlFilePath(xml.getAbsolutePath());
				}
				PluginsInstallEntity plugin = null;
				if(PlatformConst.UPDATE_OPER_TYPE_COVER_UPDATE.equals(oper.getUpdateType())){
					plugin = resourceOperService.updatePlugin(vo,oper.getPlatformResourceId());
					resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_FINISH, null);
				}
				if(PlatformConst.UPDATE_OPER_TYPE_FIRST_COVER_UPDATE.equals(oper.getUpdateType())){
					plugin = resourceOperService.createPlugin(vo);
					resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_FINISH, null);
				} 

				if (plugin == null) {
					throw new RuntimeException("plugin 不能为空");
				}

				PlatformResourceEntity t = platformResourceService.find(oper.getPlatformResourceId());
				t.setResourceId(plugin.getId());
				t.setVersion1(oper.getVersion1());
				t.setVersion2(oper.getVersion2());
				t.setVersion3(oper.getVersion3());
				t.setVersionCode(oper.getVersionCode());
				platformResourceService.update(t);
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_FINISH, null);
			} catch (Exception e) {
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_UPDATE_FAIL, e.getMessage());
				throw e;
			}
		}
	}
}
