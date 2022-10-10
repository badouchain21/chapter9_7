package com.badou.platform.util;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.util.InstanceFactory;
import com.badou.designer.jdbc.core.service.IBaseCommonService;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.platform.PlatformConst;
import com.badou.platform.PlatformHelper;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:50:28
 * @todo 模型zip包下载工厂类
 */
public class DownloadModuleZipFactory extends DownloadZipFactory{
	
	private final static String ZIP_TAG_NAME = "module";

	private static DownloadModuleZipFactory instance = new DownloadModuleZipFactory();
	
	public static DownloadModuleZipFactory getInstance(){
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
	 * @throws Exception if has error(则不更新资源并记录相关日志)
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
				ModuleZipVO vo = JSONObject.parseObject(content, ModuleZipVO.class);
				PlatformResourceEntity t = platformResourceService.find(oper.getPlatformResourceId());
				if(PlatformConst.UPDATE_OPER_TYPE_COVER_UPDATE.equals(oper.getUpdateType())){
					//更新模型（包括数据字典）
					resourceOperService.updateModule(vo,oper.getPlatformResourceId());
					t.setStatus(PlatformConst.DOWNLOAD_STATUS_SUCCESS);
				}
				if(PlatformConst.UPDATE_OPER_TYPE_FIRST_COVER_UPDATE.equals(oper.getUpdateType())){
					//初次下载模型（包括数据字典）
					MdModuleEntity module = resourceOperService.createModule(vo);
					IBaseCommonService baseModuleService = InstanceFactory.getInstance(IBaseCommonService.class);
					baseModuleService.initModule(module);
  					t.setResourceId(module.getId());
 					t.setVersion1(oper.getVersion1());
 					t.setVersion2(oper.getVersion2());
 					t.setVersion3(oper.getVersion3());
 					t.setVersionCode(oper.getVersionCode());
 					t.setResourceId(module.getId());
 					t.setStatus(PlatformConst.DOWNLOAD_STATUS_SUCCESS);
 				} 
				t.setResourceModifyTime(new Date());
				platformResourceService.update(t);

				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_FINISH, null);
			} catch (Exception e) {
				resourceOperService.updateStatus(oper, PlatformConst.DOWNLOAD_STATUS_UPDATE_FAIL, e.getMessage());
				throw e;
			}
		}
	}
}
