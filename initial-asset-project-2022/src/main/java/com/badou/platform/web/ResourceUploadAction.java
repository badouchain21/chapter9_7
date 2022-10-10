package com.badou.platform.web;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.struts.struts2.JsonSaveTemplateAction;
import com.badou.core.runtime.global.Globals;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;
import com.badou.platform.PlatformConst;
import com.badou.platform.PlatformHelper;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.util.UploadModuleZipFactoryThread;
import com.badou.platform.util.UploadPluginZipFactoryThread;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.platform.vo.ResourceVersionVO;
import com.badou.platform.web.form.PlatformResourceForm;
import com.badou.plugins.install.model.PluginsInstallEntity;
import com.badou.plugins.install.service.IPluginsInstallService;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamIntegerUtils;

/**
 * 模型上传到云平台action功能
 * @ClassName ModuleDesignUploadAction
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author wjw
 * @Date 2018年3月1日 上午9:39:09
 * @Updator bduser18
 * @UpdateDate 2018年3月1日 上午9:39:09
 * @UpdateDesc (更新内容描述)
 * @version 1.0.0
 */
@Controller
public class ResourceUploadAction extends JsonSaveTemplateAction<PlatformResourceEntity,Serializable,PlatformResourceForm> {

	@Autowired
	private IModuleDesignService moduleDesignService;
	@Autowired
	private IPluginsInstallService pluginsInstallService;
	@Autowired
	private IPlatformResourceService platformResourceService;
	@Autowired
	private IResourceOperService resourceOperService;
	
	private Integer type;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	private String resourceId;
	
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * 根据模型id获取版本数据
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年2月28日 下午4:53:40
	 * @Updator bduser18
	 * @UpdateDate 2018年2月28日 下午4:53:40
	 * @UpdateDesc (更新内容描述)
	 * @return 统一接口封装对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public JsonReturnBean getVersionData() throws Exception {
		returnBean = new JsonReturnBean();
		try{
			if (StringUtils.isNotBlank(this.resourceId)) {
				String versionJson = platformResourceService.getVersionData(this.resourceId, this.type);
				returnBean.setMessage(versionJson);
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				returnBean.setBean(custForm);
			} else {
				returnBean.setMessage("模型id为空");
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
	
	/**
	 * 根据模型id和版本编码，先判断版本再上传模型数据到云中心
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年2月28日 下午7:38:35
	 * @Updator bduser18
	 * @UpdateDate 2018年2月28日 下午7:38:35
	 * @UpdateDesc (更新内容描述)
	 * @return 统一封装接口返回bean
	 * @throws Exception if has error(直接往外抛)
	 */
	public JsonReturnBean uploadResourceData() throws Exception {
		returnBean = new JsonReturnBean();
		try{
			String versionStr = ParamUtils.getParameter(request, "versionStr");
			String remark = ParamUtils.getParameter(request, "remark");
			Integer isNew = ParamIntegerUtils.getParameter(request, "isNew");
			if (StringUtils.isNotBlank(this.resourceId) && StringUtils.isNotBlank(versionStr)) {
				ResourceVersionVO versionVo = parsingVersionstr(versionStr);
				// 创建本地的云中心资源数据
				Map<String,Object> returnMap = platformResourceService.createUploadData(this.resourceId,this.type,versionVo,remark,isNew);
				
				returnBean = (JsonReturnBean)returnMap.get("returnBean");
				if(returnBean.isHasOk()){
					//这里才是上传数据
					String versionResourceId = (String)returnMap.get("versionResourceId");
  					String postEntity = "&resourceVersionId="+versionResourceId+"&remark="+URLEncoder.encode(StringUtils.isNotBlank(remark)?remark:"","UTF-8");
					ApiReturnVO returnDataJson = PlatformHelper.httpPost("project/save.do", null, PlatformHelper.assemblyParam(postEntity));
					
					if(ApiReturnVO.SUCCESS.equals(returnDataJson.getReturnCode())){
						//获取最新版本,保存操作记录
						ResourceOperEntity operEntity = (ResourceOperEntity)returnMap.get("operEntity");
						
						JSONObject jo = JSONObject.fromObject(returnDataJson.getData());
						String url = jo.getString("url");
						operEntity.setUrl(url);
						resourceOperService.update(operEntity);
						/*
						 * 此步骤需要异步操作
						 */
						Map<String,String> postMap = platformResourceService.getUploadFileParams(versionResourceId);
						if(PlatformConst.RESOUTCE_TYPE_MODULE.equals(this.type)){
							new UploadModuleZipFactoryThread(operEntity, this.resourceId, postMap).run();
						}else if(PlatformConst.RESOUTCE_TYPE_PLUGIN.equals(this.type)){
							new UploadPluginZipFactoryThread(operEntity, this.resourceId, postMap).run();
						}
					} else {
						returnBean.setHasOk(false);
						returnBean.setTip(JsonReturnBean.TIP_FAIL);
						returnBean.setMessage(returnDataJson.getReturnMsg());
					}
					
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
	
	/**
	 * 解析version数据，返回模型版本vo
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年3月1日 上午11:42:49
	 * @Updator bduser18
	 * @UpdateDate 2018年3月1日 上午11:42:49
	 * @UpdateDesc (更新内容描述)
	 * @return 资源版本vo
	 * @throws Exception if has error(直接往外抛)
	 */
	private ResourceVersionVO parsingVersionstr(String versionStr) throws Exception{
		if(StringUtils.isBlank(versionStr)){
			return null;
		}
		String[] split = versionStr.split(Globals.INFOS_SPLITOR_UL);
		ResourceVersionVO vo = new ResourceVersionVO(split[0],split[1],split[2],split[3],null);
		if(PlatformConst.RESOUTCE_TYPE_MODULE.equals(this.type)){
			MdModuleEntity module = moduleDesignService.find(this.resourceId);
			vo.setResourceCode(module.getCode());
			vo.setResourceName(module.getModuleName());
		}else if(PlatformConst.RESOUTCE_TYPE_PLUGIN.equals(this.type)){
			PluginsInstallEntity plugin = pluginsInstallService.find(this.resourceId);
			vo.setResourceCode(plugin.getCode());
			vo.setResourceName(plugin.getName());
		}
		vo.setType(this.type);
		return vo;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:03:05
	 * @todo 批量上传模型数据接口
	 * @return 统一接口返回对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public JsonReturnBean batchUploadModuleData() throws Exception {
		returnBean = new JsonReturnBean();
		try {
			List<Map<String, Object>> noExistModules = platformResourceService.findNoExistCenterModule();
			returnBean.setBean(noExistModules);
			returnBean.setHasOk(true);
		} catch (Exception e) {
			logger.error("批量上传模型失败！", e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage("批量上传模型失败！");
		}
		
		return returnBean;
	}
}
