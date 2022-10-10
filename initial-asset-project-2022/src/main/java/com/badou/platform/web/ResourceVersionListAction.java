package com.badou.platform.web;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.web.form.PlatformResourceForm;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:10:08
 * @todo 资源版本列表接口类
 */
@RestController
@RequestMapping("/platform/resourceversionlist")
public class ResourceVersionListAction extends JsonListTemplateAction<PlatformResourceEntity, Serializable, PlatformResourceForm> {

	@Autowired
	private IPlatformResourceService platformResourceService;
	
	protected JsonReturnBean returnBean;
	
	@JSONField(name="rbean")
	public JsonReturnBean getReturnBean() {
		return returnBean;
	}
	
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
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:01:28
	 * @todo 设置默认service
	 * @param platformResourceService
	 */
	@Autowired
	public void setModuleDesignService(IPlatformResourceService platformResourceService) {
		this.platformResourceService = platformResourceService;
		super.setBaseService(platformResourceService);
	}
	
	/*@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult(name="list",location="/WEB-INF/jsp/platform/resourceversionlist_list.jsp")
	public String list() throws Exception {
		pageSize = PropertyUtils.getIntProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_PAGESIZE,
				DefaultPropertiesLoader.CONFIG_FILE);
		pageOptions = PropertyUtils.getProperty(DefaultPropertiesLoader.SDAP_APPFRAMEWORK_OPTIONS_PAGESIZE, 
				DefaultPropertiesLoader.CONFIG_FILE);
		return "list";
	}
	
	@Action(interceptorRefs = @InterceptorRef("baseJsonStack"))
	@DispatcherResult(name="upload",location="/WEB-INF/jsp/platform/platformresource_upload.jsp")
	public String upload() throws Exception {
		return "upload";
	}*/
	
	/**
	 * 根据模型id判断是否在云中心存在资源
	 * @Description (TODO描述这个方法的作用)
	 * @author zlz
	 * @Date 2018年2月28日 下午4:53:40
	 * @Updator bduser18
	 * @UpdateDate 2018年2月28日 下午4:53:40
	 * @UpdateDesc (更新内容描述)
	 * @return 统一接口返回对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public JsonReturnBean judgeLocal() throws Exception {
		returnBean = new JsonReturnBean();
		try{
			if (StringUtils.isNotBlank(resourceId)) {
				PlatformResourceEntity entity = this.platformResourceService.findByResourceId(this.resourceId, this.type);
				if(entity == null){
					returnBean.setMessage("云中心未存在资源,无法更新！");
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				}else{
					returnBean.setMessage(null);
					returnBean.setHasOk(true);
					returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				}
			 
 			} else {
				returnBean.setMessage("未选择模型！");
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
 	
	@Override
	public LigeruiListVO<PlatformResourceForm> listJSON() {
		try{
			//获取版本列表数据
			listvo = this.platformResourceService.getAllVersionList(this.resourceId, this.type);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return listvo;
	}
}
