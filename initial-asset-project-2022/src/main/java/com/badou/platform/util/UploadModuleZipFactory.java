package com.badou.platform.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.dictionary.model.DictionaryEntity;
import com.badou.brms.dictionary.service.IDictionaryService;
import com.badou.brms.util.InstanceFactory;
import com.badou.designer.module.design.ModuleCacheContainer;
import com.badou.designer.module.design.ModuleConstants;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.platform.PlatformConst;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.tools.common.util.FileUtils;
import com.badou.tools.common.util.SpringHelper;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:02:19
 * @todo 上传模型zip包文件工厂
 */
public class UploadModuleZipFactory extends UploadZipFactory{
	
	private final static String ZIP_TAG_NAME = "module";
	
	private static UploadModuleZipFactory instance = new UploadModuleZipFactory();
	
	public static UploadModuleZipFactory getInstance(){
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
				//String resourceId = resourceOperService.getResourceId(oper);
				MdModuleEntity module = ModuleCacheContainer.getInstance().get(resourceId);
				Map<String,DictionaryEntity> dicMap = new HashMap<String,DictionaryEntity>();
				IDictionaryService dictionaryService = SpringHelper.getBean(IDictionaryService.class);
				for(MdFieldEntity field : module.getFields()){
					//编辑的数据字典
					if(StringUtils.isNotBlank(field.getForm().getDicName())&&!dicMap.containsKey(field.getForm().getDicName())){
						DictionaryEntity entity = dictionaryService.findByCode(field.getForm().getDicName());
						dicMap.put(field.getForm().getDicName(), entity);
					}
					//搜索的数据字典
					if (StringUtils.isNotBlank(field.getForm().getSearchData())&&ModuleConstants.DATA_SOURCE_DIC.equals(field.getForm().getSearchDataSource())&&!dicMap.containsKey(field.getForm().getSearchData())) {
						DictionaryEntity entity = dictionaryService.findByCode(field.getForm().getSearchData());
						dicMap.put(field.getForm().getSearchData(), entity);
					}
				}
				ModuleZipVO vo = new ModuleZipVO();
				vo.setVersion(oper.getVersion());
				vo.setModule(module);
				vo.setDicMap(dicMap);
				vo.setVersionCode(oper.getVersionCode());
				vo.setVersion1(oper.getVersion1());
				vo.setVersion2(oper.getVersion2());
				vo.setVersion3(oper.getVersion3());
				
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
	 	
			IPlatformResourceService platformResourceService = InstanceFactory.getInstance(IPlatformResourceService.class);
			PlatformResourceEntity platformResourceEntity = platformResourceService.get(oper.getPlatformResourceId());
			 if(ApiReturnVO.SUCCESS.equals(retrunObj.getReturnCode())){
				resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_SUCCESS, null);
				platformResourceEntity.setResourceModifyTime(new Date());
				platformResourceEntity.setStatus(PlatformConst.UPLOAD_STATUS_SUCCESS);
			}else{
				resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_FAIL, retrunObj.getReturnMsg());
				platformResourceEntity.setStatus(PlatformConst.UPLOAD_STATUS_FAIL);
			} 
			 platformResourceService.update(platformResourceEntity);
		} catch (Exception e) {
			resourceOperService.updateStatus(oper, PlatformConst.UPLOAD_STATUS_FAIL, e.getMessage());
			throw e;
		}
	}
}
