package com.badou.platform.web.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.brms.dboperation.query.ICriterion;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.QueryParamGroup;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.designer.module.database.model.ModuleDatabaseEntity;
import com.badou.designer.module.database.service.IModuleDatabaseService;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;
import com.badou.designer.module.design.web.form.ModuleDesignListForm;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.vo.ModuleDesignListVO;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.bean.BeanUtils;

/**
 * 用于有云中心功能的模型列表展示action
 * @ClassName PlatformModuleDesignListAction
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author wjw
 * @Date 2018年3月2日 下午12:19:07
 * @Updator bduser18
 * @UpdateDate 2018年3月2日 下午12:19:07
 * @UpdateDesc (更新内容描述)
 * @version 1.0.0
 */
@RestController
@RequestMapping("/platform/plateformmoduledesignlist")
public class PlatformModuleDesignListAction extends JsonListTemplateAction<MdModuleEntity, Serializable, ModuleDesignListForm> {

	private static final long serialVersionUID = -7581124566484666360L;

	private IModuleDesignService moduleDesignService;
	@Autowired
	private IModuleDatabaseService moduleDataBaseService;
	@Autowired
	private IResourceOperService resourceOperService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:55:42
	 * @todo 设置默认service
	 * @param moduleDesignService
	 */
	@Autowired
	public void setModuleDesignService(IModuleDesignService moduleDesignService) {
		this.moduleDesignService = moduleDesignService;
		super.setBaseService(moduleDesignService);
	}
	
	@Override
	protected void exeBeforeList() throws Exception {
		QueryCriterion queryCriterion = new QueryCriterion();
		String code = ParamUtils.getParameter(request, "code");
		if (StringUtils.isNotBlank(code)) {
			queryCriterion.addParamGroup(new QueryParamGroup(QueryOperSymbolEnum.or)
			.addParams(new QueryHibernatePlaceholderParam("moduleName", code, null, QueryOperSymbolEnum.like,QueryParam.PARAM_PLACEHOLDER_NAME))
			.addParams(new QueryHibernatePlaceholderParam("tableName", code, null, QueryOperSymbolEnum.like,QueryParam.PARAM_PLACEHOLDER_NAME))
			.addParams(new QueryHibernatePlaceholderParam("dbTable", code, null, QueryOperSymbolEnum.like,QueryParam.PARAM_PLACEHOLDER_NAME))
			.addParams(new QueryHibernatePlaceholderParam("code", code, null, QueryOperSymbolEnum.like,QueryParam.PARAM_PLACEHOLDER_NAME)));
		}
		request.setAttribute(Request_Criterion, queryCriterion);
	}
	
	@Override
	public LigeruiListVO<ModuleDesignListForm> listJSON() {
		try{
			this.exeBeforeList();
			ICriterion criterion = (ICriterion)request.getAttribute(Request_Criterion);
			Pagelet page = moduleDesignService.listPages(criterion);
			dataSource(page);
			//获取更新状态
			uploadState(page);
			listvo = new LigeruiListVO<ModuleDesignListForm>();
			listvo.setTotal(page.getTotalCount());
			listvo.setRows(page.getDatas());
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return listvo;
	}
	
	/**
	 * 更新补全上传状态字段
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年3月2日 下午2:49:44
	 * @Updator bduser18
	 * @UpdateDate 2018年3月2日 下午2:49:44
	 * @UpdateDesc (更新内容描述)
	 * @param pagelet
	 * @throws Exception if has error(直接往外抛)
	 */
	private void uploadState(Pagelet pagelet) throws Exception{
		List<Object> datas = pagelet.getDatas();
		List<Object> list = new ArrayList<Object>(datas.size()); 
		for (Object obj : datas) {
			ModuleDesignListForm form = (ModuleDesignListForm) obj;
			ResourceOperEntity resourceOper = resourceOperService.findLatestUpload((String)form.getId());
			ModuleDesignListVO vo = new ModuleDesignListVO();
			BeanUtils.copyPropertiesNotNull(vo, form);
			if (resourceOper != null){
				vo.setUploadState(resourceOper.getStatus());
			} 
			list.add(vo);
		}
		pagelet.setDatas(list);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:57:43
	 * @todo 
	 * @param pagelet
	 */
	protected void dataSource(Pagelet pagelet) {
		List<Object> datas = pagelet.getDatas();
		List<Object> list = new ArrayList<Object>(datas.size()); 
		for (Object obj : datas) {
			MdModuleEntity entity = (MdModuleEntity) obj;
			ModuleDesignListForm form = new ModuleDesignListForm();
			form.setEntityToFormProperties(entity);
			
			ModuleDatabaseEntity dataSource;
			try {
				dataSource = moduleDataBaseService.find(entity.getDataSourceId());
				if (dataSource == null){
					continue;
				}
				form.setDataSourceName(dataSource.getName());
				form.setDataSourceType(dataSource.getType());
				
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			
			list.add(form);
		}
		pagelet.setDatas(list);
	}
}
