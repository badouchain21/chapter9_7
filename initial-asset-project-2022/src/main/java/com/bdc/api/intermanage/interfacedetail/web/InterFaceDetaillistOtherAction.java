package com.bdc.api.intermanage.interfacedetail.web;

import java.util.List;

import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.brms.dboperation.query.ICriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.core.annotation.PageMdJsonStack;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.badou.designer.jdbc.common.web.form.SearchParamVO;
import com.badou.tools.common.util.ParamUtils;

@RestController
@Api(value="InterFaceDetaillistOtherAction")
@RequestMapping(value="/interfacedetail")
public class InterFaceDetaillistOtherAction extends BaseCommonListAction {

	/**
	 * @Field @serialVersionUID : TODO(自定义继承JsonEditTemplateAction)
	 */

	@Autowired
	private IInterFaceDetailService interFaceDetailService;

	@RequestMapping(value="listByNetId")
    @BaseMdJsonStack
    @PageMdJsonStack
	public LigeruiListVO<JSONObject> listByNetId(){
        try {
            String netId = request.getParameter("netId");
            Boolean isOther = ParamUtils.getBooleanParameter(request,"isOther");
            StringBuilder searchSql = new StringBuilder();
            String searchParam = ParamUtils.getParameter(request, "searchParam");
            if(StringUtils.isNotBlank(searchParam)){
	        	List<SearchParamVO> paramList = JSONArray.parseArray(searchParam, SearchParamVO.class);

				for (SearchParamVO param : paramList) {
					searchSql.append(param.getName()+" "+QueryOperSymbolEnum.like.getName()+" '%"+param.getValue()+"%' ");
				}
            }
            Pagelet pagelet = interFaceDetailService.findOtherByNetId(netId,searchSql.toString(),isOther);
            listvo = new LigeruiListVO<JSONObject>();
            listvo.setTotal(pagelet.getTotalCount());
            List<Object> list = pagelet.getDatas();
            for(Object o : list){
            	if(o instanceof InterFaceDetailEntity){
            		InterFaceDetailEntity d = (InterFaceDetailEntity) o;
            		d.setInterFacePermissionEntities(null);
            	}
            }
            listvo.setRows(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:",e);
        }
        return listvo;
	}

}
