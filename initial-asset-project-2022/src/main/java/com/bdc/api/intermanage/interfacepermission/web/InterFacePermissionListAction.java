package com.bdc.api.intermanage.interfacepermission.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badou.brms.dboperation.query.*;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.interfacepermission.web.form.InterFacePermissionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.annotation.PageMdJsonStack;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.badou.tools.common.util.ParamUtils;
import com.badou.tools.common.util.StringUtils;


@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping(value="/api/intermanage/interfacepermission/interfacepermissionlist/")
public class InterFacePermissionListAction extends BaseCommonListAction {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */

	@Autowired
	private IInterFacePermissionService interFacePermissionService;

    @Override
    @BaseMdJsonStack
    @PageMdJsonStack
    @RequestMapping(value = "listJSON")
    public LigeruiListVO<JSONObject> listJSON() {
        try {
            exeBeforeList(false);
            ICriterion criterion = (ICriterion)((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getAttribute("Request_Criterion");
            interFacePermissionService.findPages(criterion);
            this.convert(getPagelet());
            exeAfterList();
            listvo = new LigeruiListVO();
            listvo.setTotal(getPagelet().getTotalCount().intValue());
            listvo.setRows(getPagelet().getDatas());
        } catch (Exception e) {
            e.printStackTrace();
			logger.error("error:",e);
        }
        return listvo;
    }

    /**
     * 格式化数据
     */
	protected void convert(Pagelet result){
        List<Object> values = new ArrayList<Object>();
        if(result.getDatas() != null && !result.getDatas().isEmpty()){
            for(Object t : result.getDatas()){
                values.add(convert((InterFacePermissionEntity)t));
            }
        }
        result.setDatas(null);
        result.setDatas(values);
    }

	/**
	 *
	 * 描述：转换数据
	 * @author linxiaoqing
	 * @date 2019年8月30日
	 * @param t
	 * @return
	 */
	protected InterFacePermissionForm convert(InterFacePermissionEntity t){
	    InterFacePermissionForm x = new InterFacePermissionForm();
        x.setEntityToFormProperties((InterFacePermissionEntity)t);
        return x;
    }


	@Override
	protected void exeBeforeList(Boolean isExport) throws Exception {
	    QueryCriterion queryCriterion = new QueryCriterion();
        Map<String, String> params = getParams();
        String test = request.getParameter("netId");
		String netId = params.get("netId")==null?ParamUtils.getParameter(request,"netId"):null;
		String interfaceId = params.get("interfaceId");
		String status = params.get("status");
		String type = params.get("type");
		String name = params.get("name");
		String IsCallInterface = params.get("IsCallInterface");
		String appId = params.get("appId");
		String appkey = params.get("appkey");
		String interfacePermission = params.get("interfacePermission");
		//外网系统权限查看
		if(StringUtils.isNotBlank(netId)){
			queryCriterion.addParam(new QueryHibernatePlaceholderParam("netInformationEntity.id", netId, null,QueryOperSymbolEnum.eq));

			if(StringUtils.isNotBlank(status)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("interFaceDetailEntity.status", Integer.parseInt(status), null,QueryOperSymbolEnum.eq));
			}
			if(StringUtils.isNotBlank(type)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("interFaceDetailEntity.type", Integer.parseInt(type), null,QueryOperSymbolEnum.eq));
			}
			if(StringUtils.isNotBlank(interfacePermission)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("interfacePermission", Integer.parseInt(interfacePermission), null,QueryOperSymbolEnum.eq));
			}
			if(StringUtils.isNotBlank(name)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("interFaceDetailEntity.name", name, null,QueryOperSymbolEnum.like));
			}
		}
		//接口权限查看
		if(StringUtils.isNotBlank(interfaceId)){
			queryCriterion.addParam(new QueryHibernatePlaceholderParam("interfaceId", interfaceId, null,QueryOperSymbolEnum.eq));

			if(StringUtils.isNotBlank(IsCallInterface)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("netInformationEntity.IsCallInterface", Integer.parseInt(IsCallInterface), null,QueryOperSymbolEnum.eq));
			}
			if(StringUtils.isNotBlank(appId)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("netInformationEntity.appId", appId, null,QueryOperSymbolEnum.like));
			}
			if(StringUtils.isNotBlank(appkey)){
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("netInformationEntity.appkey", appkey, null,QueryOperSymbolEnum.like));
			}
		}
		queryCriterion.addOrder(new QueryOrderby(0, "createTime", SortOrderEnum.desc));

		request.setAttribute(Request_Criterion, queryCriterion);
	}

	/**
	 *
	 * 描述：将查询参数组装成Map格式
	 * @author linxiaoqing
	 * @date 2019年8月29日
	 * @return
	 */
	private Map<String, String> getParams() {
	    // 查询参数
        String searchParam = ParamUtils.getParameter(request, "searchParam", null);
        // 解析参数
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(searchParam)) {
            JSONArray paramArray = JSONArray.parseArray(searchParam);
            for (Object item : paramArray) {
                JSONObject param = JSONObject.parseObject(item.toString(), JSONObject.class);
                params.put(param.getString("name"), param.getString("value"));
            }
        }
        return params;
	}


}
