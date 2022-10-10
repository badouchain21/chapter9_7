package com.badou.platform.web;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.platform.PlatformHelper;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.platform.web.form.PlatformResourceForm;
import com.badou.project.common.util.ProjectPropertiesLoader;
import com.badou.tools.common.util.param.ParamIntegerUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:09:42
 * @todo 资源下载列表接口
 */
@RestController
@RequestMapping("/platform/resourcedownloadlist")
public class ResourceDownloadListAction  extends JsonListTemplateAction<PlatformResourceEntity,Serializable,PlatformResourceForm>{

		
	@Autowired
	private IPlatformResourceService platformResourceService;
	
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
	
	protected LigeruiListVO<Map> data;
	
	public LigeruiListVO<Map> getData() {
		return data;
	}

	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult(name="list",location="/WEB-INF/jsp/platform/resourcelist_list.jsp")
	public String list() throws Exception {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE, 
				DefaultPropertiesLoader.CONFIG_FILE);
		return "list";
	}*/
	
	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午4:59:12
	 * @todo 获取列表对象接口
	 * @return 返回对象集合vo
	 */
	public LigeruiListVO listJSON() {
		try{
			data = new LigeruiListVO<Map>();
			//获取项目的模型
			Map<String , Object> params = new HashMap<String , Object>();
			Pagelet pagelet = (Pagelet)PaginationTheadLocal.getDefaultPagelet();
			params.put("pageNo", pagelet.getPageNo());
			params.put("pageSize", pagelet.getPerPageSize());
			params.put("type", this.type);
			ApiReturnVO returnDataJson = PlatformHelper.httpPost("project/listNotExistsResource.do", null, PlatformHelper.assemblyParam(params));
			
			JSONObject json = JSONObject.fromObject(returnDataJson.getData()) ;
			JSONArray ja = json.getJSONArray("rows");
			List<String> resourceIds = new ArrayList<String>();
			for(int i=0;i<ja.size();i++){
				resourceIds.add(ja.getJSONObject(i).getString("resourceId"));
			}
			if(resourceIds.size()>0){
				List<String> existIds = this.platformResourceService.isExistLocal(resourceIds, this.type);
				for(int i=0;i<ja.size();i++){
					if(existIds.contains(ja.getJSONObject(i).getString("resourceId"))){
						ja.getJSONObject(i).put("isLocal", true);
					}else{
						ja.getJSONObject(i).put("isLocal", false);
					}
				}
				data.setTotal(Integer.valueOf(json.get("total").toString()));
				data.setTRows(ja);
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return data;
	}
	
	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult(name="versionList",location="/WEB-INF/jsp/platform/resourcedownloadversionlist_list.jsp")
	public String versionList() throws Exception {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE, 
				DefaultPropertiesLoader.CONFIG_FILE);
  		return "versionList";
	}*/
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:00:25
	 * @todo 版本数据接口
	 * @return 版本数据集合vo
	 * @throws Exception if has error(直接往外抛)
	 */
	public LigeruiListVO versionListJSON() throws Exception {
		try{
			data = new LigeruiListVO<Map>();
			//获取项目的模型
			Map<String , Object> params = new HashMap<String , Object>();
			Pagelet pagelet = (Pagelet)PaginationTheadLocal.getDefaultPagelet();
			params.put("pageNo", pagelet.getPageNo());
			params.put("pageSize", pagelet.getPerPageSize());
			params.put("resourceId", this.resourceId);
			params.put("type", this.type);
			ApiReturnVO returnDataJson = PlatformHelper.httpPost("project/listVersion.do", null, PlatformHelper.assemblyParam(params));
			JSONObject json = JSONObject.fromObject(returnDataJson.getData()) ;
			JSONArray ja = json.getJSONArray("rows");
			data.setTotal(Integer.valueOf(json.get("total").toString()));
			data.setTRows(ja);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw e;
		}
		return data;
	}
 	
 	
 	/**
 	 * 获取存在于云中心，不存在在于本地的模型数据，用于批量下载模型
 	 * @throws Exception if has error(直接往外抛)
 	 */
	public void batchDownloadResource() throws Exception {
 		JsonReturnBean returnBean = new JsonReturnBean();
 		returnBean.setHasOk(false);
		returnBean.setTip(JsonReturnBean.TIP_FAIL);
		try {
			Integer resourceType = ParamIntegerUtils.getParameter(request, "resourceType");
			Map<String , Object> params = new HashMap<String , Object>();
			
			// 查询已存在与本地的模型，这些模型不再下载
			List<PlatformResourceEntity> list = platformResourceService.queryAllResource(resourceType);
			if(list.size() > 0) {
				StringBuilder excludeIds = new StringBuilder();
				for (PlatformResourceEntity pr : list) {
					excludeIds.append(",").append(pr.getPlatformResourceId());
				}
				params.put("excludeIds", excludeIds.substring(1));
			}
			
			params.put("resourceType", resourceType);
			params.put("projectId", ProjectPropertiesLoader.getProjectId());
			ApiReturnVO returnDataJson = PlatformHelper.httpPost("project/batchDownloadResource.do", null, PlatformHelper.assemblyParam(params));
			if (ApiReturnVO.SUCCESS.equals(returnDataJson.getReturnCode())) {
				returnBean.setBean(JSON.parse((String) returnDataJson.getData()));
				returnBean.setHasOk(true);
				returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			} else {
				returnBean.setMessage(returnDataJson.getReturnMsg());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			returnBean.setMessage(e.getMessage());
		}
		
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JSON.toJSONString(returnBean));
		}
	}
}
