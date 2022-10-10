package com.badou.platform.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.util.InstanceFactory;
import com.badou.platform.PlatformConst;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.plugins.install.model.PluginsInstallEntity;
import com.badou.plugins.install.service.IPluginsInstallService;
import com.badou.tools.common.util.FileUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:00:40
 * @todo 上传插件zip包工厂类
 */
public class UploadPluginZipFactory extends UploadZipFactory{
	
	private final static String ZIP_TAG_NAME = "plugin";
	
	private static UploadPluginZipFactory instance = new UploadPluginZipFactory();
	
	public static UploadPluginZipFactory getInstance(){
		return instance;
	}
	
	/**
	 * 根据对象上传文件，并同时更新资源操作记录状态
	 */
	@Override
	public void uploadFile(ResourceOperEntity oper,String resourceId,Map<String,String> dataMap) throws Exception {
		IResourceOperService resourceOperService = InstanceFactory.getInstance(IResourceOperService.class);
		try {
			resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_DOING, null);
			String zipFilePath = null;
			if(StringUtils.isBlank(oper.getAttachId())){
				//文件存放的缓存路径 /home/zt/temp/attach/201832/module-userv001
				String tempPath = getTempPath() + File.separator + getFileName(ZIP_TAG_NAME, oper.getVersion());
				File tempfile = new File(tempPath);
				if(!tempfile.exists()){
					tempfile.mkdir();
				}
				
				//文件组装
				PluginsInstallEntity plugin = InstanceFactory.getInstance(IPluginsInstallService.class).find(resourceId);
				PluginZipVO vo = new PluginZipVO();
				vo.setVersion(oper.getVersion());
				vo.setPlugin(plugin);
				vo.setVersionCode(oper.getVersionCode());
				vo.setVersion1(oper.getVersion1());
				vo.setVersion2(oper.getVersion2());
				vo.setVersion3(oper.getVersion3());
				
				if(StringUtils.isNotBlank(plugin.getXmlId())){
					copyAttach(plugin.getXmlId(),plugin.getXmlName(),tempPath);
				}
				if(StringUtils.isNotBlank(plugin.getJarId())){
					copyAttach(plugin.getJarId(),plugin.getJarName(),tempPath);
				}
				
				//根据VO类生成文件到存放的缓存地址 /home/zt/temp/attach/201832/module-userv001/module-userv001.json
				File file = new File(tempPath + File.separator + getFileName(ZIP_TAG_NAME, oper.getVersion())+".json"); 
				FileUtils.writeStringToFile(file, JSONObject.toJSONString(vo,SerializerFeature.WriteMapNullValue));
				
				//压缩文件 压缩到 /home/zt/temp/attach/201832/module-userv001.zip
				zipFilePath = zip(file,tempPath,null);
				
				//保存附件
				Attach attach = saveAttach(zipFilePath, oper.getId());
				oper.setAttachId(attach.getId());
			}else{
				Attach attach = InstanceFactory.find(Attach.class, oper.getAttachId());
				zipFilePath = attach.getFileName();
			}
			//上传压缩包到云中心
			String returnStr = uploadZip(oper.getUrl(), zipFilePath, dataMap);
			ApiReturnVO retrunObj = JSONObject.parseObject(returnStr, ApiReturnVO.class);
	 	
			 if(ApiReturnVO.SUCCESS.equals(retrunObj.getReturnCode())){
				resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_SUCCESS, null);
			}else{
				resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_FAIL, retrunObj.getReturnMsg());
			} 
		} catch (Exception e) {
			resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_FAIL, e.getMessage());
			throw e;
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:49:31
	 * @todo 拷贝文件
	 * @param attachId
	 * @param fileName
	 * @param tempPath
	 * @throws Exception if has error(直接往外抛)
	 */
	private void copyAttach(String attachId, String fileName, String tempPath) throws Exception {
		AttachFactory attachFactory = new AttachFactory();
		Attach attach = InstanceFactory.find(Attach.class, attachId);
		File srcfile = new File(attachFactory.getFilePath(attach));
		if(srcfile.exists()){
			File targetFile = new File(tempPath + File.separator + fileName);
			FileUtils.copyFile(srcfile, targetFile);
		}
	}
}
