package com.badou.platform.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.struts.struts2.JsonSaveTemplateAction;
import com.badou.platform.PlatformConst;
import com.badou.platform.PlatformHelper;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.util.DownloadModuleZipFactoryThread;
import com.badou.platform.util.DownloadPluginZipFactoryThread;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.platform.web.form.PlatformResourceForm;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.param.ParamIntegerUtils;

/**
 * 时间：2018年2月28日<br>
 * 作者：chenjiabao<br>
 * 功能：模型更新功能，不进行覆盖操作
 */
@Controller
public class ResourceUpdateAction extends JsonSaveTemplateAction<PlatformResourceEntity, Serializable, PlatformResourceForm>{

	@Autowired
	private IPlatformResourceService platformResourceService;
	
	@Autowired
	private IResourceOperService resourceOperService ;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:55:42
	 * @todo 设置默认service
	 * @param platformResourceService
	 */
	@Autowired
	public void setModuleDesignService(IPlatformResourceService platformResourceService) {
		this.platformResourceService = platformResourceService;
		super.setBaseService(platformResourceService);
	}
	
	/**
	 * 更新资源
	 * @return 统一接口返回对象
	 */
	public JsonReturnBean updateResource(){
		returnBean = new JsonReturnBean();
		try {
			String versionId = ParamUtils.getParameter(request, "versionId");
			String resourceId = ParamUtils.getParameter(request, "resourceId");
			String versionCode = ParamUtils.getParameter(request, "versionCode");
			String version1 = ParamUtils.getParameter(request, "version1");
			String version2 = ParamUtils.getParameter(request, "version2");
			String version3 = ParamUtils.getParameter(request, "version3");
			Integer type = ParamIntegerUtils.getParameter(request, "type");
			Integer operType = ParamIntegerUtils.getParameter(request, "operType");
			Integer updateType = ParamIntegerUtils.getParameter(request, "updateType");
			String platformResourceId = ParamUtils.getParameter(request, "platformResourceId");
			//判断本地是否存在相应的版本文件
			String resourceOprId = this.platformResourceService.isExistLocal(resourceId, type, versionCode, version1, version2, version3);
			if(StringUtils.isBlank(resourceOprId)){
				if(StringUtils.isNotBlank(resourceId)){
					resourceOprId = this.platformResourceService.updateResource(versionId , resourceId , versionCode , version1 , version2 , version3 , type , operType , updateType);
				}else if(StringUtils.isBlank(resourceId)&& StringUtils.isNotBlank(platformResourceId)){
					resourceOprId = this.platformResourceService.createResource(versionId , platformResourceId , versionCode , version1 , version2 , version3 , type , operType , updateType);
				}
			}
			returnBean.setBean(resourceOprId);
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}

	/**
	 * 下载资源
	 * @return 统一接口返回对象
	 */
	public JsonReturnBean downloadResource(){
		returnBean = new JsonReturnBean();
		try {
			String resourceId = ParamUtils.getParameter(request, "resourceId");
			Integer type = ParamIntegerUtils.getParameter(request, "type");
			ResourceOperEntity oper = this.resourceOperService.find(resourceId);
			if(oper != null){
				if(PlatformConst.RESOUTCE_TYPE_MODULE.equals(type)){
					new DownloadModuleZipFactoryThread(oper).run();
				}else if(PlatformConst.RESOUTCE_TYPE_PLUGIN.equals(type)){
					new DownloadPluginZipFactoryThread(oper).run();
				}
			}
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
	
	/**
	 * 获取批量更新模型的模型数据
	 * @return 统一接口返回对象
	 */
	public JsonReturnBean batchUpdateResource(){
		returnBean = new JsonReturnBean();
		returnBean.setHasOk(false);
		returnBean.setTip(JsonReturnBean.TIP_FAIL);
		try {
			Integer resourceType = ParamIntegerUtils.getParameter(request, "resourceType");
			// 查询已经从云中心下载的模型
			List<PlatformResourceEntity> list = platformResourceService.queryAllResource(resourceType);
			if(list.size() == 0) {
				// 还没从云中心下载过，没有可以更新的模型
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				return returnBean;
			}
			
			Map<String , Object> params = new HashMap<String , Object>();
			params.put("resourceType", resourceType);
			ApiReturnVO returnDataJson = PlatformHelper.httpPost("project/batchUpdateResource.do", null, PlatformHelper.assemblyParam(params));
			if (ApiReturnVO.SUCCESS.equals(returnDataJson.getReturnCode())) {
				// 从云中心中获取到的是所有的模型信息，这里排除掉不需要更新的模型
				net.sf.json.JSONArray json = platformResourceService.findUpdatResource(list, (String) returnDataJson.getData());
				returnBean.setBean(json);
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			} else {
				returnBean.setMessage(returnDataJson.getReturnMsg());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	} 
}
